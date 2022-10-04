package com.example;

import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 标记化
 */
public class JackTokenizer implements Closeable {

    private String jackContent;
    private final List<String> symbols = Arrays.asList("{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~");

    private final FileReader jackTReader;
    private final BufferedReader jackTBufferedReader;
    private boolean hasMoreLinesLastTime;
    private String currentLine;

    private final List<String> delLines = new LinkedList<>();
    private final List<String> stagingLines = new LinkedList<>();

    public JackTokenizer(File jackFile) throws IOException {
        jackContent = new String(Files.readAllBytes(Paths.get(jackFile.toURI())));
        handleBlack();
        handleSymbolAndStr();
        handleKeyword();
        handleIntId();
        File jackTFile = new File(jackFile.getAbsolutePath().replace(".jack", "T.xml"));
        if (jackTFile.exists()) {
            jackTFile.delete();
        }
        try (FileWriter fileWriter = new FileWriter(jackTFile)) {
            fileWriter.write(jackContent);
            fileWriter.flush();
        }
        jackTReader = new FileReader(jackTFile);
        jackTBufferedReader = new BufferedReader(jackTReader);
        hasMoreLinesLastTime = true;
        advance();
        delLines.remove(0);
    }

    private void handleBlack() {
        jackContent = jackContent
                // 去除单行注释
                .replaceAll("//.*\\S*", "")
                // 去除多行注释
                //.replaceAll("\\/\\*(?:[^\\*]|\\*+[^\\/\\*])*\\*+\\/\\s*", "")
                .replaceAll("\\/\\*(?:\\*(?!\\/)|[^\\*])*\\*\\/\\s*", "")
                // 去除空行和行前导空白
                .replaceAll("(?m)^\\s*", "")
                // 将换行符转为空格
                .replaceAll("(?m)$\\s*", " ")
                // 将所有空白（连续/单个）转为一个空格
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void handleSymbolAndStr() {
        // 先把所有双引号包围的字符串拿走单独记录，并留下占位符以待填回字符串。以免修改了字符串常量。正则表达式匹配非双引号内的特殊字符，不会写😓
        String protectStr = "XiLn_hU";
        List<String> strList = new ArrayList<>();
        StringBuilder newJackContent = new StringBuilder();
        int segmentIndex = 0;
        int strIndex = -1;
        // TODO:没处理转义，需要排除\前缀的引号
        for (String jackContentSegment : jackContent.split("\"")) {
            segmentIndex++;
            if (segmentIndex % 2 == 0) {
                strList.add(jackContentSegment);
            } else {
                strIndex++;
                newJackContent.append(jackContentSegment).append(protectStr).append(strIndex).append(protectStr);
            }
        }
        jackContent = newJackContent.toString().replace(protectStr + strIndex + protectStr, "");

        String symbolReplaceTo;
        String protectMySymbol = "_@`";
        for (String symbol : symbols) {
            switch (symbol) {
                case "<":
                    symbolReplaceTo = "&lt;";
                    break;
                case ">":
                    symbolReplaceTo = "&gt;";
                    break;
                case "\"":
                    symbolReplaceTo = "&quot;";
                    break;
                case "&":
                    symbolReplaceTo = "&amp;";
                    break;
                default:
                    symbolReplaceTo = symbol;
            }
            jackContent = jackContent.replaceAll(String.format("\\%s(?!%s)\\s*", symbol, protectMySymbol), String.format("\n<%s%s>%s%s<%s/%s%s>%s\n", protectMySymbol, TokenType.SYMBOL.xmlEle, protectMySymbol, symbolReplaceTo, protectMySymbol, protectMySymbol, TokenType.SYMBOL.xmlEle, protectMySymbol));
        }

        for (int i = 0; i < strList.size(); i++) {
            jackContent = jackContent.replace(protectStr + i + protectStr, String.format("\n<%s>%s</%s>\n", TokenType.STR.xmlEle, strList.get(i), TokenType.STR.xmlEle));
        }

        jackContent = jackContent
                // 去除符号保护符
                .replaceAll(protectMySymbol, "")
                // 去除空行和行前导空白
                .replaceAll("(?m)^\\s*", "");
    }

    private void handleKeyword() {
        for (String keyword : KeyWord.xmlEles()) {
            jackContent = jackContent.replaceAll(String.format("%s\\s+", keyword), String.format("\n<%s>%s</%s>\n", TokenType.KEYWORD.xmlEle, keyword, TokenType.KEYWORD.xmlEle));
        }
        // 去除空行和行前导空白
        jackContent = jackContent.replaceAll("(?m)^\\s*", "");
    }

    /*
    不知道为啥不行，该死的，合并到handleIntStrId里去
    private static void handleString() {
        jackContent = jackContent.replaceAll("^\"", "<stringConstant>")
                .replaceAll("\"\\s*$", "</stringConstant>");
    }
    */

    private void handleIntId() {
        StringBuilder tempStrBuilder = new StringBuilder();
        for (String str : jackContent.split("\n")) {
            if (str.startsWith("<")) {
                tempStrBuilder.append(str).append("\n");
            } else {
                for (String strSplitEle : str.trim().split(" ")) {
                    if (strSplitEle.matches("\\d*")) {
                        // [0, 32767]
                        tempStrBuilder.append(String.format("<%s>%s</%s>\n", TokenType.INT.xmlEle, strSplitEle, TokenType.INT.xmlEle));
                    } else {
                        tempStrBuilder.append(String.format("<%s>%s</%s>\n", TokenType.ID.xmlEle, strSplitEle, TokenType.ID.xmlEle));
                    }
                }
            }
        }
        // 去除空行和行前导空白
        jackContent = tempStrBuilder.toString().replaceAll("(?m)^\\s*", "");
    }

    /**
     * 除 注释、空行 等无效行外，是否还有更多指令。
     *
     * @return boolean true/false 有/没有
     */
    public boolean hasMoreTokens() {
        hasMoreLinesLastTime = stagingLines.size() > 0 || Objects.nonNull(currentLine);
        return hasMoreLinesLastTime;
    }

    /**
     * 获取除 注释、空行 等无效行外的下一条指令。调用前需先保证hasMoreLines成立，成立时可直接操作当前指令。
     */
    public void advance() throws IOException {
        delLines.add(currentLine);
        if (!hasMoreLinesLastTime) {
            throw new RuntimeException("此方法应当在hasMoreTokens成立时才被调用");
        }
        String line = stagingLines.size() > 0 ? stagingLines.remove(0) : jackTBufferedReader.readLine();
        if (Objects.isNull(line)) {
            currentLine = null;
            return;
        }
        if (line.length() != 0) {
            currentLine = line;
            return;
        }
        advance();
    }

    /**
     * 当前标记类型，无法判断时返回null
     *
     * @return String 标记类型名称
     */
    @Nullable
    public TokenType tokenType() {
        return TokenType.getByXmlEle(currentLine.split(">")[0].substring(1));
    }

    /**
     * 关键字标记的值（大写）
     *
     * @return String 关键字 匹配失败返回null
     */
    @Nullable
    public KeyWord keyword() {
        return KeyWord.getByXmlEle(currentLine.replaceAll(String.format("<%s>", TokenType.KEYWORD.xmlEle), "")
                .replaceAll(String.format("</%s>", TokenType.KEYWORD.xmlEle), "")
        );
    }

    /**
     * 内置符号标记的值
     *
     * @return String 内置符号
     */
    public String symbol() {
        return currentLine.replaceAll(String.format("<%s>", TokenType.SYMBOL.xmlEle), "")
                .replaceAll(String.format("</%s>", TokenType.SYMBOL.xmlEle), "");
    }

    /**
     * 自定义符号标记的值
     *
     * @return String 自定义符号
     */
    public String identifier() {
        return currentLine.replaceAll(String.format("<%s>", TokenType.ID.xmlEle), "")
                .replaceAll(String.format("</%s>", TokenType.ID.xmlEle), "");
    }

    /**
     * 整形常量的值
     *
     * @return int 整形常量值
     */
    public int intVal() {
        return Integer.parseInt(currentLine.replaceAll(String.format("<%s>", TokenType.INT.xmlEle), "")
                .replaceAll(String.format("</%s>", TokenType.INT.xmlEle), "")
        );
    }

    /**
     * 字符串常量的值
     *
     * @return String 字符串常量
     */
    public String stringVal() {
        return currentLine.replaceAll(String.format("<%s>", TokenType.STR.xmlEle), "")
                .replaceAll(String.format("</%s>", TokenType.STR.xmlEle), "");
    }

    /**
     * 与advance相反，相当于取消执行count次advance
     */
    public void back(int count) throws IOException {
        if (count <= 0 || count > delLines.size()) {
            throw new RuntimeException(String.format("试图回退%d次，不在允许的范围中：(0, %d]", count, delLines.size()));
        }
        Stack<String> waitStaging = new Stack<>();
        if (Objects.nonNull(currentLine)) {
            waitStaging.push(currentLine);
        }
        while (count-- > 0) {
            waitStaging.push(delLines.remove(delLines.size() - 1));
        }
        while (!waitStaging.isEmpty()) {
            stagingLines.add(waitStaging.pop());
        }
        advance();
    }

    @Override
    public void close() throws IOException {
        jackTReader.close();
        jackTBufferedReader.close();
    }

}
