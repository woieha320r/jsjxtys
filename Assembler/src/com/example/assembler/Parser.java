package com.example.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

/**
 * 汇编器模块：解析器
 * 拆解汇编源码
 */
public class Parser implements Closeable {

    private FileReader asmFileReader;
    private BufferedReader asmBufferedReader;

    private String currentLine;

    private InstructionType instructionType;

    private boolean hasMoreLinesLastTime;

    @SuppressWarnings("unused")
    private Parser() {
    }

    /**
     * @param asmFile 汇编源码文件对象
     */
    public Parser(@NotNull File asmFile) throws IOException {
        asmFileReader = new FileReader(asmFile);
        asmBufferedReader = new BufferedReader(asmFileReader);
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
        String line = asmBufferedReader.readLine();
        if (Objects.isNull(line)) {
            currentLine = null;
            return;
        }
        line = line.split("//")[0].trim();
        if (line.length() != 0) {
            currentLine = line;
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
    public InstructionType instructionType() {
        instructionType = currentLine.startsWith("@") ? InstructionType.A
                : (currentLine.startsWith("(") && currentLine.endsWith(")")) ? InstructionType.L
                : (currentLine.contains("=") || currentLine.contains(";")) ? InstructionType.C
                : null;
        return instructionType;
    }

    /**
     * 返回当前指令的标签，仅在instructionType为A、L时才应被调用。无法解析时返回null
     *
     * @return String 标签
     */
    @Nullable
    public String symbol() {
        String symbol;
        switch (instructionType) {
            case A:
                symbol = currentLine.substring(1);
                break;
            case L:
                symbol = currentLine.substring(1, currentLine.length() - 1);
                break;
            default:
                symbol = null;
        }
        return Objects.isNull(symbol) ? null : symbol.trim();
    }

    /**
     * 返回C指令的dest部分，只有在instructionType为C时才应该被调用，无法解析时返回null
     *
     * @return String dest=comp;jump中的dest部分
     */
    @Nullable
    public String dest() {
        String dest = (Objects.equals(instructionType, InstructionType.C) && currentLine.contains("="))
                ? currentLine.substring(0, currentLine.indexOf("="))
                : null;
        return Objects.isNull(dest) ? null : dest.trim();
    }

    /**
     * 返回C指令的comp部分，只有在instructionType为C时才应该被调用，无法解析时返回null
     *
     * @return String dest=comp;jump中的comp部分
     */
    @Nullable
    public String comp() {
        if (!Objects.equals(instructionType, InstructionType.C)) {
            return null;
        }
        String comp = currentLine;
        if (comp.contains("=")) {
            comp = comp.substring(comp.indexOf("=") + 1);
        }
        if (comp.contains(";")) {
            comp = comp.substring(0, comp.lastIndexOf(";"));
        }
        return comp.trim();
    }

    /**
     * 返回C指令的jump部分，只有在instructionType为C时才应该被调用，无法解析时返回null
     *
     * @return String dest=comp;jump中的jump部分
     */
    @Nullable
    public String jump() {
        String jump = (Objects.equals(instructionType, InstructionType.C) && currentLine.contains(";"))
                ? currentLine.substring(currentLine.lastIndexOf(";") + 1)
                : null;
        return Objects.isNull(jump) ? null : jump.trim();
    }

    @Override
    public void close() throws IOException {
        asmBufferedReader.close();
        asmFileReader.close();
    }

    // 测试
    public static void main(String[] args) throws IOException {
        String asmFileContent = "// 测试解析器\n" +
                "\n" +
                "\t@128\n" +
                "\t@i=;\n" +
                "\t@SCREEN\n" +
                "\tD=M\n" +
                "\tD=D+A\n" +
                "\n" +
                "(LOOP_OUT)\n" +
                "\t0 ; JMP\n" +
                "\t D =D+i=; ;JMP\n" +
                "\t\n" +
                "\t// END\n" +
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
                String instruction = parser.currentLine;
                System.out.println("指令：" + instruction);
                InstructionType instructionType = Optional.ofNullable(parser.instructionType()).orElseThrow(
                        () -> new RuntimeException("无法判断的指令类型：" + instruction)
                );
                System.out.println("类型：" + instructionType.getName());
                switch (instructionType) {
                    case A:
                    case L:
                        String symbol = parser.symbol();
                        System.out.println("标签：" + symbol);
                        break;
                    case C:
                        Optional.ofNullable(parser.dest()).ifPresent(dest -> System.out.println("dest：" + dest));
                        Optional.ofNullable(parser.comp()).ifPresent(comp -> System.out.println("comp：" + comp));
                        Optional.ofNullable(parser.jump()).ifPresent(jump -> System.out.println("jump：" + jump));
                        break;
                    default:
                }
                parser.advance();
            }
        }
    }

}
