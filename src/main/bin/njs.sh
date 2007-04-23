#!/bin/bash

me=$(dirname $0)
NIVIDIC_HOME=$HOME/workspace/nividic
NIVIDIC_CLASSES=$NIVIDIC_HOME/target/classes:/home/jourdren/Desktop/jsr223-api-1.0.jar
NIVIDIC_JS_DIR=$NIVIDIC_HOME/src/main/javascript

MAIN_CLASS=fr.ens.transcriptome.nividic.js.JSRunner
CLASSPATH=$NIVIDIC_CLASSES:/usr/local/java/lib/js.jar:/usr/local/java/lib/commons-collections-3.1.jar:/usr/local/java/lib/commons-primitives-1.0.jar:/usr/local/java/lib/commons-lang-2.0.jar

echo $CLASSPATH
#java -cp $CLASSPATH $MAIN_CLASS `ls $NIVIDIC_JS_DIR/*.js` $*
java -cp $CLASSPATH $MAIN_CLASS $*
