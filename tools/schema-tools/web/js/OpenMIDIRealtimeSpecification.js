// To parse this data:
//
//   const Convert = require("./file");
//
//   const openMIDIRealtimeSpecification = Convert.toOpenMIDIRealtimeSpecification(json);
//
// These functions will throw an error if the JSON doesn't
// match the expected interface, even if the JSON is valid.

// Converts JSON strings to/from your types
// and asserts the results of JSON.parse at runtime
function toOpenMIDIRealtimeSpecification(json) {
    return cast(JSON.parse(json), r("OpenMIDIRealtimeSpecification"));
}

function openMIDIRealtimeSpecificationToJson(value) {
    return JSON.stringify(uncast(value, r("OpenMIDIRealtimeSpecification")), null, 2);
}

function invalidValue(typ, val) {
    throw Error(`Invalid value ${JSON.stringify(val)} for type ${JSON.stringify(typ)}`);
}

function jsonToJSProps(typ) {
    if (typ.jsonToJS === undefined) {
        var map = {};
        typ.props.forEach((p) => map[p.json] = { key: p.js, typ: p.typ });
        typ.jsonToJS = map;
    }
    return typ.jsonToJS;
}

function jsToJSONProps(typ) {
    if (typ.jsToJSON === undefined) {
        var map = {};
        typ.props.forEach((p) => map[p.js] = { key: p.json, typ: p.typ });
        typ.jsToJSON = map;
    }
    return typ.jsToJSON;
}

function transform(val, typ, getProps) {
    function transformPrimitive(typ, val) {
        if (typeof typ === typeof val) return val;
        return invalidValue(typ, val);
    }

    function transformUnion(typs, val) {
        // val must validate against one typ in typs
        var l = typs.length;
        for (var i = 0; i < l; i++) {
            var typ = typs[i];
            try {
                return transform(val, typ, getProps);
            } catch (_) {}
        }
        return invalidValue(typs, val);
    }

    function transformEnum(cases, val) {
        if (cases.indexOf(val) !== -1) return val;
        return invalidValue(cases, val);
    }

    function transformArray(typ, val) {
        // val must be an array with no invalid elements
        if (!Array.isArray(val)) return invalidValue("array", val);
        return val.map(el => transform(el, typ, getProps));
    }

    function transformObject(props, additional, val) {
        if (val === null || typeof val !== "object" || Array.isArray(val)) {
            return invalidValue("object", val);
        }
        var result = {};
        Object.getOwnPropertyNames(props).forEach(key => {
            const prop = props[key];
            const v = Object.prototype.hasOwnProperty.call(val, key) ? val[key] : undefined;
            result[prop.key] = transform(v, prop.typ, getProps);
        });
        Object.getOwnPropertyNames(val).forEach(key => {
            if (!Object.prototype.hasOwnProperty.call(props, key)) {
                result[key] = transform(val[key], additional, getProps);
            }
        });
        return result;
    }

    if (typ === "any") return val;
    if (typ === null) {
        if (val === null) return val;
        return invalidValue(typ, val);
    }
    if (typ === false) return invalidValue(typ, val);
    while (typeof typ === "object" && typ.ref !== undefined) {
        typ = typeMap[typ.ref];
    }
    if (Array.isArray(typ)) return transformEnum(typ, val);
    if (typeof typ === "object") {
        return typ.hasOwnProperty("unionMembers") ? transformUnion(typ.unionMembers, val)
            : typ.hasOwnProperty("arrayItems")    ? transformArray(typ.arrayItems, val)
            : typ.hasOwnProperty("props")         ? transformObject(getProps(typ), typ.additional, val)
            : invalidValue(typ, val);
    }
    return transformPrimitive(typ, val);
}

function cast(val, typ) {
    return transform(val, typ, jsonToJSProps);
}

function uncast(val, typ) {
    return transform(val, typ, jsToJSONProps);
}

function a(typ) {
    return { arrayItems: typ };
}

function u(...typs) {
    return { unionMembers: typs };
}

function o(props, additional) {
    return { props, additional };
}

function m(additional) {
    return { props: [], additional };
}

function r(name) {
    return { ref: name };
}

