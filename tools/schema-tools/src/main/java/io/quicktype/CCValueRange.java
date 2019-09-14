package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The CC value range
 */
public class CCValueRange {
    private DiscreteCCValue[] discreteValues;
    private long max;
    private long min;

    /**
     * A list of special one-off values outside of the normal value range
     */
    @JsonProperty("discreteValues")
    public DiscreteCCValue[] getDiscreteValues() { return discreteValues; }
    @JsonProperty("discreteValues")
    public void setDiscreteValues(DiscreteCCValue[] value) { this.discreteValues = value; }

    /**
     * Maximum value
     */
    @JsonProperty("max")
    public long getMax() { return max; }
    @JsonProperty("max")
    public void setMax(long value) { this.max = value; }

    /**
     * Minimum value
     */
    @JsonProperty("min")
    public long getMin() { return min; }
    @JsonProperty("min")
    public void setMin(long value) { this.min = value; }
}
