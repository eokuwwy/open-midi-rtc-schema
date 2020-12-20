package com.eokuwwy.openmidirtc.tools

import io.quicktype.ControlChange
import io.quicktype.ControlChangeSequence
import io.quicktype.DeviceType
import io.quicktype.HexValueRange
import io.quicktype.NonRegisteredParameterNumber
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.Receive
import io.quicktype.Receive as Transmit
import io.quicktype.SysexValue
import io.quicktype.SystemExclusive
import io.quicktype.ValueSequenceType
import io.quicktype.ValueType

import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.ccIt
import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.ccSequenceIt
import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.nrpnIt
import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.sysexIt

/**
 * Create a spec file from a CSV that has CCs, NRPNs, and Sysex commands
 * Example CSV Lines:
 * cc,Filter Cutoff,74,,0,127,,,,,
 * ccSeq,Filter Cutoff,71,23,0,127,0,127,MSB_LSB,,,,
 * nrpnDecimal,Filter Res,31,1,0,255,,,,,
 * nrpn,Filter Res,31,1,0,127,0,127,MSB_LSB,,
 * sysex,PWM Depth,F0 41 32 cc 0F dd F7,MIDI_CHANNEL,3,00,0F,DATA_VALUE,5,00,7F
 * sysex,DCO Level,F0 41 32 01 0F dd F7,DATA_VALUE,5,00,7F,,,,
 */
class CsvImporter {
    static final int CSV_COLUMN_PARAM_TYPE = 0
    static final int CSV_COLUMN_PARAM_NAME = 1
    static final int CSV_COLUMN_PARAM_MSB = 2, CSV_COLUMN_PARAM_SYSEX_MESSAGE = 2
    static final int CSV_COLUMN_PARAM_LSB = 3, CSV_COLUMN_SYSEX_SUB_TYPE_1 = 3
    static final int CSV_COLUMN_PARAM_MIN_VAL = 4, CSV_COLUMN_SYSEX_SUB_INDEX_1 = 4, CSV_COLUMN_PARAM_MSB_MIN_VAL = 4
    static final int CSV_COLUMN_PARAM_MAX_VAL = 5, CSV_COLUMN_SYSEX_SUB_VALUE_1_MIN = 5, CSV_COLUMN_PARAM_MSB_MAX_VAL = 5
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_1_MAX  = 6, CSV_COLUMN_PARAM_LSB_MIN_VAL = 6
    static final int CSV_COLUMN_SYSEX_SUB_TYPE_2 = 7, CSV_COLUMN_PARAM_LSB_MAX_VAL = 7
    static final int CSV_COLUMN_SYSEX_SUB_INDEX_2 = 8, CSV_COLUMN_VALUE_SEQUENCE_TYPE  = 8 // MSB_ONLY, MSB_LSB, MSB_UNTIL_OVERFLOW
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_2_MIN = 9
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_2_MAX  = 10

    static final String PARAM_TYPE_CC = 'cc'
    static final String PARAM_TYPE_CC_SEQUENCE = 'ccSeq'
    static final String PARAM_TYPE_NRPN_DECIMAL = 'nrpnDecimal' // NRPNs in decimal notation without breaking up into MSB and LSB
    static final String PARAM_TYPE_NRPN = 'nrpn' // akin to CC Sequence, uses MSB and LSB
    static final String PARAM_TYPE_SYSEX = 'sysex'

    static void main(String[] args) {
        String fileName = args[0]
        String outputFile = args[1]
        DeviceType type = args.length > 2 ?
            DeviceType.forValue(args[2]) :
            DeviceType.SYNTHESIZER
        String manufacturer = args.length > 3 ? args[3] : ""
        String displayName = args.length > 4 ? args[4] : ""
        String manID = args.length > 5 ? args[5] : ""
        String url = args.length > 6 ? args[6] : ""
        String transmits = args.length > 7 ? args[7] : ""
        String receives = args.length > 8 ? args[8] : ""
        List<Transmit> trans = []
        List<Receive> recs = []

        transmits.split(",")?.each {
            try {
                trans.add(Transmit.forValue(it.toUpperCase()))
            } catch (Exception e) {
                // eat
            }
        }

        receives.split(",")?.each {
            try {
                recs.add(Receive.forValue(it.toUpperCase()))
            } catch (Exception e) {
                // eat
            }
        }

        CsvImporter csvImporter = new CsvImporter()

        csvImporter.createSpecFromCsv(
            fileName,
            outputFile,
            type,
            manufacturer,
            displayName,
            manID,
            url,
            trans,
            recs
        )
    }