const typeMap = {
    "OpenMIDIRealtimeSpecification": o([
        { json: "controlChangeCommands", js: "controlChangeCommands", typ: u(undefined, a(r("ControlChange"))) },
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "device", js: "device", typ: r("Device") },
        { json: "displayName", js: "displayName", typ: "" },
        { json: "implementationVersion", js: "implementationVersion", typ: "" },
        { json: "nrpnCommands", js: "nrpnCommands", typ: u(undefined, a(r("NonRegisteredParameterNumber"))) },
        { json: "receives", js: "receives", typ: a(r("Receive")) },
        { json: "rpnCommands", js: "rpnCommands", typ: u(undefined, a(r("RegisteredParameterNumber"))) },
        { json: "schemaVersion", js: "schemaVersion", typ: "" },
        { json: "sysexCommands", js: "sysexCommands", typ: u(undefined, a(r("SystemExclusive"))) },
        { json: "title", js: "title", typ: "" },
        { json: "transmits", js: "transmits", typ: a(r("Receive")) },
    ], "any"),
    "ControlChange": o([
        { json: "additionalInfo", js: "additionalInfo", typ: u(undefined, "") },
        { json: "controlChangeNumber", js: "controlChangeNumber", typ: 0 },
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "name", js: "name", typ: "" },
        { json: "valueRange", js: "valueRange", typ: r("CCValueRange") },
    ], "any"),
    "CCValueRange": o([
        { json: "discreteValues", js: "discreteValues", typ: u(undefined, a(r("DiscreteCCValue"))) },
        { json: "max", js: "max", typ: 0 },
        { json: "min", js: "min", typ: 0 },
    ], "any"),
    "DiscreteCCValue": o([
        { json: "name", js: "name", typ: "" },
        { json: "value", js: "value", typ: 0 },
    ], "any"),
    "Device": o([
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "deviceType", js: "deviceType", typ: u(undefined, r("DeviceType")) },
        { json: "displayName", js: "displayName", typ: "" },
        { json: "documentationResource", js: "documentationResource", typ: u(undefined, "") },
        { json: "identifier", js: "identifier", typ: u(undefined, "") },
        { json: "manufacturer", js: "manufacturer", typ: u(undefined, "") },
        { json: "model", js: "model", typ: u(undefined, "") },
        { json: "name", js: "name", typ: u(undefined, "") },
        { json: "sysexMetadata", js: "sysexMetadata", typ: u(undefined, r("DeviceSystemExclusiveConfig")) },
        { json: "version", js: "version", typ: u(undefined, "") },
    ], "any"),
    "DeviceSystemExclusiveConfig": o([
        { json: "bitDescriptions", js: "bitDescriptions", typ: u(undefined, a("")) },
        { json: "deviceIdentifier", js: "deviceIdentifier", typ: u(undefined, "") },
        { json: "exampleDescription", js: "exampleDescription", typ: u(undefined, "") },
        { json: "exampleMessage", js: "exampleMessage", typ: u(undefined, "") },
        { json: "sysexEnd", js: "sysexEnd", typ: u(undefined, "") },
        { json: "sysexStart", js: "sysexStart", typ: u(undefined, "") },
    ], "any"),
    "NonRegisteredParameterNumber": o([
        { json: "additionalInfo", js: "additionalInfo", typ: u(undefined, "") },
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "name", js: "name", typ: "" },
        { json: "parameterNumber", js: "parameterNumber", typ: 0 },
        { json: "valueRange", js: "valueRange", typ: r("NrpnCommandValueRange") },
    ], "any"),
    "NrpnCommandValueRange": o([
        { json: "discreteValues", js: "discreteValues", typ: u(undefined, a(r("PurpleDiscreteFourteenBitValue"))) },
        { json: "max", js: "max", typ: 0 },
        { json: "min", js: "min", typ: 0 },
    ], "any"),
    "PurpleDiscreteFourteenBitValue": o([
        { json: "name", js: "name", typ: "" },
        { json: "value", js: "value", typ: 0 },
    ], "any"),
    "RegisteredParameterNumber": o([
        { json: "additionalInfo", js: "additionalInfo", typ: u(undefined, "") },
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "name", js: "name", typ: "" },
        { json: "parameterNumber", js: "parameterNumber", typ: 0 },
        { json: "valueRange", js: "valueRange", typ: r("RpnCommandValueRange") },
    ], "any"),
    "RpnCommandValueRange": o([
        { json: "discreteValues", js: "discreteValues", typ: u(undefined, a(r("FluffyDiscreteFourteenBitValue"))) },
        { json: "max", js: "max", typ: 0 },
        { json: "min", js: "min", typ: 0 },
    ], "any"),
    "FluffyDiscreteFourteenBitValue": o([
        { json: "name", js: "name", typ: "" },
        { json: "value", js: "value", typ: 0 },
    ], "any"),
    "SystemExclusive": o([
        { json: "additionalInfo", js: "additionalInfo", typ: u(undefined, "") },
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "name", js: "name", typ: "" },
        { json: "substitutableMessage", js: "substitutableMessage", typ: "" },
        { json: "substitutableValues", js: "substitutableValues", typ: a(r("SysexValue")) },
    ], "any"),
    "SysexValue": o([
        { json: "description", js: "description", typ: u(undefined, "") },
        { json: "index", js: "index", typ: 0 },
        { json: "name", js: "name", typ: u(undefined, "") },
        { json: "substituteFor", js: "substituteFor", typ: "" },
        { json: "valueRange", js: "valueRange", typ: r("HexValueRange") },
        { json: "valueType", js: "valueType", typ: r("ValueType") },
    ], "any"),
    "HexValueRange": o([
        { json: "discreteValues", js: "discreteValues", typ: u(undefined, a(r("DiscreteHexValue"))) },
        { json: "max", js: "max", typ: "" },
        { json: "min", js: "min", typ: "" },
    ], "any"),
    "DiscreteHexValue": o([
        { json: "name", js: "name", typ: "" },
        { json: "value", js: "value", typ: "" },
    ], "any"),
    "DeviceType": [
        "DAW",
        "DJ",
        "DRUM_MACHINE",
        "EFFECTS",
        "LIGHTS",
        "MULTI",
        "OTHER",
        "SYNTHESIZER",
    ],
    "Receive": [
        "CHANNEL_PRESSURE",
        "CLOCK",
        "NOTE_NUMBER",
        "PITCH_BEND",
        "POLY_PRESSURE",
        "PROGRAM_CHANGE",
        "VELOCITY_NOTE_OFF",
        "VELOCITY_NOTE_ON",
    ],
    "ValueType": [
        "DATA_VALUE",
        "MIDI_CHANNEL",
    ],
};

module.exports = {
    "openMIDIRealtimeSpecificationToJson": openMIDIRealtimeSpecificationToJson,
    "toOpenMIDIRealtimeSpecification": toOpenMIDIRealtimeSpecification,
};
