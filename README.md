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

## Data Flow

The following [diagram](diagram.monopic) illustrates the data flow and layout of the two applications in relation to the HID Edge units that they will control and monitor.

     ┌───────────────┐
     │               │       ┌──────────────────────┐
     │ HID Edge Solo │◀───┐  │    Raspberry PI 2    │
     │               │    │  │ ┌──────────────────┐ │                                 ┌─────────────────────┐
     └───────────────┘    │  │ │  Update Script   │ │    ┌─────────────────────────┐  │     Amazon EC2      │
     ┌───────────────┐    │  │ │      (cron)      │ │    │       Amazon SQS        │  │ ┌─────────────────┐ │
     │               │    │  │ └──────────────────┘ │    │ ┌─────────────────────┐ │  │ │     Docker      │ │
     │ HID Edge Solo │◀───┤  │ ╔══════════════════╗─┼────┼▶│        State        │─┼──┼─┼▶╔═════════════╗ │ │
     │               │    │  │ ║                  ║ │    │ └─────────────────────┘ │  │ │ ║             ║ │ │
     └───────────────┘    │  │ ║                  ║ │    │ ┌─────────────────────┐ │  │ │ ║             ║ │ │
     ┌───────────────┐    ├──┼▶║  Night Watchman  ║─┼────┼▶│       Health        │─┼──┼─┼▶║   Lakitu    ║ │ │
     │               │    │  │ ║                  ║ │    │ └─────────────────────┘ │  │ │ ║             ║ │ │
     │ HID Edge EVO  │◀───┤  │ ║                  ║ │    │ ┌─────────────────────┐ │  │ │ ║             ║ │ │
     │               │    │  │ ╚══════════════════╝◀┼────┼─│      Commands       │◀┼──┼─┼─╚═════════════╝ │ │
     └───────────────┘    │  │           ▲          │    │ └─────────────────────┘ │  │ └────────▲────────┘ │
     ┌───────────────┐    │  └───────────┼──────────┘    └─────────────────────────┘  └──────────┼──────────┘
     │               │    │              │                                                       │
     │ HID Edge EVO  │◀───┘              ┌───────────────────────────────────────────────────────┐
     │               │                   │                       JSON API                        │
     └───────────────┘                   └───────────────────────────────────────────────────────┘

The [night-watcham](night-watchman/README.md) app is configured to cache data from any number of doors configured within the [application.properties](application.properties.sample) configuration file while running.  Each door can be controlled individually or across the board.  For example unlock one, or unlock all.  The JSON API is the same for the raspberry PI and for the cloud Lakitu app to keep things simple.

:warning: If you do not need cloud access, you do not need the Lakitu app.  You can aggregate your door management with just the Night Watchman app.

The Night Watchman application, when configured with the correct [~/.aws/credentials](aws-credentials.sample) file will send up state and health check information to SQS queues.  It will also listen on an SQS queue for incoming commands to be relayed to the appropriate door(s).

The [lakitu](lakitu/README.md) app is designed to be run from a [docker](https://github.com/aaronzirbes/docker-lakitu) container within an [EC2 instance](https://github.com/aaronzirbes/packer-lakitu).  It will listen on to the health and state SQS queues to monitor the health of the night watchman app and to build up an identical copy of it's state information for the doors that it is monitoring so that it can provide query access to the current state of each door and the credentials and cardholders configured within each one.

Both the Lakitu application and Night Watchman application expose the same endpoints for checking health, state and executing commands on the doors (lock, unlock, open, turn off alarm, add access holder, etc...)

## Endpoints

### Informational

 * `GET` /health
 * `GET` /health/status
 * `GET` /doors
 * `GET` /doors/{door}
 * `GET` /credentials/{door}
 * `GET` /cardholders/{door}
 * `GET` /events/{door}
 * `GET` /state/{door}

# Find cardholders by name, phone, email or fob number

 * `GET` /cardholders/find/{query} 
 * `GET` /cardholders/find/{door}/{query}

### Door Actions

 * `POST` /doors/lock -> (locks all doors)
 * `POST` /doors/lock/{door}
 * `POST` /doors/open/{door}
 * `POST` /doors/stop-alarm/{door}
 * `POST` /doors/unlock/{door}

### Access Holder

    `POST` /access

Payload Example:

    {
      "firstName": "Joe",
      "middleInitial": "Q",
      "lastName": "Climber",
      "custom1": "",
      "custom2": "",
      "emailAddress": "joe.climber@mncc.coop",
      "phoneNumber": "",
      "cards": [
        {
          "cardNumber": "4121",
          "formatName": "MNCC"
        }
        {
          "rawCardNumber": "1A2B3D4D"
        }
      ]
      "endTime": "2015-09-07T14:18:56.225"
    }

Note: You usually won't use `rawCardNumber` for access cards as the
`cardNumber` + `formatName` combo is the most prevalent.
