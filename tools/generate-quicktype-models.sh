QUICKTYPE_DIR=quicktype
mkdir $QUICKTYPE_DIR
rm -rf $QUICKTYPE_DIR/*
mkdir $QUICKTYPE_DIR/java
mkdir $QUICKTYPE_DIR/swift
mkdir $QUICKTYPE_DIR/js

quicktype -s schema ../schema/open-midi-rtc-schema.json -o $QUICKTYPE_DIR/java/OpenMIDIRealtimeSpecification.java
quicktype -s schema ../schema/open-midi-rtc-schema.json -o $QUICKTYPE_DIR/swift/OpenMIDIRealtimeSpecification.swift
quicktype -s schema ../schema/open-midi-rtc-schema.json -o $QUICKTYPE_DIR/js/OpenMIDIRealtimeSpecification.js
