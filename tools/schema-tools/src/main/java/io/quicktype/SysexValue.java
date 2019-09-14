package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * Details about a value that can be used within a substitutable sysex message
 */
public class SysexValue {
    private String description;
    private long index;
    private String name;
    private String substituteFor;
    private HexValueRange valueRange;
    private ValueType valueType;

    /**
     * A description of the value
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * The zero-based index of the value within the sysex message
     */
    @JsonProperty("index")
    public long getIndex() { return index; }
    @JsonProperty("index")
    public void setIndex(long value) { this.index = value; }

    /**
     * A name for the value
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * The placeholder value being used in the substitutable sysex message
     */
    @JsonProperty("substituteFor")
    public String getSubstituteFor() { return substituteFor; }
    @JsonProperty("substituteFor")
    public void setSubstituteFor(String value) { this.substituteFor = value; }

    /**
     * Defines multiple min, max, and special data values for this sysex value
     */
    @JsonProperty("valueRange")
    public HexValueRange getValueRange() { return valueRange; }
    @JsonProperty("valueRange")
    public void setValueRange(HexValueRange value) { this.valueRange = value; }

    /**
     * Defines the type of value, which is typically DATA_VALUE
     */
    @JsonProperty("valueType")
    public ValueType getValueType() { return valueType; }
    @JsonProperty("valueType")
    public void setValueType(ValueType value) { this.valueType = value; }
}
