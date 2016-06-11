#!/bin/sh

if [ $# -lt 1 ] ; then
    echo "$0: Usage: <path to your JDK 1.4.1 source distribution> [options]"
    exit 1
fi

prg=$0
dirname=`dirname $prg`
path=$1
options=$2
vmoptions=$3

SRC=$path/jakarta-servletapi-5/jsr152/src/share:$path/jakarta-servletapi-5/jsr154/src/share:$path/jakarta-tomcat-catalina/catalina/src/share:$path/jakarta-tomcat-catalina/modules/cluster/src:$path/jakarta-tomcat-connectors/coyote/src/java:$path/jakarta-tomcat-connectors/http11/src/java:$path/jakarta-tomcat-connectors/naming/src:$path/jakarta-tomcat-jasper/jasper2/src/share:$path/jakarta-tomcat-connectors/util/java

OBJ=$path/jakarta-tomcat-5/embed/lib/ant.jar:$path/jakarta-tomcat-5/embed/lib/mx4j-jmx.jar:$path/jakarta-tomcat-5/embed/lib/commons-collections.jar:$path/jakarta-tomcat-5/embed/lib/commons-digester.jar:$path/jakarta-tomcat-5/embed/lib/commons-el.jar:$path/jakarta-tomcat-5/embed/lib/commons-logging.jar:$path/jakarta-tomcat-5/embed/lib/commons-modeler.jar:$path/jakarta-tomcat-5/embed/lib/commons-beanutils.jar:$path/jakarta-tomcat-5/compat/common/endorsed/xercesImpl.jar:$path/jakarta-tomcat-5/build/server/lib/jakarta-regexp-1.3.jar:$path/jakarta-tomcat-5/build/bin/commons-launcher.jar

java -Xmx400M $vmoptions -jar $dirname/../jars/lint4j.jar $options -sourcepath $SRC -classpath $OBJ -exclude org.apache.tomcat.util.net.puretls:org.apache.coyote.tomcat3:prg.apache.ajp.tomcat33   org.apache.\*
