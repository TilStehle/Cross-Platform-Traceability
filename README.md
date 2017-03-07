# Cross-Platform-Traceability
This project provides an IntelliJ plugin that integrates J2Swift into the IntelliJ IDEA / Android Studio IDE.
Except from the swift output, a traceability model is created, that lets users navigate directly to swift resources.
If you install the Refactorator App(https://github.com/johnno1962/RefactoratorApp) on OSX, there is also a prototypal action that lets you rename variables in Java and corresponding Swift files.

##Build
Open Project in IntelliJ IDEA (Tested with Community Edition 2016.2.5)
Download antlr jar from https://mvnrepository.com/artifact/org.antlr/antlr4/4.5
Copy optained jar into lib Folder
Rightclick jar ->“Add as Library…“

If you want to deploy the plugin in another instance of IntelliJ IDEA or Android Studio, follow the guidelines provided on https://www.jetbrains.com/help/idea/2016.3/plugin-deployment-tab.html for the export and 
https://www.jetbrains.com/help/idea/2016.3/installing-plugin-from-disk.html for installing the plugin

##Runnning and using the plugin
Run the plugin module contained in the project or install the plugin as described above.
Configure the J2Swift output directories at File->Settings->J2Swift (third file path is only needed, if you are using the swift files in an Xcode Project)
You can generate default configurations for type mappings by pressing the "Generate" button

Right-click a java file and select "J2Swift"->"convert selection to swift" (or "convert scope to swift", if you ave defined a scope)
Now you can access the converted Swift-File by right-clicking the class name or a method or field name and selecting "Go To"->"Swift Implementation"
