package com.eokuwwy.openmidirtc.tools

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.quicktype.CCValueRange
import io.quicktype.ControlChange
import io.quicktype.Device
import io.quicktype.DeviceType
import io.quicktype.NonRegisteredParameterNumber
import io.quicktype.NrpnCommandValueRange
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.SystemExclusive

class MidiTemplateUtils {
    static final String BASE_OUTPUT_DIR = "output"

    static OpenMIDIRealtimeSpecification basicMidiTemplate(String deviceName,
                                                           DeviceType deviceType,
                                                           String displayName = null,
                                                           String manufactuerName = null,
                                                           String manufacturerId = "00") {
        if (displayName == null) {
            displayName = deviceName
        }

        OpenMIDIRealtimeSpecification midi = new OpenMIDIRealtimeSpecification()
        midi.description = "open-midi-rtc-schema specification for ${deviceName}"
        midi.implementationVersion = "1.0.0"
        midi.schemaVersion = "0.0.1"
        midi.title = "$deviceName MIDI Implementation"
        midi.displayName = "$deviceName"
        midi.device = new Device()
        midi.device.deviceType = deviceType
        midi.device.displayName = "$displayName"
        midi.device.version = "1.0.0"
        midi.device.description = "$deviceName Parameter Mappings"
        midi.device.documentationResource = ""
        midi.device.identifier = manufacturerId
        midi.device.manufacturer = manufactuerName ?: "Unknown"
        midi.device.name = "$deviceName"
        midi.device.model = "$deviceName"

        return midi
    }

    /**
     * Convenience method for building a ControlChange instance
     * @param number
     * @param name
     * @param maxValue
     * @return
     */
    static ControlChange ccIt(int number, String name, int maxValue = 127) {
        new ControlChange(
            name: name,
            controlChangeNumber: number,
            valueRange: new CCValueRange(min: 0, max: maxValue)
        )
    }

    /**
     * Convenience method for building a ControlChange instance
     * @param number
     * @param name
     * @param minValue
     * @param maxValue
     * @return
     */
    static ControlChange ccIt(int number, String name, int minValue, int maxValue) {
        new ControlChange(
            name: name,
            controlChangeNumber: number,
            valueRange: new CCValueRange(min: minValue, max: maxValue)
        )
    }

    /**
     * Convenience method for building an NRPN instance
     * @param name
     * @param msb
     * @param lsb
     * @param min
     * @param max
     * @return
     */
    static NonRegisteredParameterNumber nrpnIt(String name, int msb, int lsb, int min = 0, int max = 127) {
        new NonRegisteredParameterNumber(
            name: name,
            parameterNumber: convertIt(msb, lsb),
            valueRange: new NrpnCommandValueRange(min: min, max: max)
        )
    }

    /**
     * Convenience method for building an NRPN instance, using the decimal version for the parameter number, instead
     * of separate LSB and MSB
     * @param number
     * @param name
     * @param min
     * @param max
     * @return
     */
    static NonRegisteredParameterNumber nrpnIt(int number, String name, int min = 0, int max = 127) {
        new NonRegisteredParameterNumber(
            name: name,
            parameterNumber: number,
            valueRange: new NrpnCommandValueRange(min: min, max: max)
        )
    }

    private static int convertIt(int msb, int lsb) {
        int msbToInt = msb << 7
        return lsb + msbToInt
    }

    private static initOutputDir() {
        new File("${BASE_OUTPUT_DIR}").mkdirs()
    }

