package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Readers
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

class ReaderRequest extends VertXRequest implements SimpleEntityRequest<ReaderRequest> {

    static final Integer readerID = 1

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
            reader: reader
        )
        return this
    }

}
