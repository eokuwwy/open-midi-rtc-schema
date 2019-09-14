package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The 14-bit value range
 */
public class RpnCommandValueRange {
    private FluffyDiscreteFourteenBitValue[] discreteValues;
    private long max;
    private long min;

    /**
     * A list of special one-off values outside of the normal value range
     */
    @JsonProperty("discreteValues")
    public FluffyDiscreteFourteenBitValue[] getDiscreteValues() { return discreteValues; }
    @JsonProperty("discreteValues")
    public void setDiscreteValues(FluffyDiscreteFourteenBitValue[] value) { this.discreteValues = value; }

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
