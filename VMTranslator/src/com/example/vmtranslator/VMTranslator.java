package com.example.vmtranslator;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * VM翻译器
 */
public class VMTranslator {

    private static CodeWriter codeWriter;

    public static void main(String[] args) throws IOException {
        // String nand2tetrisPath = "***/nand2tetris/";
        // args = new String[]{
        //         nand2tetrisPath + "projects/07/MemoryAccess/BasicTest/BasicTest.vm",
        //         nand2tetrisPath + "projects/07/MemoryAccess/PointerTest/PointerTest.vm",
        //         nand2tetrisPath + "projects/07/MemoryAccess/StaticTest/StaticTest.vm",
        //         nand2tetrisPath + "projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm",
        //         nand2tetrisPath + "projects/07/StackArithmetic/StackTest/StackTest.vm",
        //         nand2tetrisPath + "projects/08/FunctionCalls/FibonacciElement",
        //         nand2tetrisPath + "projects/08/FunctionCalls/SimpleFunction/SimpleFunction.vm",
        //         nand2tetrisPath + "projects/08/FunctionCalls/StaticsTest",
        //         nand2tetrisPath + "projects/08/ProgramFlow/BasicLoop/BasicLoop.vm",
        //         nand2tetrisPath + "projects/08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm"
        // };

        for (String vmFileName : args) {
            File vmFile = new File(vmFileName);
            codeWriter = new CodeWriter(vmFile);
            if (vmFile.isDirectory()) {
                for (File vmFileInDir : Objects.requireNonNull(vmFile.listFiles((dir, name) -> name.endsWith(".vm")))) {
                    handle(vmFileInDir);
                }
            } else {
                handle(vmFile);
            }
            codeWriter.close();
        }
    }

    private static void handle(File vmFile) throws IOException {
        codeWriter.setFileName(vmFile.getName());
        Parser parser = new Parser(vmFile);
        while (parser.hasMoreLines()) {
            CommandType commandType = parser.commandType();
            switch (commandType) {
                case PUSH:
                    codeWriter.writePushPop("push", parser.arg1(), parser.arg2());
                    break;
                case POP:
                    codeWriter.writePushPop("pop", parser.arg1(), parser.arg2());
                    break;
                case ARITHMETIC:
                    codeWriter.writeArithmetic(parser.arg1());
                    break;
                case LABEL:
                    codeWriter.writeLabel(parser.arg1());
                    break;
                case GOTO:
                    codeWriter.writeGoto(parser.arg1());
                    break;
                case IFGOTO:
                    codeWriter.writeIf(parser.arg1());
                    break;
                case FUNCTION:
                    codeWriter.writeFunction(parser.arg1(), parser.arg2());
                    break;
                case CALL:
                    codeWriter.writeCall(parser.arg1(), parser.arg2());
                    break;
                case RETURN:
                    codeWriter.writeReturn();
                    break;
            }
            parser.advance();
        }
    }

}
