# WulffRunsBeta

WulffRunsBeta is an ANTLR4-based parser for mathematical expressions.

#### Usage
```
2+2             // 4
2+6*10			// 62
(2+93)*2        // 190
92/(3*53)       // 0
```
Looking at the last example, you can see that currently WulffRunsBeta is not able to process double values. 
Instead, they will be rounded off to integer values.

#### Installation

Project requires [antlr-4.4-complete.jar](http://www.antlr.org/download/) v4.4 to compile.
The JAR has to be located in `/usr/local/lib/`, or the system path has to be changed to the desired path in `/pom.xml`.

##### Start
```
cd src/main/java
javac -cp /usr/local/lib/antlr-4.4-complete.jar *.java
java Initializer
```
