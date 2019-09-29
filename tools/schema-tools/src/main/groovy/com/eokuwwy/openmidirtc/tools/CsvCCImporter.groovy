package com.eokuwwy.openmidirtc.tools

import io.quicktype.ControlChange
import io.quicktype.DeviceType
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.Receive
import io.quicktype.Receive as Transmit

import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.ccIt


/**
 * Create a spec file from a CSV that has simple CCs only
 * Example CSV file line:
 * 1,MODULATION,0,127
 */
class CsvCCImporter {
    static final int CSV_COLUMN_PARAM_CC_NUMBER = 0
    static final int CSV_COLUMN_PARAM_CC_LABEL = 1
    static final int CSV_COLUMN_PARAM_CC_MIN_VAL = 2
    static final int CSV_COLUMN_PARAM_CC_MAX_VAL = 3

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

        CsvCCImporter csvImporter = new CsvCCImporter()

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

        for (String line : f.readLines()) {
            String[] splitter = line.split(",", -1)

            if (splitter.size() != 4) {
                continue
            }

            String ccNum = splitter[CSV_COLUMN_PARAM_CC_NUMBER].trim()
            String paramName = splitter[CSV_COLUMN_PARAM_CC_LABEL].trim()
            String minValue = splitter[CSV_COLUMN_PARAM_CC_MIN_VAL].trim()
            String maxValue = splitter[CSV_COLUMN_PARAM_CC_MAX_VAL].trim()

            int ccInt = -1
            int minVal = 0
            int maxVal = 127

            try {
                ccInt = Integer.parseInt(ccNum)

                if (!minValue.isEmpty()) {
                    minVal = Integer.parseInt(minValue)
                }

                if (!maxValue.isEmpty()) {
                    maxVal = Integer.parseInt(maxValue)
                }
            } catch(Exception e) {
                continue
            }

            ccCommands.add(
                ccIt(ccInt, paramName, minVal, maxVal)
            )
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

        MidiTemplateUtils.createSpecFiles(midi, outputName)
    }
}
