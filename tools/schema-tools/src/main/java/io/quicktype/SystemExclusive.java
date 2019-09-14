package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The specification for system exclusive commands
 */
public class SystemExclusive {
    private String additionalInfo;
    private String description;
    private String name;
    private String substitutableMessage;
    private SysexValue[] substitutableValues;

    /**
     * Any additional info that may be useful
     */
    @JsonProperty("additionalInfo")
    public String getAdditionalInfo() { return additionalInfo; }
    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(String value) { this.additionalInfo = value; }

    /**
     * A description of the sysex command
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * The name of the function which this sysex command performs
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * The full system exclusive message for this command, using designations such as dd as a
     * substitute for any data value bits; for example: F0 43 10 7F 1C 03 00 00 01 dd F7, or F0
     * 43 10 7F 1C 03 00 00 11 dd dd F7
     */
    @JsonProperty("substitutableMessage")
    public String getSubstitutableMessage() { return substitutableMessage; }
    @JsonProperty("substitutableMessage")
    public void setSubstitutableMessage(String value) { this.substitutableMessage = value; }

    /**
     * A list that describes the values to be used within a substitutableMessage
     */
    @JsonProperty("substitutableValues")
    public SysexValue[] getSubstitutableValues() { return substitutableValues; }
    @JsonProperty("substitutableValues")
    public void setSubstitutableValues(SysexValue[] value) { this.substitutableValues = value; }
}
