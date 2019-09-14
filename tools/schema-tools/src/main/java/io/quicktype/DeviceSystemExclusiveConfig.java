package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * Overarching summary of sysex command structure
 */
public class DeviceSystemExclusiveConfig {
    private String[] bitDescriptions;
    private String deviceIdentifier;
    private String exampleDescription;
    private String exampleMessage;
    private String sysexEnd;
    private String sysexStart;

    /**
     * An ordered list that describes each bit in a system exclusive message; Example: ["sysex
     * start", "device identifier", "parameter type", "prod ID 1", "prod ID 2", "prod ID 3",
     * "address high", "address mid", "address low", "data value", "sysex end"]
     */
    @JsonProperty("bitDescriptions")
    public String[] getBitDescriptions() { return bitDescriptions; }
    @JsonProperty("bitDescriptions")
    public void setBitDescriptions(String[] value) { this.bitDescriptions = value; }

    /**
     * Bit value used for device identification in sysex messages
     */
    @JsonProperty("deviceIdentifier")
    public String getDeviceIdentifier() { return deviceIdentifier; }
    @JsonProperty("deviceIdentifier")
    public void setDeviceIdentifier(String value) { this.deviceIdentifier = value; }

    /**
     * A description of what the working example does
     */
    @JsonProperty("exampleDescription")
    public String getExampleDescription() { return exampleDescription; }
    @JsonProperty("exampleDescription")
    public void setExampleDescription(String value) { this.exampleDescription = value; }

    /**
     * A full working example of a system exclusive message for the device
     */
    @JsonProperty("exampleMessage")
    public String getExampleMessage() { return exampleMessage; }
    @JsonProperty("exampleMessage")
    public void setExampleMessage(String value) { this.exampleMessage = value; }

    /**
     * Last bit in sysex messages, typically F7
     */
    @JsonProperty("sysexEnd")
    public String getSysexEnd() { return sysexEnd; }
    @JsonProperty("sysexEnd")
    public void setSysexEnd(String value) { this.sysexEnd = value; }

    /**
     * First bit in sysex messages, typically F0
     */
    @JsonProperty("sysexStart")
    public String getSysexStart() { return sysexStart; }
    @JsonProperty("sysexStart")
    public void setSysexStart(String value) { this.sysexStart = value; }
}
