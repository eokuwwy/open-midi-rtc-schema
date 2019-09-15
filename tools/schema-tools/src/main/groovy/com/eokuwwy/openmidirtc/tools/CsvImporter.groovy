package com.eokuwwy.openmidirtc.tools

import io.quicktype.ControlChange
import io.quicktype.DeviceType
import io.quicktype.HexValueRange
import io.quicktype.NonRegisteredParameterNumber
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.Receive
import io.quicktype.Receive as Transmit
import io.quicktype.SysexValue
import io.quicktype.SystemExclusive
import io.quicktype.ValueType

import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.ccIt
import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.nrpnIt
import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.sysexIt

/**
 * Create a spec file from a CSV that has CCs, NRPNs, and Sysex commands
 * Example CSV Lines:
 * cc,Filter Cutoff,74,,0,127,,,,,
 * nrpn,Filter Res,31,1,0,255,,,,,
 * sysex,PWM Depth,F0 41 32 cc 0F dd F7,MIDI_CHANNEL,3,00,0F,DATA_VALUE,5,00,7F
 * sysex,DCO Level,F0 41 32 01 0F dd F7,DATA_VALUE,5,00,7F,,,,
 */
class CsvImporter {
    static final int CSV_COLUMN_PARAM_TYPE = 0
    static final int CSV_COLUMN_PARAM_NAME = 1
    static final int CSV_COLUMN_PARAM_MSB = 2, CSV_COLUMN_PARAM_SYSEX_MESSAGE = 2
    static final int CSV_COLUMN_PARAM_LSB = 3, CSV_COLUMN_SYSEX_SUB_TYPE_1 = 3
    static final int CSV_COLUMN_PARAM_MIN_VAL = 4, CSV_COLUMN_SYSEX_SUB_INDEX_1 = 4
    static final int CSV_COLUMN_PARAM_MAX_VAL = 5, CSV_COLUMN_SYSEX_SUB_VALUE_1_MIN = 5
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_1_MAX  = 6
    static final int CSV_COLUMN_SYSEX_SUB_TYPE_2 = 7
    static final int CSV_COLUMN_SYSEX_SUB_INDEX_2 = 8
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_2_MIN = 9
    static final int CSV_COLUMN_SYSEX_SUB_VALUE_2_MAX  = 10


    static final String PARAM_TYPE_CC = 'cc'
    static final String PARAM_TYPE_NRPN = 'nrpn'
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

        CsvImporter csvImporter = new CsvImporter()

        csvImporter.createSpecFromCsv(
            fileName,
            outputFile,
            type,
            manufacturer,
            displayName,
            manID,
            url
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

        String deviceName = ""

        List<ControlChange> ccCommands = []
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
            } else if (paramType == PARAM_TYPE_NRPN) {
                NonRegisteredParameterNumber nrpn = nrpnCommand(splitter)

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

    private NonRegisteredParameterNumber nrpnCommand(String[] line) {
        String paramName = line[CSV_COLUMN_PARAM_NAME].trim()
        String paramMsb = line[CSV_COLUMN_PARAM_MSB].trim()
        String paramLsb = line[CSV_COLUMN_PARAM_LSB].trim()
        String minValue = line[CSV_COLUMN_PARAM_MIN_VAL].trim()
        String maxValue = line[CSV_COLUMN_PARAM_MAX_VAL].trim()

        int minVal = 0
        int maxVal = 127

        try {
            int msb = Integer.parseInt(paramMsb)
            int lsb = Integer.parseInt(paramLsb)

            if (!minValue.isEmpty()) {
                minVal = Integer.parseInt(minValue)
            }

            if (!maxValue.isEmpty()) {
                maxVal = Integer.parseInt(maxValue)
            }

            return nrpnIt(paramName, msb, lsb, minVal, maxVal)
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
