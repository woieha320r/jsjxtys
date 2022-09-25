package com.example.assembler;

/**
 * 指令类型（A、C、L）
 */
public enum InstructionType {

    A("A_INSTRUCTION", "A指令，@xxx"),
    C("C_INSTRUCTION", "C指令，dest=comp;jump"),
    L("L_INSTRUCTION", "L指令，符号");

    private final String name;
    private final String desc;

    InstructionType(String name, String desc) {
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
