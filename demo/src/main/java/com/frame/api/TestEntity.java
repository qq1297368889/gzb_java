package com.frame.api;

public class TestEntity {
    public  String code = "1";
    public  long time = System.currentTimeMillis();
    public String message = "1";

    public TestEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
