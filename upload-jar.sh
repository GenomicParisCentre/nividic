#!/bin/sh
mvn deploy:deploy-file  \
  -DpomFile=nividic-1.0.pom \
  -DgroupId=fr.ens.transcriptome \
  -DartifactId=nividic \
  -Dversion=1.0.6 \
  -Dpackaging=jar \
  -Dfile=./target/nividic-1.0-SNAPSHOT.jar \
  -DrepositoryId=leburon-repository \
  -Durl=ftp://hestia.ens.fr/export/home1/httpd/html/leburon/maven2
