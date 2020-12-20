package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The MSB value range
 */
public class RpnCommandValueRangeMSB {
    private IndecentDiscreteCCValue[] discreteValues;
    private long max;
    private long min;

    /**
     * A list of special one-off values outside of the normal value range
     */
    @JsonProperty("discreteValues")
    public IndecentDiscreteCCValue[] getDiscreteValues() { return discreteValues; }
    @JsonProperty("discreteValues")
    public void setDiscreteValues(IndecentDiscreteCCValue[] value) { this.discreteValues = value; }

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
