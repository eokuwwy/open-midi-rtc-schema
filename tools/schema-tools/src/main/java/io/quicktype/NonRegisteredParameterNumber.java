package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The specification for NRPN commands
 */
public class NonRegisteredParameterNumber {
    private String additionalInfo;
    private String description;
    private String name;
    private long parameterNumber;
    private NrpnCommandValueRange valueRange;

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
     * The NRPN parameter number
     */
    @JsonProperty("parameterNumber")
    public long getParameterNumber() { return parameterNumber; }
    @JsonProperty("parameterNumber")
    public void setParameterNumber(long value) { this.parameterNumber = value; }

    /**
     * The 14-bit value range
     */
    @JsonProperty("valueRange")
    public NrpnCommandValueRange getValueRange() { return valueRange; }
    @JsonProperty("valueRange")
    public void setValueRange(NrpnCommandValueRange value) { this.valueRange = value; }
}
