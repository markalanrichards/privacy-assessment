#!/usr/bin/env bash

DIR="$(dirname "$(readlink -f "$0")")"
AVPRS_DIR=${AVPRS_DIR:-$DIR/../..//avprs}
BACKEND_DIR=$DIR/src/main/avro
mkdir -p $BACKEND_DIR
rm $BACKEND_DIR/*
cp -r $AVPRS_DIR/* $BACKEND_DIR