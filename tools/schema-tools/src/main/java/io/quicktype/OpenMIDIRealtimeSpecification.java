package io.quicktype;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

/**
 * The Open MIDI Realtime Schema
 */
public class OpenMIDIRealtimeSpecification {
    private ControlChange[] controlChangeCommands;
    private ControlChangeSequence[] controlChangeSequenceCommands;
    private String description;
    private Device device;
    private String displayName;
    private String implementationVersion;
    private NonRegisteredParameterNumber[] nrpnCommands;
    private Receive[] receives;
    private RegisteredParameterNumber[] rpnCommands;
    private String schemaVersion;
    private SystemExclusive[] sysexCommands;
    private String title;
    private Receive[] transmits;

    /**
     * The available control change commands for the specification
     */
    @JsonProperty("controlChangeCommands")
    public ControlChange[] getControlChangeCommands() { return controlChangeCommands; }
    @JsonProperty("controlChangeCommands")
    public void setControlChangeCommands(ControlChange[] value) { this.controlChangeCommands = value; }

    /**
     * The available control change sequence commands for the specification
     */
    @JsonProperty("controlChangeSequenceCommands")
    public ControlChangeSequence[] getControlChangeSequenceCommands() { return controlChangeSequenceCommands; }
    @JsonProperty("controlChangeSequenceCommands")
    public void setControlChangeSequenceCommands(ControlChangeSequence[] value) { this.controlChangeSequenceCommands = value; }

    /**
     * A description of the specification
     */
    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    /**
     * The device for the specification
     */
    @JsonProperty("device")
    public Device getDevice() { return device; }
    @JsonProperty("device")
    public void setDevice(Device value) { this.device = value; }

    /**
     * The display name of the specification, typically used for search results
     */
    @JsonProperty("displayName")
    public String getDisplayName() { return displayName; }
    @JsonProperty("displayName")
    public void setDisplayName(String value) { this.displayName = value; }

    /**
     * The version of this specification
     */
    @JsonProperty("implementationVersion")
    public String getImplementationVersion() { return implementationVersion; }
    @JsonProperty("implementationVersion")
    public void setImplementationVersion(String value) { this.implementationVersion = value; }

    /**
     * The available NRPN commands for the specification
     */
    @JsonProperty("nrpnCommands")
    public NonRegisteredParameterNumber[] getNrpnCommands() { return nrpnCommands; }
    @JsonProperty("nrpnCommands")
    public void setNrpnCommands(NonRegisteredParameterNumber[] value) { this.nrpnCommands = value; }

    /**
     * The command types that can be received
     */
    @JsonProperty("receives")
    public Receive[] getReceives() { return receives; }
    @JsonProperty("receives")
    public void setReceives(Receive[] value) { this.receives = value; }

    /**
     * The available RPN commands for the specification
     */
    @JsonProperty("rpnCommands")
    public RegisteredParameterNumber[] getRpnCommands() { return rpnCommands; }
    @JsonProperty("rpnCommands")
    public void setRpnCommands(RegisteredParameterNumber[] value) { this.rpnCommands = value; }

    /**
     * The version of the schema; should match the version specified in the id attribute
     */
    @JsonProperty("schemaVersion")
    public String getSchemaVersion() { return schemaVersion; }
    @JsonProperty("schemaVersion")
    public void setSchemaVersion(String value) { this.schemaVersion = value; }

    /**
     * The available sysex commands for the specification
     */
    @JsonProperty("sysexCommands")
    public SystemExclusive[] getSysexCommands() { return sysexCommands; }
    @JsonProperty("sysexCommands")
    public void setSysexCommands(SystemExclusive[] value) { this.sysexCommands = value; }

    /**
     * The title of the specification
     */
    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    /**
     * The command types that can be transmitted
     */
    @JsonProperty("transmits")
    public Receive[] getTransmits() { return transmits; }
    @JsonProperty("transmits")
    public void setTransmits(Receive[] value) { this.transmits = value; }
}
