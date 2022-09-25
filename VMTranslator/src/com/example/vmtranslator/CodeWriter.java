package com.example.vmtranslator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * VM翻译器模块：代码生成
 * 将Parser拆解过的VM语言翻译成汇编并写入文件
 */
public class CodeWriter implements Closeable {

    private String vmFileName;
    private final FileWriter asmFileWriter;
    private int generateSymbolIndex = 0;
    private final Map<String, String> vmMapHackSymbol = new HashMap<>();
    private final Map<String, Integer> restoreSymbolIndex = new LinkedHashMap<>();

    public CodeWriter(File vmFile) throws IOException {
        String asmFileName;
        if (vmFile.isDirectory()) {
            asmFileName = vmFile.getPath() + "/" + vmFile.getName() + ".asm";
        } else {
            asmFileName = vmFile.getPath().split(".vm")[0] + ".asm";
        }
        File asmFile = new File(asmFileName);
        if (asmFile.exists() && !asmFile.delete()) {
            throw new IOException(asmFileName + "已存在，删除失败");
        } else if (!asmFile.createNewFile()) {
            throw new IOException(asmFileName + "文件创建失败");
        }
        asmFileWriter = new FileWriter(asmFile);

        vmMapHackSymbol.put("local", "LCL");
        vmMapHackSymbol.put("argument", "ARG");
        vmMapHackSymbol.put("this", "THIS");
        vmMapHackSymbol.put("that", "THAT");

        // 使用R14保存返回地址
        restoreSymbolIndex.put("R14", 5);
        restoreSymbolIndex.put("THAT", 1);
        restoreSymbolIndex.put("THIS", 2);
        restoreSymbolIndex.put("ARG", 3);
        restoreSymbolIndex.put("LCL", 4);

        // 08章的最后俩测试程序需要生成初始化栈基址并调用Sys.init的代码
        if (asmFileName.endsWith("FibonacciElement.asm") || asmFileName.endsWith("StaticsTest.asm")) {
            generatorLoadSysInit();
        }
    }

    private void generatorLoadSysInit() throws IOException {
        asmFileWriter.write("\t// system boot start\n\t@256\n\tD=A\n\t@SP\n\tM=D\n");
        writeCall("Sys.init", 0);
        String systemEndLabel = "SYSTEM_EXECUTION_END";
        writeLabel(systemEndLabel);
        writeGoto(systemEndLabel);
        asmFileWriter.write("\t// system boot end\n");
    }

    /**
     * 设置当前处理的vm文件的文件名
     *
     * @param vmFileName vm文件名称
     */
    public void setFileName(String vmFileName) {
        this.vmFileName = vmFileName.split(".vm")[0];
    }

    /**
     * 将运算转换为汇编
     *
     * @param arithmeticName 运算指令
     */
    public void writeArithmetic(String arithmeticName) throws IOException {
        String instruction = null;
        Arithmetic arithmetic = Arithmetic.getByName(arithmeticName);
        switch (arithmetic) {
            case ADD:
                instruction = arithmeticTwoParam("+");
                break;
            case SUB:
                instruction = arithmeticTwoParam("-");
                break;
            case AND:
                instruction = arithmeticTwoParam("&");
                break;
            case OR:
                instruction = arithmeticTwoParam("|");
                break;
            case EQ:
                instruction = arithmeticCompare("JEQ", "==");
                break;
            case GT:
                instruction = arithmeticCompare("JGT", ">");
                break;
            case LT:
                instruction = arithmeticCompare("JLT", "<");
                break;
            case NOT:
                instruction = arithmeticOneParam("!");
                break;
            case NEG:
                instruction = arithmeticOneParam("-");
                break;
            default:
        }
        if (Objects.nonNull(instruction)) {
            asmFileWriter.write(instruction);
        }
    }

    /**
     * 将栈操作转换为汇编
     *
     * @param command 操作指令
     * @param segment 内存段名称
     * @param index   内存段索引
     */
    public void writePushPop(String command, String segment, Integer index) throws IOException {
        asmFileWriter.write(
                Objects.equals(command, "push")
                        ? generatePush(segment, index)
                        : generatePop(segment, index)
        );
    }

    /**
     * 将标签定义转换为汇编
     *
     * @param label 标签
     */
    public void writeLabel(String label) throws IOException {
        asmFileWriter.write(String.format("(%s)\n", label));
    }

