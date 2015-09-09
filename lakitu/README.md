# lakitu

![Lakitu](Lakitu.png)

Cloud app that pulls state data from SQS queue and sends commands back down to door(s) via SQS queue

Currently can only send door open/close/unlock commands.

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

