#!/bin/sh

TOP="$(dirname $0)"

java -cp "$TOP/target/examples.jar" com.rfcode.zm.examples.Main $@
