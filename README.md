# Daily Exchange Rate Command Line Application
## Prerequisites
* JRE or JDK 17 or higher version installed
* Apache Maven installed
## Step 1. Build the Maven Project
To build the Maven project, navigate to the root directory of the project in your terminal/command prompt and run the following command:
```
mvn clean package
```
This command compiles the source code, runs tests, and creates a packaged JAR file in the target directory of the project.
## Step 2. Run the Command Line Application
To run the command line application, navigate to the target directory or directory with .jar file in your terminal/command prompt and run the following command:
```
java -jar daily-exchange-rate-1.0-SNAPSHOT.jar [args params]
```
Output file with name `exchange-rate.csv` will be generated in directory where .jar file is.

**NOTE:** this is one time running command line application. If you want to get new output, please, repeat **Step 2**.

[![Java CI with Maven](https://github.com/nacenik/daily-exchange-rate/actions/workflows/maven.yml/badge.svg)](https://github.com/nacenik/daily-exchange-rate/actions/workflows/maven.yml)
