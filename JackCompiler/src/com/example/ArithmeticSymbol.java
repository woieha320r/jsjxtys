package com.example;

import java.util.Arrays;
import java.util.Objects;

/**
 * 运算符
 */
public enum ArithmeticSymbol {
    ADD("+", "add"),
    SUB("-", "sub"),
    NEG("NO", "neg"),
    EQ("=", "eq"),
    GT("&gt;", "gt"),
    LT("&lt;", "lt"),
    AND("&amp;", "and"),
    OR("|", "or"),
    NOT("NO", "not");

    private final String token;
    private final String vm;

    ArithmeticSymbol(String token, String vm) {
        this.token = token;
        this.vm = vm;
    }

    public String getToken() {
        return token;
    }

    public String getVm() {
        return vm;
    }

    public static ArithmeticSymbol getByToken(String token) {
        return Arrays.stream(values())
                .filter(obj -> Objects.equals(obj.token, token))
                .findAny()
                .orElse(null);
    }

}
