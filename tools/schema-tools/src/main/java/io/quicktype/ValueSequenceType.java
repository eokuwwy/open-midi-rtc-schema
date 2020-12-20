package io.quicktype;

import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

/**
 * The MSB LSB value sequence type
 */
public enum ValueSequenceType {
    MSB_LSB, MSB_ONLY, MSB_UNTIL_OVERFLOW;

    @JsonValue
    public String toValue() {
        switch (this) {
        case MSB_LSB: return "MSB_LSB";
        case MSB_ONLY: return "MSB_ONLY";
        case MSB_UNTIL_OVERFLOW: return "MSB_UNTIL_OVERFLOW";
        }
        return null;
    }

    @JsonCreator
    public static ValueSequenceType forValue(String value) throws IOException {
        if (value.equals("MSB_LSB")) return MSB_LSB;
        if (value.equals("MSB_ONLY")) return MSB_ONLY;
        if (value.equals("MSB_UNTIL_OVERFLOW")) return MSB_UNTIL_OVERFLOW;
        throw new IOException("Cannot deserialize ValueSequenceType");
    }
}
