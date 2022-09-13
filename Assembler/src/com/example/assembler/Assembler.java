package com.example.assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 汇编器
 * 没有判断符号的命名要求，只要不是数字就按符号处理，数字允许的范围是[0, 32767]，因为只有15位可以表示数字，不支持负数常量。
 * TODO: 添加行号报错
 */
public class Assembler {

    private final Pattern numPattern = Pattern.compile("[\\d]*");

    private String binStrFillPrefixZero(String str, int binLength) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() < binLength) {
            strBuilder.insert(0, "0");
        }
        return strBuilder.toString();
    }

    private void recordSymbol(Parser parser, SymbolTable symbolTable) throws IOException {
        int instructionAddress = -1;
        String symbol;
        while (parser.hasMoreLines()) {
            InstructionType instructionType = parser.instructionType();
            if (Objects.equals(instructionType, InstructionType.L)) {
                symbol = parser.symbol();
                if (symbolTable.contains(symbol)) {
                    throw new RuntimeException("语法错误：重复符号：" + symbol);
                }
                symbolTable.addEntry(symbol, instructionAddress + 1);
            } else {
                instructionAddress++;
            }
            parser.advance();
        }
    }

    private void translateCode(Parser parser, SymbolTable symbolTable, FileWriter hackFileWriter) throws IOException {
        Code code = new Code();
        int variableAddress = 16;
        String symbol;
        Integer symbolNum;
        String machineInstruction;
        while (parser.hasMoreLines()) {
            InstructionType instructionType = parser.instructionType();
            switch (instructionType) {
                case A:
                    symbol = parser.symbol();
                    if (numPattern.matcher(symbol).matches()) {
                        symbolNum = Integer.parseInt(symbol);
                        if (symbolNum < 0 || symbolNum > 32767) {
                            throw new RuntimeException("语法错误：允许的数字范围是[0, 32767]：" + symbol);
                        }
                    } else {
                        if (!symbolTable.contains(symbol)) {
                            symbolTable.addEntry(symbol, variableAddress++);
                        }
                        symbolNum = symbolTable.getAddress(symbol);
                    }
                    machineInstruction = binStrFillPrefixZero(Integer.toBinaryString(symbolNum), 16);
                    break;
                case C:
                    machineInstruction = "111" + code.comp(parser.comp()) + code.dest(parser.dest()) + code.jump(parser.jump());
                    break;
                default:
                    machineInstruction = null;
            }
            if (Objects.nonNull(machineInstruction)) {
                hackFileWriter.write(machineInstruction + "\n");
            }
            parser.advance();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> asmFilePahs = new ArrayList<>();
        String projectPath = "nand2tetris路径";
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/add/Add.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/max/Max.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/max/MaxL.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/rect/Rect.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/rect/RectL.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/pong/Pong.asm");
        asmFilePahs.add(projectPath + "/随书工具/nand2tetris/projects/06/pong/PongL.asm");

        Assembler assembler = new Assembler();
        File asmFile;
        SymbolTable symbolTable;
        for (String asmFilePath : asmFilePahs) {
            asmFile = new File(asmFilePath);
            symbolTable = new SymbolTable();
            try (Parser parser = new Parser(asmFile)) {
                assembler.recordSymbol(parser, symbolTable);
            }
            String hackFileName = asmFilePath.split(".asm")[0] + ".hack";
            File hackFile = new File(hackFileName);
            if (hackFile.exists() && !hackFile.delete()) {
                throw new IOException(hackFileName + "已存在，删除失败");
            } else if (!hackFile.createNewFile()) {
                throw new IOException(hackFileName + "文件创建失败");
            }
            try (Parser parser = new Parser(asmFile);
                 FileWriter hackFileWriter = new FileWriter(hackFile)
            ) {
                assembler.translateCode(parser, symbolTable, hackFileWriter);
                hackFileWriter.flush();
            }
        }
    }
}
