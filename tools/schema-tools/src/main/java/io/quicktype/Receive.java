package io.quicktype;

import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Receive {
    CHANNEL_PRESSURE, CLOCK, NOTE_NUMBER, PITCH_BEND, POLY_PRESSURE, PROGRAM_CHANGE, VELOCITY_NOTE_OFF, VELOCITY_NOTE_ON;

    @JsonValue
    public String toValue() {
        switch (this) {
        case CHANNEL_PRESSURE: return "CHANNEL_PRESSURE";
        case CLOCK: return "CLOCK";
        case NOTE_NUMBER: return "NOTE_NUMBER";
        case PITCH_BEND: return "PITCH_BEND";
        case POLY_PRESSURE: return "POLY_PRESSURE";
        case PROGRAM_CHANGE: return "PROGRAM_CHANGE";
        case VELOCITY_NOTE_OFF: return "VELOCITY_NOTE_OFF";
        case VELOCITY_NOTE_ON: return "VELOCITY_NOTE_ON";
        }
        return null;
    }

    @JsonCreator
    public static Receive forValue(String value) throws IOException {
        if (value.equals("CHANNEL_PRESSURE")) return CHANNEL_PRESSURE;
        if (value.equals("CLOCK")) return CLOCK;
        if (value.equals("NOTE_NUMBER")) return NOTE_NUMBER;
        if (value.equals("PITCH_BEND")) return PITCH_BEND;
        if (value.equals("POLY_PRESSURE")) return POLY_PRESSURE;
        if (value.equals("PROGRAM_CHANGE")) return PROGRAM_CHANGE;
        if (value.equals("VELOCITY_NOTE_OFF")) return VELOCITY_NOTE_OFF;
        if (value.equals("VELOCITY_NOTE_ON")) return VELOCITY_NOTE_ON;
        throw new IOException("Cannot deserialize Receive");
    }
}
