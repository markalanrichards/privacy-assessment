#!/usr/bin/env bash

DIR="$(dirname "$(readlink -f "$0")")"
AVPRS_DIR=$DIR/avprs
BACKEND_DIR=$DIR/backend/avro-api/src/main/avro
GRAPHQL_DIR=$DIR/graphql-middleware/avpr
mkdir -p $BACKEND_DIR
mkdir -p $GRAPHQL_DIR
rm $BACKEND_DIR/*
rm $GRAPHQL_DIR/*
cp -r $AVPRS_DIR/* $BACKEND_DIR
cp -r $AVPRS_DIR/* $GRAPHQL_DIR