//
//  ReadSpecFromQuicktypeModel.swift
//  open-midi-rtc-schema
//
//  Created by Steve Connelly on 9/1/19.
//  Copyright © 2019 Steven Connelly. All rights reserved.
//

class ReadSpecFromQuicktypeModel {
    struct ParameterValueRange:Codable {
        var min: Int
        var max: Int

        func range() -> Int {
            return max - min
        }

        static let defaultParameterValueRange:ParameterValueRange = ParameterValueRange(min: 0, max: 127)
    }

    struct DestinationParameter: Codable {
        enum DestinationParameterType : String, Codable {
            case controlChange = "cc", sysex = "sysex", nrpn = "nrpn", rpn = "rpn", pitch = "PW", at="AT", note="Note"
        }

        var type:DestinationParameterType
        var channel:UInt
        var value:String
        var allowedParameterValueRange:ParameterValueRange

        init(type: DestinationParameterType, channel: UInt, value: String, allowedParameterValueRange:ParameterValueRange) {
            self.type = type
            self.channel = channel
            self.value = value
            self.allowedParameterValueRange = allowedParameterValueRange
        }
    }

    var destinationMappings:[String:DestinationParameter] = [:]

    func processSpecFromUrl(_ url: URL) {
        do {
            // parse open-midi-rtc-schema spec file into Quicktype generated model
            let midiSpec = try OpenMIDIRealtimeSpecification(fromURL: url)

            for receiveType in midiSpec.receives {
                if receiveType == .channelPressure {
                    destinationMappings["Aftertouch"] = DestinationParameter(type: .at, channel: 0, value: "AT", allowedParameterValueRange: ParameterValueRange(min: 0, max: 127))
                } else if receiveType == .pitchBend {
                    destinationMappings["Pitch Bend"] = DestinationParameter(type: .pitch, channel: 0, value: "PB", allowedParameterValueRange: ParameterValueRange(min: 0, max: 127))
                } else if receiveType == .noteNumber {
                    destinationMappings["Note Number"] = DestinationParameter(type: .note, channel: 0, value: "Note Num", allowedParameterValueRange: ParameterValueRange(min: 0, max: 127))
                }
            }

            if let ccMappings = midiSpec.controlChangeCommands {
                for cc in ccMappings {
                    destinationMappings[cc.name] = DestinationParameter(type: .controlChange, channel: 0, value: "\(cc.controlChangeNumber)", allowedParameterValueRange: ParameterValueRange(min: cc.valueRange.min, max: cc.valueRange.max))
                }
            }

            if let nrpnMappings = midiSpec.nrpnCommands {
                for nrpn in nrpnMappings {
                    let keyName = "\(nrpn.name) NRPN"

                    destinationMappings[keyName] = DestinationParameter(type: .nrpn, channel: 0, value: "\(nrpn.parameterNumber)", allowedParameterValueRange: ParameterValueRange(min: nrpn.valueRange.min, max: nrpn.valueRange.max))
                }
            }

            if let rpnMappings = midiSpec.rpnCommands {
                for rpn in rpnMappings {
                    let keyName = "\(rpn.name) RPN"

                    destinationMappings[keyName] = DestinationParameter(type: .rpn, channel: 0, value: "\(rpn.parameterNumber)", allowedParameterValueRange: ParameterValueRange(min: rpn.valueRange.min, max: rpn.valueRange.max))
                }
            }

            if let sysexMappings = midiSpec.sysexCommands {
                for sysexCommand in sysexMappings {
                    var sysexBytes = sysexCommand.substitutableMessage.components(separatedBy: " ")

                    // chop off the F0 and F7 leading and trailing bytes
                    sysexBytes.removeLast()
                    sysexBytes.removeFirst()

                    var hexValueRange:HexValueRange?

                    for subValue in sysexCommand.substitutableValues {
                        if subValue.valueType == .midiChannel {
                            sysexBytes[subValue.index - 1] = "$C"
                        } else if subValue.valueType == .dataValue {
                            sysexBytes[subValue.index - 1] = "$V"
                            hexValueRange = subValue.valueRange
                        }
                    }

                    let substitutedMessage = sysexBytes.joined(separator: " ")

                    if let hexRange = hexValueRange, let min = UInt32(hexRange.min, radix: 16), let max = UInt32(hexRange.max, radix: 16) {
                        destinationMappings[sysexCommand.name] = DestinationParameter(type: .sysex, channel: 0, value: substitutedMessage, allowedParameterValueRange: ParameterValueRange(min: Int(min), max: Int(max)))
                    }
                }
            }
        } catch {
            print("Error occurred while processing open-midi-rtc-schema spec file: \(error.localizedDescription), url: \(url.absoluteString)")
        }
    }
}
