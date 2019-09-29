package com.eokuwwy.openmidirtc.tools

import io.quicktype.ControlChange
import io.quicktype.DeviceType
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.Receive
import io.quicktype.Receive as Transmit

import static com.eokuwwy.openmidirtc.tools.MidiTemplateUtils.ccIt


class SquarpImporter {

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

        SquarpImporter squarpImporter = new SquarpImporter()

        squarpImporter.createSpecFromSquarpFile(
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

    void createSpecFromSquarpFile(String filepath,
                                      String outputName,
                                      DeviceType deviceType,
                                      String manufacturer,
                                      String displayName,
                                      String manufacturerId = "00",
                                      String resourceUrl = "",
                                      List<Transmit> transmits = null,
                                      List<Receive> receives = null) {
        File f = new File(filepath)

        String deviceName = displayName ?: ""

        List<ControlChange> ccCommands = []

        ccCommands.add(
            ccIt(1, "Modulation")
        )

        for (String line : f.readLines()) {
            String[] splitter = line.split(":")
            String ccNum = splitter[0].trim()
            String paramName = splitter[1].trim()
            int ccInt = -1

            if (ccNum == "NAME") {
                deviceName = paramName
            }

            try {
                ccInt = Integer.parseInt(ccNum)
            } catch(Exception e) {
                continue
            }

            ccCommands.add(
                ccIt(ccInt, paramName)
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
                Receive.PITCH_BEND
            ] as Receive[]
        }

        midi.controlChangeCommands = ccCommands as ControlChange[]

        MidiTemplateUtils.createSpecFiles(midi, outputName)
    }
}
