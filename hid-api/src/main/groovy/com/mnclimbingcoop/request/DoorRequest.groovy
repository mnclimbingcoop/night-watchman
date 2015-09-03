package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.DoorCommand
import com.mnclimbingcoop.domain.type.ResponseFormat

@JacksonXmlRootElement(localName='VertXMessage')
class DoorRequest extends VertXRequest implements SimpleEntityRequest<DoorRequest> {

    DoorRequest() {
        doors = new Doors(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.STATUS)
    }

    @Override
    DoorRequest list() {
        doors = new Doors(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.STATUS)
        return this
    }

    DoorRequest lock() {
        doors = new Doors(action: Action.COMMAND_MODE, command: DoorCommand.LOCK)
        return this
    }

    DoorRequest unlock() {
        doors = new Doors(action: Action.COMMAND_MODE, command: DoorCommand.UNLOCK)
        return this
    }

    DoorRequest grantAccess() {
        doors = new Doors(action: Action.COMMAND_MODE, command: DoorCommand.GRANT)
        return this
    }

}
