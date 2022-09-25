package com.example.vmtranslator;

/**
 * 指令类型
 */
public enum CommandType {

    PUSH("C_PUSH", "压栈，push local 1"),
    POP("C_POP", "弹栈，pop local 2"),
    ARITHMETIC("C_ARITHMETIC", "算术运算，add"),
    LABEL("C_LABEL", "标签定义，label LOOP_END"),
    IFGOTO("C_IF", "栈顶元素非0时跳转，if-goto LOOP_END"),
    GOTO("C_GOTO", "无条件跳转，goto LOOP_END"),
    FUNCTION("C_FUNCTION", "函数定义，function test(a, b)"),
    RETURN("C_RETURN", "转到函数调用者处继续执行，return"),
    CALL("C_CALL", "调用函数，call test");

    private final String name;
    private final String desc;

    CommandType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
