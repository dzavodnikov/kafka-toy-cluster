#!/usr/bin/env bash

function run() {
    local CLASS=${1}
    local MISC=${*:2}

    eval "java -cp target/Kafka-Examples-1.0-SNAPSHOT-jar-with-dependencies.jar: ${CLASS} ${MISC}"
}
