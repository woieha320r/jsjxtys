package com.example.vmtranslator;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

/**
 * VM翻译器模块：解析器
 * 拆解VM语言源码
 */
public class Parser implements Closeable {

    private FileReader vmFileReader;
    private BufferedReader vmBufferedReader;

    private String currentLine;
    private String[] commandArr;

    private CommandType commandType;

    private boolean hasMoreLinesLastTime;

    @SuppressWarnings("unused")
    private Parser() {
    }

    /**
     * @param vmFile VM语言源码文件对象
     */
    public Parser(@NotNull File vmFile) throws IOException {
        vmFileReader = new FileReader(vmFile);
        vmBufferedReader = new BufferedReader(vmFileReader);
        hasMoreLinesLastTime = true;
        advance();
    }

    /**
     * 除 注释、空行 等无效行外，是否还有更多指令。
     *
     * @return boolean true/false 有/没有
     */
    public boolean hasMoreLines() {
        hasMoreLinesLastTime = Objects.nonNull(currentLine);
        return hasMoreLinesLastTime;
    }

    /**
     * 获取除 注释、空行 等无效行外的下一条指令。调用前需先保证hasMoreLines成立，成立时可直接操作当前指令。
     */
    public void advance() throws IOException {
        if (!hasMoreLinesLastTime) {
            throw new RuntimeException("此方法应当在hasMorLines成立时才被调用");
        }
        String line = vmBufferedReader.readLine();
        if (Objects.isNull(line)) {
            currentLine = null;
            return;
        }
        line = line.split("//")[0].trim().replaceAll("\\s{2,}", " ");
        if (line.length() != 0) {
            currentLine = line;
            commandArr = currentLine.split(" ");
            return;
        }
        advance();
    }

    /**
     * 当前指令的类型，无法判断时返回null
     *
     * @return String 指令类型名称
     */
    @Nullable
    public CommandType commandType() {
        commandType = currentLine.startsWith("push ") ? CommandType.PUSH
                : currentLine.startsWith("pop ") ? CommandType.POP
                : Objects.nonNull(Arithmetic.getByName(currentLine)) ? CommandType.ARITHMETIC
                : currentLine.startsWith("label ") ? CommandType.LABEL
                : currentLine.startsWith("if-goto ") ? CommandType.IFGOTO
                : currentLine.startsWith("goto ") ? CommandType.GOTO
                : currentLine.startsWith("function ") ? CommandType.FUNCTION
                : currentLine.equals("return") ? CommandType.RETURN
                : currentLine.startsWith("call ") ? CommandType.CALL
                : null;
        return commandType;
    }

    /**
     * 对于ARITHMETIC，返回自身
     * 对于RETURN，返回null
     * 否则返回指令中的第一个参数
     *
     * @return String 指令中第一个参数
     */
    @Nullable
    public String arg1() {
        return Objects.equals(commandType, CommandType.ARITHMETIC) ? currentLine
                : commandArr.length > 1 ? commandArr[1]
                : null;
    }

    /**
     * 返回指令中的第二个参数，仅在PUSH、POP、FUNCTION、CALL时才应被调用
     *
     * @return Integer 指令中的第二个参数
     */
    @Nullable
    public Integer arg2() {
        return commandArr.length > 2 ? Integer.valueOf(commandArr[2]) : null;
    }

    @Override
    public void close() throws IOException {
        vmBufferedReader.close();
        vmFileReader.close();
    }

    // 测试
    public static void main(String[] args) throws IOException {
        String asmFileContent = "// 测试解析器\n" +
                "\n" +
                "\tfunction test\n" +
                "\tpush local 0\n" +
                "\tpop static 1\n" +
                " label RUN\n" +
                "\tcall test2\n" +
                "   goto RUN\n" +
                "if-goto  RUN \n" +
                " sub \t \n" +
                "\treturn\n" +
                "    \n";
        String asmFileName = "./test.asm";
        File asmFile = new File(asmFileName);
        if (asmFile.exists() && !asmFile.delete()) {
            throw new IOException(asmFileName + "已存在，删除失败");
        } else if (!asmFile.createNewFile()) {
            throw new IOException(asmFileName + "文件创建失败");
        }
        try (FileWriter asmFileWriter = new FileWriter(asmFile)) {
            asmFileWriter.write(asmFileContent);
            asmFileWriter.flush();
        }
        try (Parser parser = new Parser(asmFile)) {
            while (parser.hasMoreLines()) {
                System.out.println("=========================================");
                String command = parser.currentLine;
                System.out.println("指令：" + command);
                CommandType commandType = Optional.ofNullable(parser.commandType()).orElseThrow(
                        () -> new RuntimeException("无法判断的指令类型：" + command)
                );
                System.out.println("类型：" + commandType.getName());
                Optional.ofNullable(parser.arg1()).ifPresent(arg -> System.out.println("arg1：" + arg));
                Optional.ofNullable(parser.arg2()).ifPresent(arg -> System.out.println("arg2：" + arg));
                parser.advance();
            }
        }
    }

}
