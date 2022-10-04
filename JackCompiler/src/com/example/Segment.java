package com.example;

/**
 * vm内存段
 */
public enum Segment {
    CONSTANT("constant", "常量"),
    ARGUMENT("argument", "方法参数"),
    LOCAL("local", "局部变量"),
    STATIC("static", "静态字段"),
    THIS("this", "对象引用"),
    THAT("that", "数组引用"),
    POINTER("pointer", "0为this，1为that"),
    TEMP("temp", "无专用");

    private final String name;
    private final String dec;

    Segment(String name, String dec) {
        this.dec = dec;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDec() {
        return dec;
    }
}
