#!/usr/bin/env bash

source env.sh

TOPICS=(
    Text1
    Text2
)

for topic in "${TOPICS[@]}"; do
    run pro.zavodnikov.kafka.CreateTopic \
        --broker-list localhost:9092 \
        --name "${topic}"
done
