# lakitu

![Lakitu](Lakitu.png)

Cloud app that pulls state data from SQS queue and sends commands back down to door(s) via SQS queue

It is designed to be run from a [docker](https://github.com/aaronzirbes/docker-lakitu) container within an [EC2 instance](https://github.com/aaronzirbes/packer-lakitu).

It can however be run via docker on your own Internet accessible server via docker or by hand via an init.d or upstart script.

It's configured via the [application.properties](application.properties.sample) file that sits in the same folder as the JAR file it is run from (or is in the same folder the packer image is built from)


