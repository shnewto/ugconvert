# UgConvert

_Scala_ + _Scio_ + _Beam_ + _GCP Dataflow_ pipeline for extracting historical data from the PDF archives of the [Used Gravitrons lit zine](https://usedgravitrons.com/).


### Requirements 

- Google Cloud SDK
- sbt
- Java 8 SDK

When provided an issue of Used Gravitrons, UgConvert will attempt to

- Read PDF Input
- Extract the PDF text
- Split the extracted text into segments for parsing
- Parse the extracted text segments and identify the 'Table of Contents' and the 'Contributor Bios' pages
- Distribute the 'Table of Contents', 'Contributor Bios', and 'Other' their own specified output location
- Send the extracted text of the full issue (after segmenting) to its own specified output location

Some caveats:

- Initial work was done using the `scala-parser-combinators` library to parse the extacted text but that turned out not to be thread safe (or at least, thread predictable ha). So for the time being, there's some special casing being done rather than properly leveraging a parsing library. 
- There's more to do! Classifying the TOC and Bios is a good step forward but I'd like this to present a much richer picture of each issue. 

Usage locally:

```bash
sbt "runMain usedgravitrons.UgConvert \
--input=<PDF_ISSUE_PATH> \
--bios=<BIOS_OUTPUT_DIR> \
--toc=<TOC_OUTPUT_DIR> \
--other=<OTHER_OUTPUT_DIR> \
--complete=<COMPLETE_ISSUE_OUTPUT_DIR>"
```

Usage on GCP:

```bash
sbt "runMain usedgravitrons.UgConvert \
--project=[PROJECT] \
--runner=DataflowRunner \
--region=[REGION] \
--tempLocation=[TEMP_BUCKET] \
--stagingLocation=[STAGING_BUCKET] \
--input=[PDF_ISSUE_INPUT_BUCKET] \
--bios=[BIOS_OUTPUT_BUCKET] \
--toc=[TOC_OUTPUT_BUCKET] \
--other=[OTHER_OUTPUT_BUCKET] \
--complete=[COMPLETE_ISSUE_OUTPUT_BUCKET]"
```

If you have questions, comments, or want to help out please raise an issue :heart:

