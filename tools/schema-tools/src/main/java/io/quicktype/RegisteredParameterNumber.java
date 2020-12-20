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
    private Long parameterNumber;
    private Long parameterNumberLSB;
    private Long parameterNumberMSB;
    private RpnCommandValueRange valueRange;
    private RpnCommandValueRangeLSB valueRangeLSB;
    private RpnCommandValueRangeMSB valueRangeMSB;
    private ValueSequenceType valueSequenceType;

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
     * The RPN parameter number full decimal value
     */
    @JsonProperty("parameterNumber")
    public Long getParameterNumber() { return parameterNumber; }
    @JsonProperty("parameterNumber")
    public void setParameterNumber(Long value) { this.parameterNumber = value; }

    /**
     * The RPN parameter number LSB
     */
    @JsonProperty("parameterNumberLSB")
    public Long getParameterNumberLSB() { return parameterNumberLSB; }
    @JsonProperty("parameterNumberLSB")
    public void setParameterNumberLSB(Long value) { this.parameterNumberLSB = value; }

    /**
     * The RPN parameter number MSB
     */
    @JsonProperty("parameterNumberMSB")
    public Long getParameterNumberMSB() { return parameterNumberMSB; }
    @JsonProperty("parameterNumberMSB")
    public void setParameterNumberMSB(Long value) { this.parameterNumberMSB = value; }

    /**
     * The 14-bit full decimal value range
     */
    @JsonProperty("valueRange")
    public RpnCommandValueRange getValueRange() { return valueRange; }
    @JsonProperty("valueRange")
    public void setValueRange(RpnCommandValueRange value) { this.valueRange = value; }

    /**
     * The LSB value range
     */
    @JsonProperty("valueRangeLSB")
    public RpnCommandValueRangeLSB getValueRangeLSB() { return valueRangeLSB; }
    @JsonProperty("valueRangeLSB")
    public void setValueRangeLSB(RpnCommandValueRangeLSB value) { this.valueRangeLSB = value; }

    /**
     * The MSB value range
     */
    @JsonProperty("valueRangeMSB")
    public RpnCommandValueRangeMSB getValueRangeMSB() { return valueRangeMSB; }
    @JsonProperty("valueRangeMSB")
    public void setValueRangeMSB(RpnCommandValueRangeMSB value) { this.valueRangeMSB = value; }

    /**
     * The MSB LSB value sequence type
     */
    @JsonProperty("valueSequenceType")
    public ValueSequenceType getValueSequenceType() { return valueSequenceType; }
    @JsonProperty("valueSequenceType")
    public void setValueSequenceType(ValueSequenceType value) { this.valueSequenceType = value; }
}
