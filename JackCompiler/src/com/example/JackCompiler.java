package com.example;

import java.io.File;
import java.util.Objects;

/**
 * Jack编译器
 */
public class JackCompiler {
    public static void main(String[] args) throws Exception {
        // args = new String[]{
        //         "/Volumes/CaseSensitive/11/Seven",
        //         "/Volumes/CaseSensitive/11/ConvertToBin",
        //         "/Volumes/CaseSensitive/11/Square",
        //         "/Volumes/CaseSensitive/11/Average",
        //         "/Volumes/CaseSensitive/11/Pong",
        //         "/Volumes/CaseSensitive/11/ComplexArrays"
        // };

        for (String jackFileName : args) {
            File jackFile = new File(jackFileName);
            if (jackFile.isDirectory()) {
                for (File jackFileInDir : Objects.requireNonNull(jackFile.listFiles((dir, name) -> name.endsWith(".jack")))) {
                    handle(jackFileInDir);
                }
            } else {
                handle(jackFile);
            }
        }
    }

    private static void handle(File jackFile) throws Exception {
        File writeToFile = new File(jackFile.getAbsolutePath().replace(".jack", ".vm"));
        if (writeToFile.exists()) {
            writeToFile.delete();
        }
        try (JackTokenizer jackTokenizer = new JackTokenizer(jackFile);
             VMWriter vmWriter = new VMWriter(writeToFile)
        ) {
            CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, vmWriter);
            compilationEngine.compileClass();
        }
    }

}
