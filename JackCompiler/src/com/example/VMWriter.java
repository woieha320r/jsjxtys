package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 代码生成
 */
public class VMWriter implements AutoCloseable {

    private final FileWriter fileWriter;
    private final BufferedWriter fileBufferedWriter;

    public VMWriter(File writeTo) throws IOException {
        fileWriter = new FileWriter(writeTo);
        fileBufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * 写push命令
     *
     * @param segment 内存段
     * @param index   索引
     */
    public void writePush(Segment segment, int index) throws IOException {
        fileBufferedWriter.write(String.format("\tpush %s %d\n", segment.getName(), index));
    }

    /**
     * 写pop命令
     *
     * @param segment 内存段
     * @param index   索引
     */
    public void writePop(Segment segment, int index) throws IOException {
        fileBufferedWriter.write(String.format("\tpop %s %d\n", segment.getName(), index));
    }

    /**
     * 写算术逻辑符号
     *
     * @param command 算术逻辑符
     */
    public void writeArithmetic(ArithmeticSymbol command) throws IOException {
        fileBufferedWriter.write(String.format("\t%s\n", command.getVm()));
    }

    /**
     * 写标签
     *
     * @param label 标签
     */
    public void writeLabel(String label) throws IOException {
        fileBufferedWriter.write(String.format("label %s\n", label));
    }

    /**
     * 写跳转命令
     *
     * @param label 标签
     */
    public void writeGoto(String label) throws IOException {
        fileBufferedWriter.write(String.format("\tgoto %s\n", label));
    }

    /**
     * 写条件跳转命令
     *
     * @param label 标签
     */
    public void writeIf(String label) throws IOException {
        fileBufferedWriter.write(String.format("\tif-goto %s\n", label));
    }

    /**
     * 写函数调用命令
     *
     * @param name  函数名
     * @param nArgs 入参个数
     */
    public void writeCall(String name, int nArgs) throws IOException {
        fileBufferedWriter.write(String.format("\tcall %s %d\n", name, nArgs));
    }

    /**
     * 写函数定义命令
     *
     * @param name  函数名
     * @param nVars 参数个数
     */
    public void writeFunction(String name, int nVars) throws IOException {
        fileBufferedWriter.write(String.format("function %s %d\n", name, nVars));
    }

    /**
     * 写return命令
     */
    public void writeReturn() throws IOException {
        fileBufferedWriter.write("\treturn\n");
    }

    @Override
    public void close() throws Exception {
        fileBufferedWriter.flush();
        fileWriter.close();
        fileBufferedWriter.close();
    }
}
