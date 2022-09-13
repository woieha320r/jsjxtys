package com.example.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 汇编器模块：符号表
 * 存取汇编源码中的所有符号（内置、自定义、变量）
 */
public class SymbolTable {

    private final Map<String, Integer> symbolMap;

    public SymbolTable() {
        symbolMap = new HashMap<>(23);
        symbolMap.put("R0", 0);
        symbolMap.put("R1", 1);
        symbolMap.put("R2", 2);
        symbolMap.put("R3", 3);
        symbolMap.put("R4", 4);
        symbolMap.put("R5", 5);
        symbolMap.put("R6", 6);
        symbolMap.put("R7", 7);
        symbolMap.put("R8", 8);
        symbolMap.put("R9", 9);
        symbolMap.put("R10", 10);
        symbolMap.put("R11", 11);
        symbolMap.put("R12", 12);
        symbolMap.put("R13", 13);
        symbolMap.put("R14", 14);
        symbolMap.put("R15", 15);
        symbolMap.put("SP", 0);
        symbolMap.put("LCL", 1);
        symbolMap.put("ARG", 2);
        symbolMap.put("THIS", 3);
        symbolMap.put("THAT", 4);
        symbolMap.put("SCREEN", 16384);
        symbolMap.put("KBD", 24576);
    }

    /**
     * 追加内容
     *
     * @param symbol  符号字符串
     * @param address 符号地址数字
     */
    public void addEntry(@NotNull String symbol, @NotNull Integer address) {
        symbolMap.put(symbol, address);
    }

    /**
     * 是否已记录此符号
     *
     * @param symbol 符号字符串
     * @return boolean true/false 已记录/未记录
     */
    public boolean contains(@NotNull String symbol) {
        return symbolMap.containsKey(symbol);
    }

    /**
     * 获取指定符号的地址，未记录时返回null
     *
     * @param symbol 符号字符串
     * @return Integer 符号地址
     */
    @Nullable
    public Integer getAddress(@NotNull String symbol) {
        return symbolMap.get(symbol);
    }

}
