package com.eokuwwy.midi.spec

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.quicktype.ControlChange
import io.quicktype.OpenMIDIRealtimeSpecification
import io.quicktype.Device
import io.quicktype.DeviceType
import io.quicktype.Receive as ReceiveOrTransmit
import io.quicktype.CCValueRange
import spock.lang.Specification

//
//  QuicktypeModelSpec.groovy
//  open-midi-rtc-schema
//
//  Created by Steve Connelly on 9/1/19.
//  Copyright © 2019 Steven Connelly. All rights reserved.
//
class QuicktypeModelSpec extends Specification {
    void 'I can create a spec for commonly reserved CCs from Quicktype generated models'() {
        given:
        String displayName = "Default CC Mappings"

        OpenMIDIRealtimeSpecification midi = new OpenMIDIRealtimeSpecification()
        midi.description = "open-midi-rtc-schema specification for Common CC usage"
        midi.implementationVersion = "1.0.0"
        midi.schemaVersion = "0.0.1"
        midi.title = "Common CC MIDI Implementation"
        midi.displayName = displayName
        midi.device = new Device()
        midi.device.deviceType = DeviceType.MULTI
        midi.device.displayName = "Default"
        midi.device.version = "1.0.0"
        midi.device.description = "Common Default CC Mappings"
        midi.device.documentationResource = "http://nickfever.com/music/midi-cc-list"
        midi.device.identifier = "00"
        midi.device.manufacturer = "N/A"
        midi.device.name = "Default"
        midi.device.model = "Default"
        midi.transmits = [
            ReceiveOrTransmit.NOTE_NUMBER,
            ReceiveOrTransmit.VELOCITY_NOTE_ON,
            ReceiveOrTransmit.VELOCITY_NOTE_OFF,
            ReceiveOrTransmit.PITCH_BEND,
            ReceiveOrTransmit.PROGRAM_CHANGE,
            ReceiveOrTransmit.CLOCK,
            ReceiveOrTransmit.CHANNEL_PRESSURE
        ] as ReceiveOrTransmit[]
        midi.receives = [
            ReceiveOrTransmit.NOTE_NUMBER,
            ReceiveOrTransmit.VELOCITY_NOTE_ON,
            ReceiveOrTransmit.VELOCITY_NOTE_OFF,
            ReceiveOrTransmit.PITCH_BEND,
            ReceiveOrTransmit.PROGRAM_CHANGE,
            ReceiveOrTransmit.CLOCK,
            ReceiveOrTransmit.CHANNEL_PRESSURE
        ] as ReceiveOrTransmit[]

        def ccIt = { ccNum, name, desc, range = new CCValueRange(min: 0, max: 127) ->
            new ControlChange(
                name: name,
                description: desc,
                controlChangeNumber: ccNum,
                valueRange: range
            )
        }

        List<ControlChange> controlChangeList = [
            ccIt(1, "Modulation", "Typically sent via a mod wheel or mod ribbon"),
            ccIt(2, "Breath Controller", "Originally intended for modulation from a breath input control"),
            ccIt(4, "Foot Controller", "Sends a continuous stream of values based on the pedal position"),
            ccIt(5, "Portamento Time", "Controls the portamento rate"),
            ccIt(7, "Volume", "Channel volume control"),
            ccIt(8, "Balance", "Control left and right balance for stereo patches"),
            ccIt(10, "Panning", "Control left and right balance for mono patches"),
            ccIt(11, "Expression", "Typically used as a percentage of volume (CC7)"),
            ccIt(12, "Effect Param 1", "Typically used to control an effect parameter"),
            ccIt(13, "Effect Param 2", "Typically used to control an effect parameter"),
            ccIt(64, "Damper Pedal", "Sustain Control"),
            ccIt(65, "Portamento On/Off", "Toggles portamento"),
            ccIt(66, "Sostenuto On/Off", "Toggles sostenuto"),
            ccIt(67, "Soft Pedal On/Off", "Softens the volume"),
            ccIt(68, "Legato On/Off", "Toggles legato mode"),
            ccIt(69, "Hold 2 On/Off", "Different type of note hold mode"),
            ccIt(71, "Filter Resonance", "Generally used for filter resonance"),
            ccIt(72, "Release Time", "Generally used for amp envelope release"),
            ccIt(73, "Attack Time", "Generally used for amp envelope attack"),
            ccIt(74, "Filter Cutoff", "Generally used for filter frequency cutoff"),
            ccIt(84, "Portamento Depth", "The amount of portamento"),
            ccIt(91, "Effect 1 Depth", "Depth of effect 1"),
            ccIt(92, "Effect 2 Depth", "Depth of effect 2"),
            ccIt(93, "Effect 3 Depth", "Depth of effect 3"),
            ccIt(94, "Effect 4 Depth", "Depth of effect 4"),
            ccIt(95, "Effect 5 Depth", "Depth of effect 5")
        ]

        midi.controlChangeCommands = controlChangeList as ControlChange[]

        and:
        ObjectMapper objectMapper = new ObjectMapper()
        YAMLMapper yamlMapper = new YAMLMapper()

        when:
        Gson gson = new GsonBuilder().setPrettyPrinting().create()
        String jsonString = gson.toJson(midi)

        // parse JSON
        JsonNode jsonNodeTree = objectMapper.readTree(jsonString)
        // save it as YAML
        String jsonAsYaml = yamlMapper.writeValueAsString(jsonNodeTree)

        File json = new File('examples/cc-defaults-from-quicktype.json')
        json.write(jsonString)

        File yml = new File('examples/cc-defaults-from-quicktype.yml')
        yml.write(jsonAsYaml)

        then:
        jsonString != null
        (objectMapper.readValue(json, OpenMIDIRealtimeSpecification) as OpenMIDIRealtimeSpecification).displayName == displayName
        (yamlMapper.readValue(yml, OpenMIDIRealtimeSpecification) as OpenMIDIRealtimeSpecification).displayName == displayName
    }
}
