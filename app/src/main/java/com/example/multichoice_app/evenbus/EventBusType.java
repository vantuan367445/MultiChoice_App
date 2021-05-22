package com.example.multichoice_app.evenbus;

public class EventBusType {
    private StateChange stateChange;
    private String json;

    public EventBusType(StateChange stateChange, String json) {
        this.stateChange = stateChange;
        this.json = json;
    }


    public StateChange getStateChange() {
        return stateChange;
    }

    public void setStateChange(StateChange stateChange) {
        this.stateChange = stateChange;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public enum StateChange{
        UPDATE_QUES_LOG
    }
}
