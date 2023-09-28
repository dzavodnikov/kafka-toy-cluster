#!/usr/bin/env bash

echo "Stop Kafka cluster and clean data directory..."

docker compose down

rm -rf data/
