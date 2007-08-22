#!/bin/bash

me=$(dirname $0)
NIVIDIC_HOME=$HOME/workspace/nividic
NIVIDIC_CLASSES=$NIVIDIC_HOME/target/classes:/home/jourdren/Desktop/jsr223-api-1.0.jar
NIVIDIC_JS_DIR=$NIVIDIC_HOME/src/main/javascript
LOCAL_LIB=/usr/local/java/lib
JAVA_ARGS="-client -Xmx512m"


#MAIN_CLASS=fr.ens.transcriptome.nividic.js.JSRunner
MAIN_CLASS=fr.ens.transcriptome.nividic.js.JSShell
CLASSPATH=$NIVIDIC_CLASSES:$LOCAL_LIB/js.jar:$LOCAL_LIB/commons-collections-3.1.jar:$LOCAL_LIB/commons-primitives-1.0.jar:$LOCAL_LIB/commons-lang-2.0.jar:$LOCAL_LIB/commons-math-1.1.jar:$LOCAL_LIB/JRclient-RF503.jar:$LOCAL_LIB/poi-3.0-FINAL.jar:$LOCAL_LIB/bytecode.jar:$LOCAL_LIB/biojava-1.5.jar:$LOCAL_LIB/axis.jar:$LOCAL_LIB/commons-discovery-0.2.jar:$LOCAL_LIB/jaxrpc.jar:$LOCAL_LIB/org.apache.commons.logging_1.0.4.v200706111724.jar:$LOCAL_LIB/saaj.jar:$LOCAL_LIB/wsdl4j-1.5.1.jar

CMD="$JAVA_ARGS -cp $CLASSPATH $MAIN_CLASS $NIVIDIC_JS_DIR"

foo=0
while [ "$foo" -le $(($BASH_ARGC-1)) ]
do
  INDEX=$(($BASH_ARGC-$foo-1))
  ARG=${BASH_ARGV[$INDEX]}
  foo=$(($foo+1))
  CMD="$CMD \"$ARG\""
done

echo $CMD | xargs java
