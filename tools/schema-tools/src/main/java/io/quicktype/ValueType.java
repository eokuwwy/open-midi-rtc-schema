package io.quicktype;

import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

/**
 * Defines the type of value, which is typically DATA_VALUE
 */
public enum ValueType {
    DATA_VALUE, MIDI_CHANNEL;

    @JsonValue
    public String toValue() {
        switch (this) {
        case DATA_VALUE: return "DATA_VALUE";
        case MIDI_CHANNEL: return "MIDI_CHANNEL";
        }
        return null;
    }

    @JsonCreator
    public static ValueType forValue(String value) throws IOException {
        if (value.equals("DATA_VALUE")) return DATA_VALUE;
        if (value.equals("MIDI_CHANNEL")) return MIDI_CHANNEL;
        throw new IOException("Cannot deserialize ValueType");
    }
}
