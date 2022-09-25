package com.example;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class JackAnalyzer {

    public static void main(String[] args) throws IOException {
        // args = new String[]{
        //         "/Volumes/BackUp/ArrayTest",
        //         "/Volumes/BackUp/ExpressionLessSquare",
        //         "/Volumes/BackUp/Square"
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

    private static void handle(File jackFile) throws IOException {
        File writeToFile = new File(jackFile.getAbsolutePath().replace(".jack", ".xml"));
        if (writeToFile.exists()) {
            writeToFile.delete();
        }
        try (JackTokenizer jackTokenizer = new JackTokenizer(jackFile);
             CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, writeToFile)
        ) {
            compilationEngine.compileClass();
        }
    }

}
