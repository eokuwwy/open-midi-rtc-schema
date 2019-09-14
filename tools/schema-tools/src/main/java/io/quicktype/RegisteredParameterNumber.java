package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The specification for RPN commands
 */
public class RegisteredParameterNumber {
    private String additionalInfo;
    private String description;
    private String name;
    private long parameterNumber;
    private RpnCommandValueRange valueRange;

    /**
     * Additional info that may be useful
     */
    @JsonProperty("additionalInfo")
    public String getAdditionalInfo() { return additionalInfo; }
    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(String value) { this.additionalInfo = value; }

    /**
     * Description of the control change
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * The name of the function of the control change
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * The RPN parameter number
     */
    @JsonProperty("parameterNumber")
    public long getParameterNumber() { return parameterNumber; }
    @JsonProperty("parameterNumber")
    public void setParameterNumber(long value) { this.parameterNumber = value; }

    /**
     * The 14-bit value range
     */
    @JsonProperty("valueRange")
    public RpnCommandValueRange getValueRange() { return valueRange; }
    @JsonProperty("valueRange")
    public void setValueRange(RpnCommandValueRange value) { this.valueRange = value; }
}
