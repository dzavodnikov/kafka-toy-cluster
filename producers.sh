#!/usr/bin/env bash

source env.sh

run pro.zavodnikov.kafka.TextMessageProducer \
    --broker-list localhost:9092 \
    --topic Text1 \
    --delay 1 \
    --total 10

run pro.zavodnikov.kafka.TextMessageProducer \
    --broker-list localhost:9092 \
    --topic Text2 \
    --delay 1 \
    --total 10
