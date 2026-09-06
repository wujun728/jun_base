package com.ruoyi.nocode.domain.dto;

import java.util.Set;

public class ActivitiHighLineDTO {
    private Set<String> highPoint;
    private Set<String> highLine;
    private Set<String> waitingToDo;
    private  Set<String>  iDo;

    public Set<String> getHighPoint() {
        return highPoint;
    }

    public void setHighPoint(Set<String> highPoint) {
        this.highPoint = highPoint;
    }

    public Set<String> getHighLine() {
        return highLine;
    }

    public void setHighLine(Set<String> highLine) {
        this.highLine = highLine;
    }

    public Set<String> getWaitingToDo() {
        return waitingToDo;
    }

    public void setWaitingToDo(Set<String> waitingToDo) {
        this.waitingToDo = waitingToDo;
    }

    public Set<String> getiDo() {
        return iDo;
    }

    public void setiDo(Set<String> iDo) {
        this.iDo = iDo;
    }
}
