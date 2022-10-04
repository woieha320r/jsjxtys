package com.example;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 符号表
 */
public class SymbolTable {

    static class Table {
        String name;
        String type;
        VariableKind kind;
        int index;

        public Table(String name, String type, VariableKind kind, int index) {
            this.name = name;
            this.type = type;
            this.kind = kind;
            this.index = index;
        }
    }

    private final Map<VariableKind, Integer> index = new HashMap<>(4);
    private final List<Table> symbolTable = new ArrayList<>();

    public SymbolTable() {
        reset();
    }

    /**
     * 重置
     */
    public void reset() {
        symbolTable.clear();
        for (VariableKind variableKind : VariableKind.values()) {
            index.put(variableKind, 0);
        }
    }

    /**
     * 添加
     *
     * @param name 名称
     * @param type 声明类型
     * @param kind 种类
     */
    public void define(@NotNull String name, @NotNull String type, @NotNull VariableKind kind) {
        int indexNow = index.get(kind);
        index.put(kind, indexNow + 1);
        symbolTable.add(new Table(name, type, kind, indexNow));
    }

    /**
     * 查询指定种类的记录条数
     *
     * @param kind 种类
     * @return int 记录条数
     */
    public int varCount(@NotNull VariableKind kind) {
        return index.get(kind);
    }

    /**
     * 查询指定记录的种类
     *
     * @param name 记录名称
     * @return VariableKind 种类，无法匹配时返回null
     */
    public @Nullable
    VariableKind kindOf(@NotNull String name) {
        Table table = symbolTable.stream().filter(ele -> Objects.equals(ele.name, name)).findAny().orElse(null);
        if (Objects.isNull(table)) {
            return null;
        }
        return table.kind;
    }

    /**
     * 查询指定记录的类型
     *
     * @param name 记录名称
     * @return String 类型，无法匹配时返回null
     */
    public @Nullable
    String typeOf(@NotNull String name) {
        Table table = symbolTable.stream().filter(ele -> Objects.equals(ele.name, name)).findAny().orElse(null);
        if (Objects.isNull(table)) {
            return null;
        }
        return table.type;
    }

    /**
     * 查询指定记录的索引
     *
     * @param name 记录名称
     * @return Integer 索引，无法匹配时返回null
     */
    public @Nullable
    Integer indexOf(@NotNull String name) {
        Table table = symbolTable.stream().filter(ele -> Objects.equals(ele.name, name)).findAny().orElse(null);
        if (Objects.isNull(table)) {
            return null;
        }
        return table.index;
    }

    public List<String> allSymbol() {
        return symbolTable.stream().map(sym -> sym.name).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("xxx", "int", VariableKind.FIELD);
        symbolTable.define("xxx2", "int", VariableKind.FIELD);
        symbolTable.define("yyy", "String", VariableKind.STATIC);
        for (VariableKind variableKind : VariableKind.values()) {
            System.out.printf("varCount-%s\t\t%d\n", variableKind.getName(), symbolTable.varCount(variableKind));
        }
        for (String name : Arrays.asList("xxx", "xxx2", "yyy")) {
            System.out.printf("kindOf-%s\t\t%s\n", name, symbolTable.kindOf(name).getName());
            System.out.printf("typeOf-%s\t\t%s\n", name, symbolTable.typeOf(name));
            System.out.printf("indexOf-%s\t\t%d\n", name, symbolTable.indexOf(name));
        }
    }

}
