package com.example.managementapp;

public class BorderNameModel {

    private String borderName;
    private int id;

    public BorderNameModel(String borderName, int id) {
        this.borderName = borderName;
        this.id = id;
    }

    public String getBorderName() {
        return borderName;
    }

    public void setBorderName(String borderName) {
        this.borderName = borderName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
