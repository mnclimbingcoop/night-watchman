#!/bin/bash

#SCOPE=MAJOR
#SCOPE=MINOR
SCOPE=PATCH

gradle -DRELEASE_SCOPE=${SCOPE} -Prelease.stage=final -Prelease.scope=${SCOPE} clean build release bintrayUpload

git push upstream --tags
