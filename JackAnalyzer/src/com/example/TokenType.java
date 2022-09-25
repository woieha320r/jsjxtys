package com.example;

import java.util.Arrays;
import java.util.Objects;

/**
 * 标记类型
 */
public enum TokenType {

    KEYWORD("keyword", "KEYWORD"),
    SYMBOL("symbol", "SYMBOL"),
    ID("identifier", "IDENTIFIER,"),
    INT("integerConstant", "INT_CONST"),
    STR("stringConstant", "STRING_CONST");

    public String xmlEle;
    public String name;

    TokenType(String xmlEle, String name) {
        this.xmlEle = xmlEle;
        this.name = name;
    }

    public static TokenType getByXmlEle(String xmlELe) {
        return Arrays.stream(values())
                .filter(tokenType -> Objects.equals(xmlELe, tokenType.xmlEle))
                .findAny()
                .orElse(null);
    }

}
