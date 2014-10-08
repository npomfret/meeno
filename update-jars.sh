#!/bin/sh

export BASE_URI=http://central.maven.org/maven2/
export LIB_DIR=lib
export TEST_LIB_DIR=test-lib

function getlib {
    get $1 $2 $LIB_DIR
}

function gettestlib {
    get $1 $2 $TEST_LIB_DIR
}

function get {
    wget --user-agent="agent" $BASE_URI/$1/$2
    mv $2 $3/$2
}

rm -rf $LIB_DIR
mkdir -p $LIB_DIR

getlib com/intellij/annotations/12.0 annotations-12.0.jar
getlib commons-io/commons-io/2.4 commons-io-2.4.jar
getlib org/apache/commons/commons-lang3/3.3.2 commons-lang3-3.3.2.jar
getlib com/google/code/gson/gson/2.3 gson-2.3.jar
getlib com/google/guava/guava/18.0 guava-18.0.jar
getlib org/apache/httpcomponents/httpclient/4.3.5 httpclient-4.3.5.jar
getlib org/apache/httpcomponents/httpcore/4.3.2 httpcore-4.3.2.jar
getlib commons-logging/commons-logging/1.2 commons-logging-1.2.jar

rm -rf $TEST_LIB_DIR
mkdir $TEST_LIB_DIR

gettestlib org/hamcrest/hamcrest-all/1.3 hamcrest-all-1.3.jar
gettestlib junit/junit/4.11 junit-4.11.jar
gettestlib junit/junit/4.11 junit-4.11-sources.jar