    static void createSpecFiles(OpenMIDIRealtimeSpecification midi, String name) {
        initOutputDir()

        Gson gson = new GsonBuilder().setPrettyPrinting().create()
        String jsonString = gson.toJson(midi)
        println(jsonString)

        // parse JSON
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString)
        // save it as YAML
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree)

        println(jsonAsYaml)

        File json = new File("${BASE_OUTPUT_DIR}/${name}.json")
        json.write(jsonString)

        File yml = new File("${BASE_OUTPUT_DIR}/${name}.yml")
        yml.write(jsonAsYaml)

        assert jsonString != null

        File f = new File("${BASE_OUTPUT_DIR}/${name}.json")
        OpenMIDIRealtimeSpecification midiFile = new ObjectMapper().readValue(f, OpenMIDIRealtimeSpecification)

        assert midiFile != null

        writeDocumentationFromSpecFile(name)
    }

    static void writeDocumentationFromSpecFile(String specName) {
        File f = new File("${BASE_OUTPUT_DIR}/${specName}.json")
        OpenMIDIRealtimeSpecification midiFile = new ObjectMapper().readValue(f, OpenMIDIRealtimeSpecification)

        String title = "${midiFile.displayName} Controllable Parameters\n"
        StringBuilder sb = new StringBuilder("<html><head><title>$title</title></head><body>\n")
        sb.append("<style>\n")
        sb.append("tr, td {\n")
        sb.append("border-bottom: 1px solid black;\n}\n")
        sb.append(".firstCol {\n" +
            "        border-left: 1px solid black;\n" +
            "    }\n" +
            "    .lastCol {\n" +
            "        border-right: 1px solid black;\n" +
            "    }\n")
        sb.append("</style>\n")
        sb.append("<h1>$title</h1>\n")
        sb.append("<hr/>\n")

        if (midiFile.controlChangeCommands) {
            sb.append("<h3>Control Change List</h3>\n")

            List<ControlChange> commands = midiFile.controlChangeCommands.sort { a, b ->
                a.controlChangeNumber <=> b.controlChangeNumber
            }

            sb.append("<table border=\"0\" cellpadding=\"5\" cellspacing=\"0\"><tr><td>Parameter Name</td><td>CC Number</td><td>Value Range</td><td>Info</td></tr>\n")
            commands.each {
                String additionalInfo = it.description ?: ""
                if (additionalInfo && it.additionalInfo) {
                    additionalInfo += " - ${it.additionalInfo}"
                } else if (it.additionalInfo) {
                    additionalInfo = it.additionalInfo
                }
                sb.append("<tr><td class=\"firstCol\">${it.name}</td><td>${it.controlChangeNumber}</td><td>${it.valueRange.min} - ${it.valueRange.max}</td><td class=\"lastCol\">${additionalInfo}</td></tr>\n")
            }
            sb.append("</table>\n")
            sb.append("<br><br>\n")
        }

        if (midiFile.nrpnCommands) {
            sb.append("<hr/>\n")
            sb.append("<h3>NRPN Command List</h3>\n")

            List<NonRegisteredParameterNumber> commands = midiFile.nrpnCommands.sort { a, b ->
                a.parameterNumber <=> b.parameterNumber
            }

            sb.append("<table border=\"0\" cellpadding=\"5\" cellspacing=\"0\"><tr><td>Parameter Name</td><td>Param Number</td><td>MSB</td><td>LSB</td><td>Value Range</td><td>Info</td></tr>\n")
            commands.each {
                String additionalInfo = it.description ?: ""
                if (additionalInfo && it.additionalInfo) {
                    additionalInfo += " - ${it.additionalInfo}"
                } else if (it.additionalInfo) {
                    additionalInfo = it.additionalInfo
                }

                int lsb = it.parameterNumber & 127
                int msb= it.parameterNumber >> 7

                sb.append("<tr><td class=\"firstCol\">${it.name}</td><td>${it.parameterNumber}</td><td>$msb</td><td>$lsb</td><td>${it.valueRange.min} - ${it.valueRange.max}</td><td class=\"lastCol\">${additionalInfo}</td></tr>\n")
            }
            sb.append("</table>\n")
            sb.append("<br><br>\n")
        }

        if (midiFile.sysexCommands) {
            sb.append("<hr/>\n")
            sb.append("<h3>System Exclusive Command List</h3>\n")

            List<SystemExclusive> commands = midiFile.sysexCommands.sort { a, b ->
                a.name <=> b.name
            }

            sb.append("<table border=\"0\" cellpadding=\"5\" cellspacing=\"0\"><tr><td>Parameter Name</td><td>Sysex String</td><td>Info</td></tr>\n")
            commands.each {
                String additionalInfo = it.description ?: ""
                if (additionalInfo && it.additionalInfo) {
                    additionalInfo += " - ${it.additionalInfo}"
                } else if (it.additionalInfo) {
                    additionalInfo = it.additionalInfo
                }
                sb.append("<tr><td class=\"firstCol\">${it.name}</td><td>${it.substitutableMessage}</td><td class=\"lastCol\">${additionalInfo}</td></tr>\n")
            }
            sb.append("</table>\n")
            sb.append("<br><br>\n")
        }

        sb.append("</body></html>\n")

        File doc = new File("${BASE_OUTPUT_DIR}/${specName}.html")
        doc.write(sb.toString())
    }
}
