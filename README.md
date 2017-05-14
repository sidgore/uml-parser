[![Stories in Ready](https://badge.waffle.io/sidgore/uml-parser.png?label=ready&title=Ready)](https://waffle.io/sidgore/uml-parser)
# JAVA-UML Parser
 
 Java UML Parser-
 A UML parser which converts the given JAVA Source code into Class and Sequence Diagrams.
 
 Instructions for Generating Class Diagram-
 Requirements:
 Java JDK version 1.8
 Working internet connection (only for class diagram)
 plantUml.jar
 javaparser.jar
 The program expects following arguments:
 Keyword:
 “class” keyword is used to generate class Diagram while “sequence” keyword is used to generate Sequence Diagram.
 Path:
 The path of the folder which contains the files with .java extension.
 Example “/Users/sidgore/Desktop/workspace/uml-parser/Test-Cases/uml-parser-test-1”
 
 Output file
 The name of output file without any extension.
 Here we are creating all the diagrams as .png files.
 Example – diagram
 Example:- To generate class diagram
 java -jar sid-umlparser.jar class /Users/sidgore/Downloads/downloads/paul-202-github/umlparser/uml-parser-test-4 diagram
 
 
 Now for Sequence Diagram-
 Apart from the above two parameters following parameters are required.
 Class Name
 The name of Class from which the execution starts.
 Example: Main
 Method Name
 The name of method which is required to create Sequence Diagram.
 Ex - main
 To generate sequence diagram
 java -jar sid-umlparser.jar sequence /Users/sidgore/Downloads/downloads/paul-202-github/umlparser/uml-sequence-test Main main sequenceDiagram
 
 
 
 
 Details of libraries and tools used
 There are 2 parts of this UML parser program:
 JavaToString.java – Converts Source code into  AST grammer which is understood by yUML.
 StringToUML.java– The code generated is converted into a Class Diagram using yUML.
 Javaparser.jar ::https://github.com/javaparser/javaparser
 This is an open Source library which is used to parse the source code into a code understood by yUML.
 
 yUML:https://yuml.me/diagram/scruffy/class/draw
 yUML is an online tool for creating and publishing simple UML diagrams.An Internet Connection is required to access this.
 For generating the sequence diagram, plantUML is integrated in the code: http://plantuml.com/
