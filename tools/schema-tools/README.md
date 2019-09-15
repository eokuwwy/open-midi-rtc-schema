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
 
   You can create an open-midi-rtc-schema doc from a simple CSV file using the `generateFromSimpleCsv` (CC only) or `generateFromCsv` (CC, Sysex, and NRPN) gradle task. 
   
   A CSV file for the simple script (CC only) should follow the following column format:
   
   `cc number,parameter name,min value,max value`
   
   Sample line:
   
   `1,MODULATION,0,127`
   
   Min and max value will be defaulted to 0 and 127 if left blank.
   
   Example Usage:
   
   
   ```
   ./gradlew generateFromSimpleCsv --args='examples/csv/ju-06a.csv ju-06a'
   ```
   <br>
   The column format for the gradle task that handles multiple parameter types (CC, Sysex, and NRPN) is significantly more complex, and is as follows:
   
   `parameter type,parameter name,cc number/nrpn msb/sysex message,nrpn lsb/sysex sub value 1 type,min value/sysex sub value 1 index,max value/sysex sub value 1 min value,sysex sub value 1 max value,sysex sub value 2 type,sysex sub value 2 index,sysex sub value 2 min value,sysex sub value 2 max value`
   
   - parameter type must be one of: `cc, nrpn, sysex`
   - sysex messages may contain substitutable values, the value type must be one of: `MIDI_CHANNEL, DATA_VALUE`
   - min and max values for CC and NRPN are expressed in decimal notation, sysex min and max are expressed in hexadecimal notation. CC and NRPN min and max are defaulted to (0, 127) and sysex min and max for substitutable parameters are defaulted to (00, 7F) if left blank.
   
   Sample lines:
   
   ```
   cc,Filter Cutoff,74,,0,127,,,,,
 	nrpn,Filter Res,31,1,0,255,,,,,
 	sysex,PWM Depth,F0 41 32 cc 0F dd F7,MIDI_CHANNEL,3,00,0F,DATA_VALUE,5,00,7F
 	sysex,DCO Level,F0 41 32 01 0F dd F7,DATA_VALUE,5,00,7F,,,,
   ```
   
   Example Usage:
   
   ```
   ./gradlew generateFromCsv --args='examples/csv/midi-machine.csv midi-machine DRUM_MACHINE Eokuwwy VaporDrumPhantom 00017F eokuwwy.com'
   ```
   
   <br>
   <em>Further Sysex Explanation</em><br>
   Let's breakdown this line in the above example:
   `sysex,PWM Depth,F0 41 32 cc 0F dd F7,MIDI_CHANNEL,3,00,0F,DATA_VALUE,5,00,7F`
   
   The sysex message `F0 41 32 cc 0F dd F7` has 2 substitutable values, `cc` and `dd`, which are at indexes `3` and `5` respectively. Indexes start at 0. The type of the first value is `MIDI_CHANNEL`, and it has a min and max of `(0,16)`, which is `(00,0F)` in hex. The second value type is `DATA_VALUE`, and its range is `(0,127)`, which is `(00,7F)` in hex. 
   
   Sysex messages used for real-time control should have, at the very least one substitutable value of type `DATA_VALUE`. In the above example `dd` is used as a placeholder for this byte. You may have seen this byte denoted as `vv`, `$V` or `%V` in documentation for certain devices. Whatever designation you choose to use doesn't really matter for generating the spec document. You just need to make sure that the index is correct. Indexes start at 0. In the above example, the index for the data value byte is `5`.
   
   
   <br>
- Schema Doc generation from a [Squarp definition](https://squarp.community/t/six-trak-cc-definitions/850) file

   You can create an open-midi-rtc-schema doc from a Squarp definition using the `generateFromSquarp` gradle task.
   
   Example Usage:
   
   ```
   ./gradlew generateFromSquarp --args='examples/squarp/six-trak.squarp six-trak MULTI'
   ```
   
   <br>
   The generated files will appear in the `outputs` directory.
   <br>
   
   The CSV generation tasks above take the following arguments (in the order specified):
   
   ```
   Input filename (required)
   Output name (required)
   Device type (defaults to SYNTHESIZER)
   Manufacturer name (optional)
   Display name (optional)
   Manufacturer ID (optional)
   URL (optional)
   ```
   <br>
- HTML doc generation from an existing schema doc file 

	You can generate some simple HTML documentation for any spec file with the following gradle task: `generateDocForSpec`. Simply supply the input and output file names.
	
	Usage:
	```
	./gradlew generateDocForSpec --args='output/midi-machine.json output/midi-machine-doc.html'
	```  