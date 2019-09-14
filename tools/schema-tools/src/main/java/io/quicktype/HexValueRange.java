package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * Defines multiple min, max, and special data values for this sysex value
 */
public class HexValueRange {
    private DiscreteHexValue[] discreteValues;
    private String max;
    private String min;

    /**
     * A list of one-off hex values outside the normal range
     */
    @JsonProperty("discreteValues")
    public DiscreteHexValue[] getDiscreteValues() { return discreteValues; }
    @JsonProperty("discreteValues")
    public void setDiscreteValues(DiscreteHexValue[] value) { this.discreteValues = value; }

    /**
     * Max value in hex format
     */
    @JsonProperty("max")
    public String getMax() { return max; }
    @JsonProperty("max")
    public void setMax(String value) { this.max = value; }

    /**
     * Min value in hex format
     */
    @JsonProperty("min")
    public String getMin() { return min; }
    @JsonProperty("min")
    public void setMin(String value) { this.min = value; }
}
