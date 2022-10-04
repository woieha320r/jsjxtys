package com.example;

/**
 * 变量种类
 */
public enum VariableKind {
    STATIC("STATIC", Segment.STATIC, "静态字段"),
    FIELD("FIELD", Segment.THIS, "实例字段"),
    ARG("ARG", Segment.ARGUMENT, "方法参数"),
    VAR("VAR", Segment.LOCAL, "局部变量");

    private final String name;
    private final Segment segment;
    private final String dec;

    VariableKind(String name, Segment segment, String dec) {
        this.name = name;
        this.segment = segment;
        this.dec = dec;
    }

    public Segment getSegment() {
        return segment;
    }

    public String getName() {
        return name;
    }

    public String getDec() {
        return dec;
    }
}