    /**
     * 将无条件跳转转换为汇编
     *
     * @param label 跳转到的标签
     */
    public void writeGoto(String label) throws IOException {
        asmFileWriter.write(String.format("\t@%s\n\t0;JMP\n", label));
    }

    /**
     * 将有条件跳转转换为汇编
     *
     * @param label 跳转到的标签
     */
    public void writeIf(String label) throws IOException {
        generateSymbolIndex++;
        asmFileWriter.write(String.format("\t// if (RAM[--SP] != 0) goto %s\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n" +
                        "\t@NOT_GOTO_%d\n\tD;JEQ\n\t@%s\n\t0;JMP\n(NOT_GOTO_%d)\n", label, generateSymbolIndex, label
                , generateSymbolIndex));
    }

    /**
     * 将函数定义转为汇编
     *
     * @param functionName 函数名称
     * @param nVars        局部变量个数
     */
    public void writeFunction(String functionName, Integer nVars) throws IOException {
        // 能否不初始化局部变量而只是留出空间：SP+=nVars
        StringBuilder waitWrite = new StringBuilder("\t// function start\n");
        writeLabel(functionName);
        for (int i = 0; i < nVars; i++) {
            waitWrite.append("\t// RAM[SP++] = 0\n\t@SP\n\tA=M\n\tM=0\n\t@SP\n\tM=M+1\n");
        }
        waitWrite.append("\t// function end\n");
        asmFileWriter.write(waitWrite.toString());
    }

    /**
     * 将函数调用定义转为汇编
     *
     * @param functionName 函数名称
     * @param nArgs        参数个数
     */
    public void writeCall(String functionName, Integer nArgs) throws IOException {
        generateSymbolIndex++;
        String returnAddressSymbol = "RETURN_ADDRESS_" + generateSymbolIndex;
        StringBuilder waitWrite = new StringBuilder("\t// call start\n");
        waitWrite.append(String.format("\t// RAM[SP++] = %s\n\t@%s\n\tD=A\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n"
                , returnAddressSymbol, returnAddressSymbol));
        Stream.of("LCL", "ARG", "THIS", "THAT").forEachOrdered(symbol ->
                waitWrite.append(String.format("\t// RAM[SP++] = %s\n\t@%s\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n"
                        , symbol, symbol)));
        waitWrite.append(String.format("\t// ARG = SP-nFrame-nArgs\n\t@%d\n\tD=A\n\t@SP\n\tD=M-D\n\t@%d\n\tD=D-A\n" +
                "\t@ARG\n\tM=D\n", restoreSymbolIndex.size(), nArgs));
        waitWrite.append("\t// LCL = SP\n\t@SP\n\tD=M\n\t@LCL\n\tM=D\n");
        waitWrite.append(String.format("\t@%s\n\t0;JMP\n", functionName));
        waitWrite.append("(").append(returnAddressSymbol).append(")\n");
        waitWrite.append("\t// call end\n");
        asmFileWriter.write(waitWrite.toString());
    }

    /**
     * 将return转为汇编
     */
    public void writeReturn() throws IOException {
        // 返回地址要在*ARG改变前取出，以免无参时返回地址被修改
        int returnAddressRestoreIndex = restoreSymbolIndex.get("R14");
        StringBuilder waitWrite = new StringBuilder("\t// return start\n");
        waitWrite.append(String.format("\t// R14 = RAM[LCL-%d]\n\t@%d\n\tD=A\n\t@LCL\n" +
                "\tA=M-D\n\tD=M\n\t@R14\n\tM=D\n", returnAddressRestoreIndex, returnAddressRestoreIndex));
        waitWrite.append("\t// RAM[ARG] = RAM[--SP]\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n" +
                "\t@ARG\n\tA=M\n\tM=D\n\t// SP = ARG+1\n\tD=A\n\t@SP\n\tM=D+1\n");
        restoreSymbolIndex.forEach((symbol, index) -> {
            if (!Objects.equals(symbol, "R14")) {
                waitWrite.append(String.format("\t// %s = RAM[LCL-%d]\n\t@%d\n\tD=A\n\t@LCL\n\tA=M-D\n\tD=M\n\t@%s\n" +
                        "\tM=D\n", symbol, index, index, symbol));
            }
        });
        waitWrite.append("\t@R14\n\tA=M\n\t0;JMP\n");
        waitWrite.append("\t// return end\n");
        asmFileWriter.write(waitWrite.toString());
    }

