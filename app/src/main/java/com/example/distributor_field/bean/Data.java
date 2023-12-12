package com.example.distributor_field.bean;

public class Data {
    private String param1;
    private int  param2;
    private int param3;

    public Data(String param1, int param2, int param3) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    // Include getters and setters for the parameters
    // ...

    @Override
    public String toString() {
        return "Data{" +
                "param1='" + param1 + '\'' +
                ", param2=" + param2 +
                ", param3=" + param3 +
                '}';
    }

}
