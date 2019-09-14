package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * Control Change command specification
 */
public class ControlChange {
    private String additionalInfo;
    private long controlChangeNumber;
    private String description;
    private String name;
    private CCValueRange valueRange;

    /**
     * Additional info that may be useful
     */
    @JsonProperty("additionalInfo")
    public String getAdditionalInfo() { return additionalInfo; }
    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(String value) { this.additionalInfo = value; }

    /**
     * The control change number
     */
    @JsonProperty("controlChangeNumber")
    public long getControlChangeNumber() { return controlChangeNumber; }
    @JsonProperty("controlChangeNumber")
    public void setControlChangeNumber(long value) { this.controlChangeNumber = value; }

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
     * The CC value range
     */
    @JsonProperty("valueRange")
    public CCValueRange getValueRange() { return valueRange; }
    @JsonProperty("valueRange")
    public void setValueRange(CCValueRange value) { this.valueRange = value; }
}
