# Open MIDI Realtime Schema
The Open MIDI Realtime Schema is an open-source [JSON schema](https://json-schema.org/) for defining and describing MIDI parameters for apps, software, and devices that can be controlled in real-time.

For manufactuers and developers, creating documents that adhere to the schema provides the ability to:

- Generate documentation
- Generate code stubs
- Test implementations against the schema
- Integrate with other apps/software/devices that have published a document that conforms to the schema

Since this is a JSON Schema, there are tools available that can validate a document and ensure that it conforms to the schema. In addition, there are also tools that can generate code stubs, which can be used to parse documents that adhere to the schema. Once such tool is [Quicktype](https://github.com/quicktype/quicktype), which can be installed via `npm`. For convenience, a script for generating data models via Quicktype is available in the `tools` directory of this repo.


### Existing documents
Open source specs that conform to the schema can be found here: [https://github.com/eokuwwy/open-midi-rtc-specs](https://github.com/eokuwwy/open-midi-rtc-specs)   

[See the list](./list.md) of apps, software, and hardware that use the Open MIDI Realtime Schema.