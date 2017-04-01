# IFC-RDF-Convertor_Validator
This tool is to compare original IFC files with the roundtrip IFC files that have gone through IFC-RDF conversion process and back.
It proves that ifcOWL ontology is valid, and the conversion pattern between IFC STEP and RDF is also valid.
The comparison program is to firstly convert IFC STEP files to IfcOWL RDF files and the convert them back. Original IFC files are in folder "resources/IFC_original", and roundtrip IFC files are in folder "resources/IFC_roundtrip". Files are processed using chechsum MD5 and SHA-1. Result is in the file "compare_result.xls". Some conditions here:
1. As ifcOWL does not cover IFC HEADER data, IFC HEADER data is removed in comparison process. Comparison between the original IFC files and round-trip IFC files are only for the DATA part. 
2. All the comments, blank lines and spaces that do not affect contents are removed. 
