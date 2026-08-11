package com.transit.transitdata.model.RT;

public class StopTimeUpdateRT {

    public int stopsequence;
    public String stopId;
    public long arriveTime;
    public int scheduleRelationship;

    public int getStopsequence() {
        return stopsequence;
    }

    public void setStopsequence(int stopsequence) {
        this.stopsequence = stopsequence;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getScheduleRelationship() {
        return scheduleRelationship;
    }

    public void setScheduleRelationship(int scheduleRelationship) {
        this.scheduleRelationship = scheduleRelationship;
    }

    @Override
    public String toString() {
        return "StopTimeUpdateRT{" +
                "stopsequence=" + stopsequence +
                ", stopId='" + stopId + '\'' +
                ", arriveTime=" + arriveTime +
                ", scheduleRelationship=" + scheduleRelationship +
                '}';
    }
}
