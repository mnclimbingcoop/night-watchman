# Night Watchman

![Night Watchman](nightwatch.png)

Raspberry Pi app that polls and builds state data from HID Edge devices and forwards data via SQS queue
to the Lakitu cloud app.

It also listens on an SQS queue for commands to forward to door(s) from the Lakitu app

Currently can only send door open/close/unlock commands.

