package com.fjmpaez.queuing.pojo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Event {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("request_trx_id")
    private String requestTrxId;

    @JsonProperty("event_params")
    private Map<String, String> eventParams = new HashMap<>();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRequestTrxId() {
        return requestTrxId;
    }

    public void setRequestTrxId(String requestTrxId) {
        this.requestTrxId = requestTrxId;
    }

    public Map<String, String> getEventParams() {
        return eventParams;
    }

    public void setEventParams(Map<String, String> eventParams) {
        this.eventParams = eventParams;
    }

    @Override
    public String toString() {
        return "Event [eventId=" + eventId + ", eventType=" + eventType + ", requestTrxId="
                + requestTrxId + ", eventParams=" + eventParams + "]";
    }

}
