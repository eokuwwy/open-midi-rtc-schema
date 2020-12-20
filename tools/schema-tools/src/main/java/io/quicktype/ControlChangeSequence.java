package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * Control Change MSB LSB sequence command specification, typically used to send high res
 * MIDI values via a sequence of MIDI CCs
 */
public class ControlChangeSequence {
    private String additionalInfo;
    private long controlChangeNumberLSB;
    private long controlChangeNumberMSB;
    private String description;
    private String name;
    private ControlChangeSequenceCommandValueRangeLSB valueRangeLSB;
    private ControlChangeSequenceCommandValueRangeMSB valueRangeMSB;
    private ValueSequenceType valueSequenceType;

    /**
     * Additional info that may be useful
     */
    @JsonProperty("additionalInfo")
    public String getAdditionalInfo() { return additionalInfo; }
    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(String value) { this.additionalInfo = value; }

    /**
     * The control change number used for the LSB value
     */
    @JsonProperty("controlChangeNumberLSB")
    public long getControlChangeNumberLSB() { return controlChangeNumberLSB; }
    @JsonProperty("controlChangeNumberLSB")
    public void setControlChangeNumberLSB(long value) { this.controlChangeNumberLSB = value; }

    /**
     * The control change number used for the MSB value
     */
    @JsonProperty("controlChangeNumberMSB")
    public long getControlChangeNumberMSB() { return controlChangeNumberMSB; }
    @JsonProperty("controlChangeNumberMSB")
    public void setControlChangeNumberMSB(long value) { this.controlChangeNumberMSB = value; }

    /**
     * Description of the control change sequence
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * The name of the function of the MSB LSB control change sequence
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * The LSB value range
     */
    @JsonProperty("valueRangeLSB")
    public ControlChangeSequenceCommandValueRangeLSB getValueRangeLSB() { return valueRangeLSB; }
    @JsonProperty("valueRangeLSB")
    public void setValueRangeLSB(ControlChangeSequenceCommandValueRangeLSB value) { this.valueRangeLSB = value; }

    /**
     * The MSB value range
     */
    @JsonProperty("valueRangeMSB")
    public ControlChangeSequenceCommandValueRangeMSB getValueRangeMSB() { return valueRangeMSB; }
    @JsonProperty("valueRangeMSB")
    public void setValueRangeMSB(ControlChangeSequenceCommandValueRangeMSB value) { this.valueRangeMSB = value; }

    /**
     * The MSB LSB value sequence type
     */
    @JsonProperty("valueSequenceType")
    public ValueSequenceType getValueSequenceType() { return valueSequenceType; }
    @JsonProperty("valueSequenceType")
    public void setValueSequenceType(ValueSequenceType value) { this.valueSequenceType = value; }
}
