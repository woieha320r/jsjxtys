package com.example.vmtranslator;

import java.util.Arrays;
import java.util.Objects;

/**
 * 算术指令
 */
public enum Arithmetic {

    ADD("add", "加"),
    SUB("sub", "减"),
    NEG("neg", "取反"),
    EQ("eq", "相等"),
    GT("gt", "大于"),
    LT("lt", "小于"),
    AND("and", "与"),
    OR("or", "或"),
    NOT("not", "非");

    private final String name;
    private final String desc;

    Arithmetic(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static Arithmetic getByName(String name) {
        return Arrays.stream(values())
                .filter(ele -> Objects.equals(ele.getName(), name))
                .findAny()
                .orElse(null);
    }

}
