#!/bin/bash

#SCOPE=MAJOR
SCOPE=MINOR

gradle -DRELEASE_SCOPE=${SCOPE} -Prelease.stage=final -Prelease.scope=${SCOPE} clean build release bintrayUpload
