package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The device for the specification
 */
public class Device {
    private String description;
    private DeviceType deviceType;
    private String displayName;
    private String documentationResource;
    private String identifier;
    private String manufacturer;
    private String model;
    private String name;
    private DeviceSystemExclusiveConfig sysexMetadata;
    private String version;

    /**
     * Description of the device
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * Type of device
     */
    @JsonProperty("deviceType")
    public DeviceType getDeviceType() { return deviceType; }
    @JsonProperty("deviceType")
    public void setDeviceType(DeviceType value) { this.deviceType = value; }

    /**
     * The name used for MIDI device discovery
     */
    @JsonProperty("displayName")
    public String getDisplayName() { return displayName; }
    @JsonProperty("displayName")
    public void setDisplayName(String value) { this.displayName = value; }

    /**
     * Link to external documentation
     */
    @JsonProperty("documentationResource")
    public String getDocumentationResource() { return documentationResource; }
    @JsonProperty("documentationResource")
    public void setDocumentationResource(String value) { this.documentationResource = value; }

    /**
     * an alternate identifier
     */
    @JsonProperty("identifier")
    public String getIdentifier() { return identifier; }
    @JsonProperty("identifier")
    public void setIdentifier(String value) { this.identifier = value; }

    /**
     * Name of the manufacturer
     */
    @JsonProperty("manufacturer")
    public String getManufacturer() { return manufacturer; }
    @JsonProperty("manufacturer")
    public void setManufacturer(String value) { this.manufacturer = value; }

    /**
     * Model name or number
     */
    @JsonProperty("model")
    public String getModel() { return model; }
    @JsonProperty("model")
    public void setModel(String value) { this.model = value; }

    /**
     * Internal naming reference
     */
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    /**
     * Overarching summary of sysex command structure
     */
    @JsonProperty("sysexMetadata")
    public DeviceSystemExclusiveConfig getSysexMetadata() { return sysexMetadata; }
    @JsonProperty("sysexMetadata")
    public void setSysexMetadata(DeviceSystemExclusiveConfig value) { this.sysexMetadata = value; }

    /**
     * The firmware or software version
     */
    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }
}
