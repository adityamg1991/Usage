package com.example.aditya.usage.Event;

import com.example.aditya.usage.Data.ProcessData;

import java.util.ArrayList;

/**
 * Created by aditya on 01/07/15.
 */
public class ProcessInfoEvent {

    private ArrayList<ProcessData> processDataList;

    public ArrayList<ProcessData> getProcessDataList() {
        return processDataList;
    }

    public void setProcessDataList(ArrayList<ProcessData> processDataList) {
        this.processDataList = processDataList;
    }
}
