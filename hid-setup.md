# Setting up a new Edge EVO Solo device:

## Open Browser
* Login via IP to: https://192.168.1.116/html/index.html
* username: admin
* password: (blank)

## Language Selection
* English

## EULA
* Accept End User License Agreement

## First Time setup:

### Installer Password
* Installer Password: `Pick a random 10 digit string`

### Installer Lock
* Enable Installer Lock: *Yes*
* User Password: `Pick a different random 10 digit string`

### Card Format
* Template: *Do not create a Format at this time*

### Door Parameters
* EDGE EVO Solo Door Name: `anything you want`
* Unlock Time `6`
* Extended Unlock Time `20`
* Door Held Time `38`
* Unlock on REX: `Yes`
* Door Type: `Two Reader Door`
* Reader Type: `Card`
* In Keypad Type: `HID`
* In Electrical Interface: `Wiegand`
* Out Keypad Type: `HID`
* Out Electrical Interface `Wiegand`

### Network Settings
* *Static* (preferred, but DHCP will work)
* Installer Contact Information *please fill this in*

### Displays
* Display Name as: `[First Name] [Last Name]`
* Display Date as: `mm/dd/yyyy`
* Display Time as `24:00`
* First day of the week: `Sunday`

### EDGE EVO Solo Date and Time
* Current Date/Time: *Set this*, hitting *Today* works to set the date, but you'll have to set the time manually.
* Time Zone: `GMT-0600 Central`
* TZ *automatic*

`Save then Set up Alerts`

## First Time setup:

### Alert SEttings

Change any setting you may want.  It appears to allow for HTTP hooks for certain events.

TODO: This might be good to investigate later endpoint implementation.

`Save`

It will then reboot. "This will take several minutes"
