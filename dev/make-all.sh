#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

cd ${FWDIR} && \
sbt clean package -DbuildProfile=ci