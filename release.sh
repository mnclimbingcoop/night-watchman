
gradle -DRELEASE_SCOPE=MAJOR -Prelease.stage=final -Prelease.scope=MAJOR clean build release bintrayUpload
