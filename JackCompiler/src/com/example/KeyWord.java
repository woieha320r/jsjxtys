package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 内置关键字
 */
public enum KeyWord {
    CLASS("class", "CLASS"),
    CONSTRUCTOR("constructor", "CONSTRUCTOR"),
    FUNCTION("function", "FUNCTION"),
    METHOD("method", "METHOD"),
    FIELD("field", "FIELD"),
    STATIC("static", "STATIC"),
    VAR("var", "VAR"),
    INT("int", "INT"),
    CHAR("char", "CHAR"),
    BOOLEAN("boolean", "BOOLEAN"),
    VOID("void", "VOID"),
    TRUE("true", "TRUE"),
    FALSE("false", "FALSE"),
    NULL("null", "NULL"),
    THIS("this", "THIS"),
    LET("let", "LET"),
    DO("do", "DO"),
    IF("if", "IF"),
    ELSE("else", "ELSE"),
    WHILE("while", "WHILE"),
    RETURN("return", "RETURN");

    public final String xmlEle;
    public final String name;

    KeyWord(String xmlEle, String name) {
        this.xmlEle = xmlEle;
        this.name = name;
    }

    public static KeyWord getByXmlEle(String xmlEle) {
        return Arrays.stream(values()).filter(keyWord -> Objects.equals(keyWord.xmlEle, xmlEle)).findAny().orElse(null);
    }

    public static List<String> xmlEles() {
        return Arrays.stream(values()).map(keyWord -> keyWord.xmlEle).collect(Collectors.toList());
    }

}
