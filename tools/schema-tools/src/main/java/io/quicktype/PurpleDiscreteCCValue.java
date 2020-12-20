package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * A CC name/value pair
 */
public class PurpleDiscreteCCValue {
    private String name;
    private long value;

    /**
     * CC value name
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * CC value
     */
    @JsonProperty("value")
    public long getValue() { return value; }
    @JsonProperty("value")
    public void setValue(long value) { this.value = value; }
}