    /**
     * 写入文件并关闭
     */
    @Override
    public void close() throws IOException {
        asmFileWriter.flush();
        asmFileWriter.close();
    }

    private String arithmeticTwoParam(String arithmeticSymbol) {
        return String.format("\t// D = RAM[--SP]\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n" +
                "\t// RAM[--SP] %s= D\n\t@SP\n\tM=M-1\n\tA=M\n\tM=M%sD\n" +
                "\t// SP++\n\t@SP\n\tM=M+1\n", arithmeticSymbol, arithmeticSymbol
        );
    }

    private String arithmeticCompare(String compareTrueCondition, String compareSymbolInAnnotation) {
        generateSymbolIndex++;
        return String.format("\t// D = RAM[--SP]\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n" +
                        "\t// RAM[SP] = (RAM[--SP] -= D) %s 0 ? -1 : 0\n" +
                        "\t@SP\n\tM=M-1\n\tA=M\n\tD=M-D\n" +
                        "\t@COMPARE_TRUE_%d\n\tD;%s\n\t@SP\n\tA=M\n\tM=0\n" +
                        "\t@COMPARE_AFTER_%d\n\t0;JMP\n" +
                        "(COMPARE_TRUE_%d)\n\t@SP\n\tA=M\n\tM=-1\n" +
                        "(COMPARE_AFTER_%d)\n\t// SP++\n" +
                        "\t@SP\n\tM=M+1\n", compareSymbolInAnnotation, generateSymbolIndex, compareTrueCondition
                , generateSymbolIndex, generateSymbolIndex, generateSymbolIndex
        );
    }

    private String arithmeticOneParam(String arithmeticSymbol) {
        return String.format("\t// RAM[SP-1] = !RAM[SP-1]\n\t@SP\n\tA=M-1\n\tM=%sM\n", arithmeticSymbol);
    }

    private String generatePush(String segment, Integer index) {
        String dataFrom;
        switch (segment) {
            case "constant":
                dataFrom = String.format("\t// RAM[SP++] = %d\n\t@%d\n\tD=A\n", index, index);
                break;
            case "static":
                dataFrom = String.format("\t// RAM[SP++] = %s.%d\n\t@%s.%d\n\tD=M\n", vmFileName, index, vmFileName, index);
                break;
            case "pointer":
                int ramAddress = Objects.equals(index, 0) ? 3 : 4;
                dataFrom = String.format("\t// RAM[SP++] = RAM[%d]\n\t@R%d\n\tD=M\n", ramAddress, ramAddress);
                break;
            case "temp":
                dataFrom = String.format("\t// RAM[SP++] = RAM[5+%d]\n\t@5\n\tD=A\n\t@%d\n\tA=D+A\n\tD=M\n", index, index);
                break;
            default:
                dataFrom = String.format("\t// RAM[SP++] = RAM[%s + %d]\n\t@%d\n\tD=A\n\t@%s\n\tA=M+D\n\tD=M\n", segment
                        , index, index, vmMapHackSymbol.get(segment)
                );
        }
        return dataFrom + "\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
    }

    private String generatePop(String segment, Integer index) {
        String dataTo;
        String annotation;
        switch (segment) {
            case "static":
                annotation = String.format("\t// %s.%d = RAM[--SP]\n", vmFileName, index);
                dataTo = vmFileName + "." + index + "\n\tD=A";
                index = 0;
                break;
            case "pointer":
                int ramAddress = Objects.equals(index, 0) ? 3 : 4;
                annotation = String.format("\t// R%d = RAM[--SP]\n", ramAddress);
                dataTo = "R" + ramAddress + "\n\tD=A";
                index = 0;
                break;
            case "temp":
                annotation = String.format("\t// RAM[5+%d] = RAM[--SP]\n", index);
                dataTo = "5\n\tD=A";
                break;
            default:
                annotation = String.format("\t// RAM[%s + %d] = RAM[--SP]\n", segment, index);
                dataTo = vmMapHackSymbol.get(segment) + "\n\tD=M";
        }
        return annotation + String.format("\t@%s\n\t@%d\n\tD=D+A\n\t@R15\n\tM=D\n" +
                "\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n", dataTo, index
        );
    }

}
