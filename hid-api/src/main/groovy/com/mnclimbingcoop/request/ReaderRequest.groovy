package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Reader
import com.mnclimbingcoop.domain.Readers
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

@JacksonXmlRootElement(localName='VertXMessage')
class ReaderRequest extends VertXRequest implements SimpleEntityRequest<ReaderRequest> {

    static final Integer READER_ID = 1

    ReaderRequest() {
        readers = new Readers(action: Action.LIST_RECORDS)
    }

    @Override
    ReaderRequest list() {
        readers = new Readers(action: Action.LIST_RECORDS)
        return this
    }

    /** Note, only send changed fields */
    ReaderRequest update(Reader reader) {
        readers = new Readers(
            action: Action.UPDATE_DATA,
            readerID: READER_ID,
            readers: [ reader ]
        )
        return this
    }

}
