# Open MIDI Realtime Schema
The Open MIDI Realtime Schema is an open-source [JSON schema](https://json-schema.org/) for defining and describing MIDI parameters for apps, software, and devices that can be controlled in real-time.

For manufactuers and developers, creating documents that adhere to the schema provides the ability to:

- Generate documentation
- Generate code stubs
- Test implementations against the schema
- Integrate with other apps/software/devices that have published a document that conforms to the schema

Since this is a JSON Schema, there are online and offline tools available that can validate a document and ensure that it conforms to the schema, such as [https://www.jsonschemavalidator.net](https://www.jsonschemavalidator.net/). To try it out for yourself, copy and paste the contents of [the schema file](./schema/open-midi-rtc-schema.json) into the text area on the left side of the [validator](https://www.jsonschemavalidator.net/). Then copy and paste the contents of one of the [examples](./examples) into the text area on the right side. The validator should indicate that there are no errors and the document successfully validates against the schema.

In addition, there are also tools that can generate code stubs, which can be used to parse or create documents that adhere to the schema. Once such tool is [Quicktype](https://github.com/quicktype/quicktype), which can be installed via `npm`. For convenience, a script for generating data models via Quicktype is available in the `tools` directory of this repo.

  - [QuicktypeModelSpec.groovy](./examples/QuicktypeModelSpec.groovy) is a JVM-based example that demonstrates how to build a schema document from the Quicktype models, within the context of a [Spock](https://github.com/spockframework/spock) unit test. 
  - [ReadSpecFromQuicktypeModel.swift](./examples/ReadSpecFromQuicktypeModel.swift) demonstrates a way to read and process a schema document in Swift.


### Existing documents
Open source specs that conform to the schema can be found here: [https://github.com/eokuwwy/open-midi-rtc-specs](https://github.com/eokuwwy/open-midi-rtc-specs)   

[See the list](./list.md) of apps, software, and hardware that use the Open MIDI Realtime Schema.