    void createSpecFromCsv(String filepath,
                           String outputName,
                           DeviceType deviceType,
                           String manufacturer,
                           String displayName,
                           String manufacturerId = "00",
                           String resourceUrl = "",
                           List<Transmit> transmits = null,
                           List<Receive> receives = null) {

        println("Creating Spec From CSV...\n" +
            "File: ${filepath}\n" +
            "Output: $outputName\n" +
            "DeviceType: ${deviceType.name()}\n" +
            "Manufacturer: ${manufacturer}\n" +
            "DisplayName: ${displayName}\n" +
            "Manufacturer ID: ${manufacturerId}\n" +
            "URL: ${resourceUrl}\n"
        )

        File f = new File(filepath)

        String deviceName = displayName ?: ""

        List<ControlChange> ccCommands = []
        List<ControlChangeSequence> ccSequenceCommands = []
        List<NonRegisteredParameterNumber> nrpnCommands = []
        List<SystemExclusive> sysexCommands = []

        for (String line : f.readLines()) {
            String[] splitter = line.split(",", -1)

            if (splitter.size() != 11) {
                continue
            }

            String paramType = splitter[CSV_COLUMN_PARAM_TYPE].trim()

            if (paramType == PARAM_TYPE_CC) {
                ControlChange cc = ccCommand(splitter)

                if (cc != null) {
                    ccCommands.add(cc)
                }
            } else if (paramType == PARAM_TYPE_CC_SEQUENCE) {
                ControlChangeSequence ccs = ccSequenceCommand(splitter)

                if (ccs != null) {
                    ccSequenceCommands.add(ccs)
                }
            } else if (paramType == PARAM_TYPE_NRPN_DECIMAL) {
                NonRegisteredParameterNumber nrpn = nrpnDecimalCommand(splitter)

                if (nrpn != null) {
                    nrpnCommands.add(nrpn)
                }
            } else if (paramType == PARAM_TYPE_NRPN) {
                NonRegisteredParameterNumber nrpn = nrpnSequenceCommand(splitter)

                if (nrpn != null) {
                    nrpnCommands.add(nrpn)
                }
            } else if (paramType == PARAM_TYPE_SYSEX) {
                SystemExclusive sysex = sysexCommand(splitter)

                if (sysex != null) {
                    sysexCommands.add(sysex)
                }
            }
        }

        OpenMIDIRealtimeSpecification midi = MidiTemplateUtils.basicMidiTemplate(
            deviceName,
            deviceType,
            displayName,
            manufacturer,
            manufacturerId
        )

        midi.device.documentationResource = resourceUrl

        if (transmits != null) {
            midi.transmits = transmits as Transmit[]
        } else {
            midi.transmits = [
                Transmit.NOTE_NUMBER,
                Transmit.PITCH_BEND
            ] as Transmit[]
        }

        if (receives != null) {
            midi.receives = receives as Receive[]
        } else {
            midi.receives = [
                Receive.NOTE_NUMBER,
                Receive.PITCH_BEND,
                Receive.VELOCITY_NOTE_ON
            ] as Receive[]
        }

        midi.controlChangeCommands = ccCommands as ControlChange[]
        midi.nrpnCommands = nrpnCommands as NonRegisteredParameterNumber[]
        midi.controlChangeSequenceCommands = ccSequenceCommands as ControlChangeSequence[]
        midi.sysexCommands = sysexCommands as SystemExclusive[]

        MidiTemplateUtils.createSpecFiles(midi, outputName)
    }

    private ControlChange ccCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramNumber = line[CSV_COLUMN_PARAM_MSB].trim()
        String minValue = line[CSV_COLUMN_PARAM_MIN_VAL].trim()
        String maxValue = line[CSV_COLUMN_PARAM_MAX_VAL].trim()

        int minVal = 0
        int maxVal = 127

