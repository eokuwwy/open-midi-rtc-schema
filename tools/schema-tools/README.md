# Schema-Tools

This is a gradle project with useful tools related to the open-midi-rtc-schema, such as scripts for generating a [spec](https://github.com/eokuwwy/open-midi-rtc-specs) document from a CSV or Squarp input file.

This project uses the Gradle Wrapper, so there is no need to install Gradle ahead of time.

Simply type:
`
./gradlew build
`
and all dependencies will be downloaded.

### Existing Scripts

 - Schema Doc generation from a CSV file
 
   You can create an open-midi-rtc-schema doc from a simple CSV file using the `generateFromSimpleCsv` (CC only) or `generateFromCsv` (CC, Sysex, and NRPN) gradle task. The CSV file should follow the following column format:
   
   `cc_number,parameter_name,min_value,max_value`
   
   Min and max value will be defaulted to 0 and 127 if left blank.
   
   Example Usage:
   
   ```
   ./gradlew generateFromSimpleCsv --args='examples/csv/ju-06a.csv ju-06a'
   ```
   
   ```
   ./gradlew generateFromCsv --args='examples/csv/midi-machine.csv midi-machine DRUM_MACHINE Eokuwwy VaporDrumPhantom 00017F eokuwwy.com'
   ```
   
   The generated files will appear in the `outputs` directory.
   <br><br>
- Schema Doc generation from a [Squarp definition](https://squarp.community/t/six-trak-cc-definitions/850) file

   You can create an open-midi-rtc-schema doc from a Squarp definition using the `generateFromSquarp` gradle task.
   
   Example Usage:
   
   ```
   ./gradlew generateFromSquarp --args='examples/squarp/six-trak.squarp six-trak MULTI'
   ```
   
   The generated files will appear in the `outputs` directory.
   <br><br>
   
   The generation tasks above take the following arguments (in the order specified):
   
   ```
   Input filename (required)
   Output name (required)
   Device type (defaults to SYNTHESIZER)
   Manufacturer name (optional)
   Display name (optional)
   Manufacturer ID (optional)
   URL (optional)
   ```