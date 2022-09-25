package com.example;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 解析器
 * * 警告：关于句法模式的xml标签是否写入：
 * * * 当调用处 理应存在时，一定写入
 * * * 当调用处 不一定存在时，有则写入，无则不写
 */
public class CompilationEngine implements Closeable {

    private final JackTokenizer jackTokenizer;
    private final FileWriter fileWriter;
    private final BufferedWriter fileBufferedWriter;
    private final List<String> expressionOps = Arrays.asList("+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "=");

    public CompilationEngine(JackTokenizer jackTokenizer, File writeTo) throws IOException {
        this.jackTokenizer = jackTokenizer;
        fileWriter = new FileWriter(writeTo);
        fileBufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * 解析class
     * 'class' className '{' classVarDec* subroutineDec* '}'
     */
    public void compileClass() throws IOException {
        fileBufferedWriter.write("<class>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.CLASS.xmlEle));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "{"));
        compileClassVarDec();
        compileSubroutine();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "}"));
        fileBufferedWriter.write("</class>\n");
    }

    private boolean existsClassVarDec() {
        KeyWord keyword = jackTokenizer.keyword();
        return Objects.equals(keyword, KeyWord.STATIC) || Objects.equals(keyword, KeyWord.FIELD);
    }

    /**
     * 解析类级变量声明
     * ('static' | 'field' ) type varName (',' varName)* ';'
     */
    public void compileClassVarDec() throws IOException {
        while (existsClassVarDec()) {
            fileBufferedWriter.write("<classVarDec>\n");
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD));
            compileType();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ","));
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            }
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ";"));
            fileBufferedWriter.write("</classVarDec>\n");
        }
    }

    private boolean existsSubroutine() {
        KeyWord keyWord = jackTokenizer.keyword();
        return Objects.equals(keyWord, KeyWord.CONSTRUCTOR)
                || Objects.equals(keyWord, KeyWord.FUNCTION)
                || Objects.equals(keyWord, KeyWord.METHOD);
    }

    /**
     * 解析子例程：函数/构造/方法
     * ('constructor' | 'function' | 'method') ('void' | type) subroutineName '(' parameterList ')' subroutineBody
     */
    public void compileSubroutine() throws IOException {
        while (existsSubroutine()) {
            fileBufferedWriter.write("<subroutineDec>\n");
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD));
            if (Objects.equals(jackTokenizer.keyword(), KeyWord.VOID)) {
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.VOID.xmlEle));
            } else {
                compileType();
            }
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
            compileParameterList();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
            compileSubroutineBody();
            fileBufferedWriter.write("</subroutineDec>\n");
        }
    }

    private boolean existsType() {
        TokenType tokenType = jackTokenizer.tokenType();
        KeyWord keyWord = jackTokenizer.keyword();
        return Objects.equals(tokenType, TokenType.ID)
                || Objects.equals(keyWord, KeyWord.INT)
                || Objects.equals(keyWord, KeyWord.CHAR)
                || Objects.equals(keyWord, KeyWord.BOOLEAN);
    }

    /**
     * 解析参数列表
     * ( (type varName) (',' type varName)*)?
     */
    public void compileParameterList() throws IOException {
        fileBufferedWriter.write("<parameterList>\n");
        if (existsType()) {
            compileType();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL));
                compileType();
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            }
        }
        fileBufferedWriter.write("</parameterList>\n");
    }

    /**
     * 解析子例程（函数/构造/方法）体
     * '{' varDec* statements '}'
     */
    public void compileSubroutineBody() throws IOException {
        fileBufferedWriter.write("<subroutineBody>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "{"));
        compileVarDec();
        compileStatements();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "}"));
        fileBufferedWriter.write("</subroutineBody>\n");
    }

    private boolean existsVarDec() {
        return Objects.equals(jackTokenizer.keyword(), KeyWord.VAR);
    }

    /**
     * 解析局部变量
     * 'var' type varName (',' varName)* ';'
     */
    public void compileVarDec() throws IOException {
        while (existsVarDec()) {
            fileBufferedWriter.write("<varDec>\n");
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.VAR.xmlEle));
            compileType();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL));
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            }
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ";"));
            fileBufferedWriter.write("</varDec>\n");
        }
    }

    private boolean existsStatements() {
        KeyWord keyWord = jackTokenizer.keyword();
        return Objects.equals(keyWord, KeyWord.LET)
                || Objects.equals(keyWord, KeyWord.IF)
                || Objects.equals(keyWord, KeyWord.WHILE)
                || Objects.equals(keyWord, KeyWord.DO)
                || Objects.equals(keyWord, KeyWord.RETURN);
    }

    /**
     * 解析语句
     * (letStatement | ifStatement| whileStatement| doStatement | returnStatement)*
     */
    public void compileStatements() throws IOException {
        fileBufferedWriter.write("<statements>\n");
        KeyWord keyWord;
        while (existsStatements()) {
            keyWord = jackTokenizer.keyword();
            switch (keyWord) {
                case LET:
                    compileLet();
                    break;
                case IF:
                    compileIf();
                    break;
                case WHILE:
                    compileWhile();
                    break;
                case DO:
                    compileDo();
                    break;
                case RETURN:
                    compileReturn();
                    break;
            }
        }
        fileBufferedWriter.write("</statements>\n");
    }

    /**
     * 解析赋值语句
     * 'let' varName ('[' expression ']')? '=' expression ';'
     */
    public void compileLet() throws IOException {
        fileBufferedWriter.write("<letStatement>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.LET.xmlEle));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
        if (Objects.equals(jackTokenizer.symbol(), "[")) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL));
            compileExpression();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "]"));
        }
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "="));
        compileExpression();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ";"));
        fileBufferedWriter.write("</letStatement>\n");
    }

    /**
     * 解析if语句
     * 'if' '(' expression ')' '{' statements '}' ('else' '{' statements '}')?
     */
    public void compileIf() throws IOException {
        fileBufferedWriter.write("<ifStatement>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.IF.xmlEle));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
        compileExpression();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "{"));
        compileStatements();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "}"));
        if (Objects.equals(jackTokenizer.keyword(), KeyWord.ELSE)) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD));
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "{"));
            compileStatements();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "}"));
        }
        fileBufferedWriter.write("</ifStatement>\n");
    }

    /**
     * 解析while语句
     * 'while' '(' expression ')' '{' statements '}'
     */
    public void compileWhile() throws IOException {
        fileBufferedWriter.write("<whileStatement>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.WHILE.xmlEle));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
        compileExpression();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "{"));
        compileStatements();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "}"));
        fileBufferedWriter.write("</whileStatement>\n");
    }

    /**
     * 解析do语句
     * 'do' subroutineCall ';'
     */
    public void compileDo() throws IOException {
        fileBufferedWriter.write("<doStatement>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.DO.xmlEle));
        compileSubroutineCall();
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ";"));
        fileBufferedWriter.write("</doStatement>\n");
    }

    /**
     * 解析return语句
     * 'return' expression? ';'
     */
    public void compileReturn() throws IOException {
        fileBufferedWriter.write("<returnStatement>\n");
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, KeyWord.RETURN.xmlEle));
        if (!Objects.equals(jackTokenizer.symbol(), ";")) {
            compileExpression();
        }
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ";"));
        fileBufferedWriter.write("</returnStatement>\n");
    }

    private boolean existsExpression() {
        return existsTerm();
    }

    private boolean existsTerm() {
        TokenType tokenType = jackTokenizer.tokenType();
        switch (tokenType) {
            case INT:
            case STR:
            case ID:
                return true;
            case KEYWORD:
                KeyWord keyWord = jackTokenizer.keyword();
                return Objects.equals(keyWord, KeyWord.TRUE)
                        || Objects.equals(keyWord, KeyWord.FALSE)
                        || Objects.equals(keyWord, KeyWord.NULL)
                        || Objects.equals(keyWord, KeyWord.THIS);
            case SYMBOL:
                String symbol = jackTokenizer.symbol();
                return Objects.equals(symbol, "(")
                        || Objects.equals(symbol, "-")
                        || Objects.equals(symbol, "~");
            default:
                return false;
        }
    }

    /**
     * 解析表达式
     * term (op term)*
     */
    public void compileExpression() throws IOException {
        fileBufferedWriter.write("<expression>\n");
        compileTerm();
        while (expressionOps.contains(jackTokenizer.symbol())) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL));
            compileTerm();
        }
        fileBufferedWriter.write("</expression>\n");
    }

    /**
     * 解析表达式元素
     * integerConstant | stringConstant| keywordConstant | varName | varName '[' expression ']' | '(' expression ')' | (unaryOp term) | subroutineCall
     */
    public void compileTerm() throws IOException {
        fileBufferedWriter.write("<term>\n");
        TokenType tokenType = jackTokenizer.tokenType();
        switch (tokenType) {
            case INT:
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.INT));
                break;
            case STR:
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.STR));
                break;
            case KEYWORD:
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, Arrays.asList(
                        KeyWord.TRUE.xmlEle, KeyWord.FALSE.xmlEle, KeyWord.NULL.xmlEle, KeyWord.THIS.xmlEle)));
                break;
            case ID:
                // subroutineCall需要两个标记才能确定，所以需要向JackTokenizer加可回退的解决方案：back(int count)
                jackTokenizer.advance();
                String nextSymbol = jackTokenizer.symbol();
                jackTokenizer.back(1);
                if (Objects.equals(nextSymbol, "(") || Objects.equals(nextSymbol, ".")) {
                    compileSubroutineCall();
                } else {
                    // varName | varName '[' expression ']'
                    fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
                    if (Objects.equals(jackTokenizer.symbol(), "[")) {
                        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "["));
                        compileExpression();
                        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "]"));
                    }
                }
                break;
            case SYMBOL:
                String symbol = jackTokenizer.symbol();
                if (Objects.equals(symbol, "(")) {
                    // '(' expression ')'
                    fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
                    compileExpression();
                    fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
                } else {
                    // (unaryOp term)
                    fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, Arrays.asList("-", "~")));
                    compileTerm();
                }
                break;
        }
        fileBufferedWriter.write("</term>\n");
    }

    /**
     * 解析子例程调用
     * subroutineName '(' expressionList ')' | (className | varName) '.' subroutineName '(' expressionList ')'
     */
    private void compileSubroutineCall() throws IOException {
        fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
        String symbol = jackTokenizer.symbol();
        if (Objects.equals(symbol, "(")) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
            compileExpressionList();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
        } else if (Objects.equals(symbol, ".")) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "."));
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, "("));
            compileExpressionList();
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ")"));
        } else {
            throw new RuntimeException("试图解析子例程，但模式匹配失败");
        }
    }

    /**
     * 解析表达式列表
     * (expression (',' expression)*)?
     *
     * @return int 列表中表达式的数量
     */
    public int compileExpressionList() throws IOException {
        fileBufferedWriter.write("<expressionList>\n");
        int count = 0;
        if (existsExpression()) {
            compileExpression();
            count++;
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.SYMBOL, ","));
                compileExpression();
                count++;
            }
        }
        fileBufferedWriter.write("</expressionList>\n");
        return count;
    }

    private void compileType() throws IOException {
        TokenType tokenType = jackTokenizer.tokenType();
        if (Objects.equals(tokenType, TokenType.ID)) {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.ID));
        } else {
            fileBufferedWriter.write(generateXmlFromJackTokenizer(TokenType.KEYWORD, Arrays.asList(
                    KeyWord.INT.xmlEle, KeyWord.CHAR.xmlEle, KeyWord.BOOLEAN.xmlEle)));
        }
    }

    private String generateXmlFromJackTokenizer(TokenType expectType, String expectVal) throws IOException {
        return generateXmlFromJackTokenizer(Collections.singletonList(expectType), Collections.singletonList(expectVal));
    }

    private String generateXmlFromJackTokenizer(TokenType expectType, List<String> expectVals) throws IOException {
        return generateXmlFromJackTokenizer(Collections.singletonList(expectType), expectVals);
    }

    private String generateXmlFromJackTokenizer(TokenType... expectType) throws IOException {
        List<TokenType> expectTypes = Arrays.asList(expectType);
        return generateXmlFromJackTokenizer(expectTypes.isEmpty() ? null : expectTypes, null);
    }

    private String generateXmlFromJackTokenizer(List<TokenType> expectTypes, List<String> expectVals) throws IOException {
        if (Objects.nonNull(expectTypes) && expectTypes.size() > 1 && Objects.nonNull(expectVals) && expectVals.size() > 1) {
            throw new RuntimeException("不可多个类型多个值一起判断");
        }
        String xmlVal;
        TokenType tokenType = jackTokenizer.tokenType();
        if (Objects.nonNull(expectTypes) && !expectTypes.contains(tokenType)) {
            throw new RuntimeException(String.format("期待类型：%s\n实际类型：%s"
                    , expectTypes.stream().map(expect -> expect.name).collect(Collectors.joining(","))
                    , tokenType.name));
        }
        switch (tokenType) {
            case KEYWORD:
                xmlVal = jackTokenizer.keyword().xmlEle;
                break;
            case SYMBOL:
                xmlVal = jackTokenizer.symbol();
                break;
            case ID:
                xmlVal = jackTokenizer.identifier();
                break;
            case INT:
                xmlVal = String.valueOf(jackTokenizer.intVal());
                break;
            case STR:
                xmlVal = jackTokenizer.stringVal();
                break;
            default:
                xmlVal = "";
        }
        if (Objects.nonNull(expectVals) && !expectVals.contains(xmlVal)) {
            throw new RuntimeException(String.format("期待值：%s\n实际值：%s", String.join(",", expectVals), xmlVal));
        }
        if (jackTokenizer.hasMoreTokens()) {
            jackTokenizer.advance();
        } else {
            throw new RuntimeException("标记流已到尽头");
        }
        return String.format("<%s>%s</%s>\n", tokenType.xmlEle, xmlVal, tokenType.xmlEle);
    }

    @Override
    public void close() throws IOException {
        fileBufferedWriter.flush();
        fileWriter.close();
        fileBufferedWriter.close();
    }
}
