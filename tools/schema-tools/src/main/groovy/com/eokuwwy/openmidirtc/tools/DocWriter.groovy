package com.eokuwwy.openmidirtc.tools

/**
 * Generates documentation for a spec file
 */
class DocWriter {

    static void main(String[] args) {
        String fileName = args[0]
        String outputFile = args[1]

        MidiTemplateUtils.writeDocumentationFromSpecFile(fileName, outputFile, true)
    }
}
