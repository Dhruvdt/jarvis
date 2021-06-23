package com.unravel.scout.model.enums;

public enum EnumItemSubType {

    INSTAGRAM("Instagram"),
    ATTRACTION("Attraction"),
    DESTINATION("Destination"),
    ACTIVITY("Activity"),
    TRIP("Trip"),
    COUNTRY("Country"),
    FOOD("Food");

    private String displayStatus;


    EnumItemSubType(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String displayStatus() { return displayStatus; }

    @Override public String toString() { return displayStatus; }
}
