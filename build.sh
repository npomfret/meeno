#!/bin/bash

export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8

cd `dirname $0`

$ANT_HOME/bin/ant $*