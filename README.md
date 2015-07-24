# HID Door Survey Service

[ ![Download](https://api.bintray.com/packages/mnclimbingcoop/maven/lakitu/images/download.svg) ](https://bintray.com/mnclimbingcoop/maven/lakitu/_latestVersion)

## To use

### First:

Create an `application.properties` file based on the [sample file](application.properties.sample) in this project.
Update things like your SQS queue names, add AWS key and secret if needed, add door names and IP addresses.

### Then:

Run the app via the `run` script:

    bash -c "$(curl -fsSkL raw.github.com/mnclimbingcoop/night-watchman/master/run)"

