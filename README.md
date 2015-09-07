# HID Door Survey Service

[ ![Download](https://api.bintray.com/packages/mnclimbingcoop/maven/lakitu/images/download.svg)](https://bintray.com/mnclimbingcoop/maven/lakitu/_latestVersion)

[![Circle CI](https://circleci.com/gh/mnclimbingcoop/night-watchman/tree/master.svg?style=svg)](https://circleci.com/gh/mnclimbingcoop/night-watchman/tree/master)

[![Snap CI](https://snap-ci.com/mnclimbingcoop/night-watchman/branch/master/build_image)](https://snap-ci.com/mnclimbingcoop/night-watchman/branch/master)

## To use

### First:

Create an `application.properties` file based on the [sample file](application.properties.sample) in this project.
Update things like your SQS queue names, add AWS key and secret if needed, add door names and IP addresses.

### Then:

Run the app via the `run` script:

    bash -c "$(curl -fsSkL raw.github.com/mnclimbingcoop/night-watchman/master/run)"

### Scripts!

Looking for start up and run scripts?

See the [scripts](https://github.com/mnclimbingcoop/night-watchman/tree/scripts) branch.