        try {
            int ccInt = Integer.parseInt(paramNumber)

            if (!minValue.isEmpty()) {
                minVal = Integer.parseInt(minValue)
            }

            if (!maxValue.isEmpty()) {
                maxVal = Integer.parseInt(maxValue)
            }

            return ccIt(ccInt, paramName, minVal, maxVal)
        } catch(Exception e) {
            return null
        }
    }

    private ControlChangeSequence ccSequenceCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramNumberMsb = line[CSV_COLUMN_PARAM_MSB].trim()
        String paramNumberLsb = line[CSV_COLUMN_PARAM_LSB].trim()
        String minMsbValue = line[CSV_COLUMN_PARAM_MSB_MIN_VAL].trim()
        String maxMsbValue = line[CSV_COLUMN_PARAM_MSB_MAX_VAL].trim()
        String minLsbValue = line[CSV_COLUMN_PARAM_LSB_MIN_VAL].trim()
        String maxLsbValue = line[CSV_COLUMN_PARAM_LSB_MAX_VAL].trim()
        String sequenceType = line[CSV_COLUMN_VALUE_SEQUENCE_TYPE].trim()

        int minMsbVal = 0
        int maxMsbVal = 127

        int minLsbVal = 0
        int maxLsbVal = 127

        ValueSequenceType sequenceTypeVal = ValueSequenceType.MSB_LSB

        try {
            int ccMsbInt = Integer.parseInt(paramNumberMsb)
            int ccLsbInt = Integer.parseInt(paramNumberLsb)

            if (!minMsbValue.isEmpty()) {
                minMsbVal = Integer.parseInt(minMsbValue)
            }

            if (!maxMsbValue.isEmpty()) {
                maxMsbVal = Integer.parseInt(maxMsbValue)
            }

            if (!minLsbValue.isEmpty()) {
                minLsbVal = Integer.parseInt(minLsbValue)
            }

            if (!maxLsbValue.isEmpty()) {
                maxLsbVal = Integer.parseInt(maxLsbValue)
            }

            if (!sequenceType.isEmpty()) {
                if (sequenceType == "MSB_ONLY") {
                    sequenceTypeVal = ValueSequenceType.MSB_ONLY
                } else if (sequenceType == "MSB_UNTIL_OVERFLOW") {
                    sequenceTypeVal = ValueSequenceType.MSB_UNTIL_OVERFLOW
                }
            }

            return ccSequenceIt(ccMsbInt, ccLsbInt, paramName, minMsbVal, maxMsbVal, minLsbVal, maxLsbVal, sequenceTypeVal)
        } catch(Exception e) {
            return null
        }
    }

    private NonRegisteredParameterNumber nrpnSequenceCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramNumberMsb = line[CSV_COLUMN_PARAM_MSB].trim()
        String paramNumberLsb = line[CSV_COLUMN_PARAM_LSB].trim()
        String minMsbValue = line[CSV_COLUMN_PARAM_MSB_MIN_VAL].trim()
        String maxMsbValue = line[CSV_COLUMN_PARAM_MSB_MAX_VAL].trim()
        String minLsbValue = line[CSV_COLUMN_PARAM_LSB_MIN_VAL].trim()
        String maxLsbValue = line[CSV_COLUMN_PARAM_LSB_MAX_VAL].trim()
        String sequenceType = line[CSV_COLUMN_VALUE_SEQUENCE_TYPE].trim()

        int minMsbVal = 0
        int maxMsbVal = 127

        int minLsbVal = 0
        int maxLsbVal = 127

        ValueSequenceType sequenceTypeVal = ValueSequenceType.MSB_LSB

        try {
            int nrpnMsbInt = Integer.parseInt(paramNumberMsb)
            int nrpnLsbInt = Integer.parseInt(paramNumberLsb)

            if (!minMsbValue.isEmpty()) {
                minMsbVal = Integer.parseInt(minMsbValue)
            }

            if (!maxMsbValue.isEmpty()) {
                maxMsbVal = Integer.parseInt(maxMsbValue)
            }

            if (!minLsbValue.isEmpty()) {
                minLsbVal = Integer.parseInt(minLsbValue)
            }

            if (!maxLsbValue.isEmpty()) {
                maxLsbVal = Integer.parseInt(maxLsbValue)
            }

            if (!sequenceType.isEmpty()) {
                if (sequenceType == "MSB_ONLY") {
                    sequenceTypeVal = ValueSequenceType.MSB_ONLY
                } else if (sequenceType == "MSB_UNTIL_OVERFLOW") {
                    sequenceTypeVal = ValueSequenceType.MSB_UNTIL_OVERFLOW
                }
            }

            return nrpnIt(nrpnMsbInt, nrpnLsbInt, paramName, minMsbVal, maxMsbVal, minLsbVal, maxLsbVal, sequenceTypeVal)
        } catch(Exception e) {
            println("ERROR: ${e.getMessage()}")
            return null
        }
    }

    private NonRegisteredParameterNumber nrpnDecimalCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramMsb = line[CSV_COLUMN_PARAM_MSB].trim()
        String paramLsb = line[CSV_COLUMN_PARAM_LSB].trim()
        String minValue = line[CSV_COLUMN_PARAM_MIN_VAL].trim()
        String maxValue = line[CSV_COLUMN_PARAM_MAX_VAL].trim()

        int minVal = 0
        int maxVal = 127
        int lsb = -1

        try {
            int msb = Integer.parseInt(paramMsb)

            if (!paramLsb.isEmpty()) {
                lsb = Integer.parseInt(paramLsb)
            }

            if (!minValue.isEmpty()) {
                minVal = Integer.parseInt(minValue)
            }

            if (!maxValue.isEmpty()) {
                maxVal = Integer.parseInt(maxValue)
            }

            if (lsb > -1) {
                return nrpnIt(paramName, msb, lsb, minVal, maxVal)
            } else {
                return nrpnIt(msb, paramName, minVal, maxVal)
            }
        } catch(Exception e) {
            return null
        }
    }

    private SystemExclusive sysexCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramMessage = line[CSV_COLUMN_PARAM_SYSEX_MESSAGE].trim()
        String param1Type = line[CSV_COLUMN_SYSEX_SUB_TYPE_1].trim()
        String param1Index = line[CSV_COLUMN_SYSEX_SUB_INDEX_1].trim()
        String param1Min = line[CSV_COLUMN_SYSEX_SUB_VALUE_1_MIN].trim()
        String param1Max = line[CSV_COLUMN_SYSEX_SUB_VALUE_1_MAX].trim()
        String param2Type = line[CSV_COLUMN_SYSEX_SUB_TYPE_2].trim()
        String param2Index = line[CSV_COLUMN_SYSEX_SUB_INDEX_2].trim()
        String param2Min = line[CSV_COLUMN_SYSEX_SUB_VALUE_2_MIN].trim()
        String param2Max = line[CSV_COLUMN_SYSEX_SUB_VALUE_2_MAX].trim()

        List<SysexValue> values = []

        try {
            if (!param1Type.isEmpty() && !param1Index.isEmpty()) {
                String minVal = "00"
                String maxVal = "7F"

                int index = Integer.parseInt(param1Index)
                String subFor = paramMessage.split(" ")[index]

                if (!param1Min.isEmpty()) {
                    minVal = param1Min
                }

                if (!param1Max.isEmpty()) {
                    maxVal = param1Max
                }

                values.add(
                    new SysexValue(
                        index: index,
                        substituteFor: subFor,
                        valueType: ValueType.forValue(param1Type),
                        valueRange: new HexValueRange(min: minVal, max: maxVal)
                    )
                )
            }

            if (!param2Type.isEmpty() && !param2Index.isEmpty()) {
                String minVal = "00"
                String maxVal = "7F"

                int index = Integer.parseInt(param2Index)
                String subFor = paramMessage.split(" ")[index]

                if (!param2Min.isEmpty()) {
                    minVal = param2Min
                }

                if (!param2Max.isEmpty()) {
                    maxVal = param2Max
                }

                values.add(
                    new SysexValue(
                        index: index,
                        substituteFor: subFor,
                        valueType: ValueType.forValue(param2Type),
                        valueRange: new HexValueRange(min: minVal, max: maxVal)
                    )
                )
            }

            return sysexIt(paramName, paramMessage, values)
        } catch(Exception e) {
            return null
        }
    }
}
