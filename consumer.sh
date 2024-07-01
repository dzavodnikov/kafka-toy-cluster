#!/usr/bin/env bash

source env.sh

run pro.zavodnikov.kafka.TextMessageConsumer \
    --broker-list localhost:9092 \
    --topic-list "Text1,Text2"
