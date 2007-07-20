#!/bin/bash

me=$(dirname $0)
NIVIDIC_HOME=$HOME/workspace/nividic
NIVIDIC_CLASSES=$NIVIDIC_HOME/target/classes:/home/jourdren/Desktop/jsr223-api-1.0.jar
NIVIDIC_JS_DIR=$NIVIDIC_HOME/src/main/javascript
JAVA_ARGS="-client -Xmx512m"


#MAIN_CLASS=fr.ens.transcriptome.nividic.js.JSRunner
MAIN_CLASS=fr.ens.transcriptome.nividic.js.JSShell
CLASSPATH=$NIVIDIC_CLASSES:/usr/local/java/lib/js.jar:/usr/local/java/lib/commons-collections-3.1.jar:/usr/local/java/lib/commons-primitives-1.0.jar:/usr/local/java/lib/commons-lang-2.0.jar:/usr/local/java/lib/commons-math-1.1.jar:/usr/local/java/lib/JRclient-RF503.jar:/usr/local/java/lib/poi-3.0-FINAL.jar:/usr/local/java/lib/bytecode.jar:/usr/local/java/lib/biojava-1.5.jar

#echo $CLASSPATH
#java -cp $CLASSPATH $MAIN_CLASS `ls $NIVIDIC_JS_DIR/*.js` $*
java $JAVA_ARGS -cp $CLASSPATH $MAIN_CLASS $NIVIDIC_JS_DIR  $*
