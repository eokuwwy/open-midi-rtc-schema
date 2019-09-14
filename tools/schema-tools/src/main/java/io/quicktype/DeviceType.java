package io.quicktype;

import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

/**
 * Type of device
 */
public enum DeviceType {
    DAW, DJ, DRUM_MACHINE, EFFECTS, LIGHTS, MULTI, OTHER, SYNTHESIZER;

    @JsonValue
    public String toValue() {
        switch (this) {
        case DAW: return "DAW";
        case DJ: return "DJ";
        case DRUM_MACHINE: return "DRUM_MACHINE";
        case EFFECTS: return "EFFECTS";
        case LIGHTS: return "LIGHTS";
        case MULTI: return "MULTI";
        case OTHER: return "OTHER";
        case SYNTHESIZER: return "SYNTHESIZER";
        }
        return null;
    }

    @JsonCreator
    public static DeviceType forValue(String value) throws IOException {
        if (value.equals("DAW")) return DAW;
        if (value.equals("DJ")) return DJ;
        if (value.equals("DRUM_MACHINE")) return DRUM_MACHINE;
        if (value.equals("EFFECTS")) return EFFECTS;
        if (value.equals("LIGHTS")) return LIGHTS;
        if (value.equals("MULTI")) return MULTI;
        if (value.equals("OTHER")) return OTHER;
        if (value.equals("SYNTHESIZER")) return SYNTHESIZER;
        throw new IOException("Cannot deserialize DeviceType");
    }
}
