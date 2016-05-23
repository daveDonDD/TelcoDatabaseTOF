package ddoy.telcoData.entities;

import java.io.Serializable;
import java.util.Collection;

import ddoy.telcoData.entities.EventRecord;

public class EventRecordList implements Serializable {

    public Collection<EventRecord> getEventRecordList() {
        return eventRecordList;
    }

    public void setEventRecordList(Collection<EventRecord> eventRecordList) {
        this.eventRecordList = eventRecordList;
    }


    private Collection<EventRecord> eventRecordList;




}