package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 解析器
 */
public class CompilationEngine {

    private final JackTokenizer jackTokenizer;
    private final VMWriter vmWriter;
    private final List<String> expressionOps = Arrays.asList("+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "=");
    private String className;
    private SymbolTable classSymbolTable;
    private SymbolTable methodSymbolTable;
    private int generateLabelIndex = 0;

    public CompilationEngine(JackTokenizer jackTokenizer, VMWriter writeTo) {
        this.jackTokenizer = jackTokenizer;
        this.vmWriter = writeTo;
    }

    /**
     * 解析class
     * 'class' className '{' classVarDec* subroutineDec* '}'
     */
    public void compileClass() throws IOException {
        classSymbolTable = new SymbolTable();

        jackTokenizer.advance();

        className = jackTokenizer.identifier();
        jackTokenizer.advance();

        jackTokenizer.advance();
        compileClassVarDec();
        compileSubroutine();
        jackTokenizer.advance();
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
            KeyWord keyWord = jackTokenizer.keyword();
            jackTokenizer.advance();
            VariableKind variableKind = Objects.equals(keyWord, KeyWord.STATIC) ? VariableKind.STATIC : VariableKind.FIELD;

            String type = compileType();
            String varName = jackTokenizer.identifier();
            jackTokenizer.advance();
            classSymbolTable.define(varName, type, variableKind);

            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                jackTokenizer.advance();
                varName = jackTokenizer.identifier();
                jackTokenizer.advance();
                classSymbolTable.define(varName, type, variableKind);
            }
            jackTokenizer.advance();
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
            methodSymbolTable = new SymbolTable();

            KeyWord funType = jackTokenizer.keyword();
            jackTokenizer.advance();
            if (Objects.equals(funType, KeyWord.METHOD)) {
                methodSymbolTable.define("this", className, VariableKind.ARG);
            }

            KeyWord returnType = jackTokenizer.keyword();
            jackTokenizer.advance();

            String methodName = jackTokenizer.identifier();
            jackTokenizer.advance();

            jackTokenizer.advance();
            compileParameterList();
            jackTokenizer.advance();
            compileSubroutineBody(funType, returnType, methodName);
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
     * ((type varName) (',' type varName)*)?
     */
    public void compileParameterList() throws IOException {
        if (existsType()) {
            String type = compileType();
            String argName = jackTokenizer.identifier();
            jackTokenizer.advance();
            methodSymbolTable.define(argName, type, VariableKind.ARG);
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                jackTokenizer.advance();
                type = compileType();
                argName = jackTokenizer.identifier();
                jackTokenizer.advance();
                methodSymbolTable.define(argName, type, VariableKind.ARG);
            }
        }
    }

    /**
     * 解析子例程（函数/构造/方法）体
     * '{' varDec* statements '}'
     */
    public void compileSubroutineBody(KeyWord funType, KeyWord returnType, String methodName) throws IOException {
        jackTokenizer.advance();

        compileVarDec();
        int varCount = methodSymbolTable.varCount(VariableKind.VAR);
        vmWriter.writeFunction(String.format("%s.%s", className, methodName), varCount);

        switch (funType) {
            case CONSTRUCTOR:
                vmWriter.writePush(Segment.CONSTANT, classSymbolTable.varCount(VariableKind.FIELD));
                vmWriter.writeCall("Memory.alloc", 1);
                vmWriter.writePop(Segment.POINTER, 0);
                break;
            case METHOD:
                vmWriter.writePush(Segment.ARGUMENT, 0);
                vmWriter.writePop(Segment.POINTER, 0);
                break;
            default:
        }

        compileStatements(Objects.equals(returnType, KeyWord.VOID));
        jackTokenizer.advance();
    }

    /**
     * 解析局部变量
     * 'var' type varName (',' varName)* ';'
     */
    public void compileVarDec() throws IOException {
        while (Objects.equals(jackTokenizer.keyword(), KeyWord.VAR)) {
            jackTokenizer.advance();

            String type = compileType();
            String varName = jackTokenizer.identifier();
            jackTokenizer.advance();
            methodSymbolTable.define(varName, type, VariableKind.VAR);
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                jackTokenizer.advance();
                varName = jackTokenizer.identifier();
                jackTokenizer.advance();
                methodSymbolTable.define(varName, type, VariableKind.VAR);
            }
            jackTokenizer.advance();
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

    public void compileStatements() throws IOException {
        compileStatements(false);
    }

    /**
     * 解析语句
     * (letStatement | ifStatement| whileStatement| doStatement | returnStatement)*
     *
     * @param isVoid 是否声明了返回void
     */
    public void compileStatements(boolean isVoid) throws IOException {
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
                    compileReturn(isVoid);
                    break;
            }
        }
    }

    /**
     * 解析赋值语句
     * 'let' varName ('[' expression ']')? '=' expression ';'
     */
    public void compileLet() throws IOException {
        jackTokenizer.advance();

        String varName = jackTokenizer.identifier();
        jackTokenizer.advance();

        if (Objects.equals(jackTokenizer.symbol(), "[")) {
            jackTokenizer.advance();
            vmWriter.writePush(getSegmentFromSymbolTable(varName), getIndexFromSymbolTable(varName));
            compileExpression();
            vmWriter.writeArithmetic(ArithmeticSymbol.ADD);
            jackTokenizer.advance();
            jackTokenizer.advance();
            compileExpression();
            vmWriter.writePop(Segment.TEMP, 0);
            vmWriter.writePop(Segment.POINTER, 1);
            vmWriter.writePush(Segment.TEMP, 0);
            vmWriter.writePop(Segment.THAT, 0);
        } else {
            jackTokenizer.advance();
            compileExpression();
            vmWriter.writePop(getSegmentFromSymbolTable(varName), getIndexFromSymbolTable(varName));
        }

        jackTokenizer.advance();
    }

    /**
     * 解析if语句
     * 'if' '(' expression ')' '{' statements '}' ('else' '{' statements '}')?
     */
    public void compileIf() throws IOException {
        int labelIndex = generateLabelIndex++;
        String failLabel = String.format("%s_IF_NOT_%d", className, labelIndex);
        String nextLabel = String.format("%s_IF_NEXT_%d", className, labelIndex);

        jackTokenizer.advance();
        jackTokenizer.advance();

        compileExpression();
        compileBoolReverse();
        vmWriter.writeIf(failLabel);

        jackTokenizer.advance();
        jackTokenizer.advance();

        compileStatements();
        vmWriter.writeGoto(nextLabel);

        jackTokenizer.advance();

        vmWriter.writeLabel(failLabel);
        if (Objects.equals(jackTokenizer.keyword(), KeyWord.ELSE)) {
            jackTokenizer.advance();
            jackTokenizer.advance();
            compileStatements();
            jackTokenizer.advance();
        }

        vmWriter.writeLabel(nextLabel);
    }

    /**
     * 解析while语句
     * 'while' '(' expression ')' '{' statements '}'
     */
    public void compileWhile() throws IOException {
        int labelIndex = generateLabelIndex++;
        String whileStartLabel = String.format("%s_WHILE_START_%d", className, labelIndex);
        String nextLabel = String.format("%s_WHILE_NEXT_%d", className, labelIndex);

        jackTokenizer.advance();
        jackTokenizer.advance();

        vmWriter.writeLabel(whileStartLabel);
        compileExpression();
        compileBoolReverse();
        vmWriter.writeIf(nextLabel);

        jackTokenizer.advance();
        jackTokenizer.advance();
        compileStatements();
        vmWriter.writeGoto(whileStartLabel);
        jackTokenizer.advance();

        vmWriter.writeLabel(nextLabel);
    }

    /**
     * 解析do语句
     * 'do' subroutineCall ';'
     */
    public void compileDo() throws IOException {
        jackTokenizer.advance();
        compileSubroutineCall();
        vmWriter.writePop(Segment.TEMP, 0);
        jackTokenizer.advance();
    }

    /**
     * 解析return语句
     * 'return' expression? ';'
     *
     * @param isVoid 是否声明了返回void
     */
    public void compileReturn(boolean isVoid) throws IOException {
        jackTokenizer.advance();

        if (isVoid) {
            vmWriter.writePush(Segment.CONSTANT, 0);
        } else if (Objects.equals(jackTokenizer.keyword(), KeyWord.THIS)) {
            jackTokenizer.advance();
            vmWriter.writePush(Segment.POINTER, 0);
        } else if (!Objects.equals(jackTokenizer.symbol(), ";")) {
            compileExpression();
        }

        jackTokenizer.advance();
        vmWriter.writeReturn();
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
        compileTerm();
        while (expressionOps.contains(jackTokenizer.symbol())) {
            String symbol = jackTokenizer.symbol();
            jackTokenizer.advance();
            compileTerm();

            switch (symbol) {
                case "*":
                    vmWriter.writeCall("Math.multiply", 2);
                    break;
                case "/":
                    vmWriter.writeCall("Math.divide", 2);
                    break;
                default:
                    vmWriter.writeArithmetic(ArithmeticSymbol.getByToken(symbol));
            }
        }
    }

    /**
     * 解析表达式元素
     * integerConstant | stringConstant| keywordConstant | varName | varName '[' expression ']' | '(' expression ')' | (unaryOp term) | subroutineCall
     */
    public void compileTerm() throws IOException {
        TokenType tokenType = jackTokenizer.tokenType();
        switch (tokenType) {
            case INT:
                int intVal = jackTokenizer.intVal();
                jackTokenizer.advance();
                vmWriter.writePush(Segment.CONSTANT, intVal);
                break;
            case STR:
                char[] strCharArr = jackTokenizer.stringVal().toCharArray();
                jackTokenizer.advance();
                vmWriter.writePush(Segment.CONSTANT, strCharArr.length);
                vmWriter.writeCall("String.new", 1);
                for (char c : strCharArr) {
                    // 书中的码点是兼容ASCII的，UTF-8也兼容ASCII，可以直接用UTF-8的码点
                    vmWriter.writePush(Segment.CONSTANT, c);
                    vmWriter.writeCall("String.appendChar", 2);
                }
                break;
            case KEYWORD:
                KeyWord keyWord = jackTokenizer.keyword();
                jackTokenizer.advance();
                switch (keyWord) {
                    case TRUE:
                        vmWriter.writePush(Segment.CONSTANT, 1);
                        vmWriter.writeArithmetic(ArithmeticSymbol.NEG);
                        break;
                    case FALSE:
                    case NULL:
                        vmWriter.writePush(Segment.CONSTANT, 0);
                        break;
                    case THIS:
                        vmWriter.writePush(Segment.POINTER, 0);
                        break;
                    default:
                        throw new RuntimeException("表达式元素出现未预期的关键字：" + keyWord.name);
                }
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
                    String varName = jackTokenizer.identifier();
                    jackTokenizer.advance();
                    vmWriter.writePush(getSegmentFromSymbolTable(varName), getIndexFromSymbolTable(varName));
                    if (Objects.equals(jackTokenizer.symbol(), "[")) {
                        jackTokenizer.advance();
                        compileExpression();
                        vmWriter.writeArithmetic(ArithmeticSymbol.ADD);
                        vmWriter.writePop(Segment.POINTER, 1);
                        vmWriter.writePush(Segment.THAT, 0);
                        jackTokenizer.advance();
                    }
                }
                break;
            case SYMBOL:
                String symbol = jackTokenizer.symbol();
                if (Objects.equals(symbol, "(")) {
                    // '(' expression ')'
                    jackTokenizer.advance();
                    compileExpression();
                    jackTokenizer.advance();
                } else {
                    // (unaryOp term)
                    String unaryOp = jackTokenizer.symbol();
                    jackTokenizer.advance();
                    compileTerm();
                    if (Objects.equals("-", unaryOp)) {
                        vmWriter.writeArithmetic(ArithmeticSymbol.NEG);
                    } else if (Objects.equals("~", unaryOp)) {
                        compileBoolReverse();
                    } else {
                        throw new RuntimeException("未预期的一元操作符：" + unaryOp);
                    }
                }
                break;
        }
    }

    private void compileBoolReverse() throws IOException {
        int labelIndex = generateLabelIndex++;
        String toFalseLabel = String.format("%s_BOOL_REVERSE_TO_FALSE_%d", className, labelIndex);
        String nextLabel = String.format("%s_BOOL_REVERSE_NEXT_%d", className, labelIndex);
        vmWriter.writeIf(toFalseLabel);
        vmWriter.writePush(Segment.CONSTANT, 1);
        vmWriter.writeArithmetic(ArithmeticSymbol.NEG);
        vmWriter.writeGoto(nextLabel);
        vmWriter.writeLabel(toFalseLabel);
        vmWriter.writePush(Segment.CONSTANT, 0);
        vmWriter.writeLabel(nextLabel);
    }

    /**
     * 解析子例程调用
     * (subroutineName '(' expressionList ')') | ((className | varName) '.' subroutineName '(' expressionList ')')
     */
    private void compileSubroutineCall() throws IOException {
        String methodName;
        String funOrClassOrVarName = jackTokenizer.identifier();
        jackTokenizer.advance();
        String symbol = jackTokenizer.symbol();
        boolean isMethod = true;
        if (Objects.equals(symbol, "(")) {
            // 调用同类实例方法
            methodName = String.format("%s.%s", className, funOrClassOrVarName);
            jackTokenizer.advance();
            vmWriter.writePush(Segment.POINTER, 0);
        } else if (Objects.equals(symbol, ".")) {
            // varName是调用实例方法，className是调用静态函数
            jackTokenizer.advance();
            String methodBelongsClass = getTypeFromSymbolTable(funOrClassOrVarName);
            if (Objects.isNull(methodBelongsClass)) {
                isMethod = false;
                methodBelongsClass = funOrClassOrVarName;
            }
            methodName = String.format("%s.%s", methodBelongsClass, jackTokenizer.identifier());
            if (isMethod) {
                vmWriter.writePush(getSegmentFromSymbolTable(funOrClassOrVarName), getIndexFromSymbolTable(funOrClassOrVarName));
            }
            jackTokenizer.advance();
            jackTokenizer.advance();
        } else {
            throw new RuntimeException("试图解析子例程，但模式匹配失败：" + symbol);
        }
        int paramCount = compileExpressionList();
        if (isMethod) {
            paramCount++;
        }
        vmWriter.writeCall(methodName, paramCount);
        jackTokenizer.advance();
    }

    /**
     * 解析表达式列表
     * (expression (',' expression)*)?
     *
     * @return int 列表中表达式的数量
     */
    public int compileExpressionList() throws IOException {
        int count = 0;
        if (existsExpression()) {
            compileExpression();
            count++;
            while (Objects.equals(jackTokenizer.symbol(), ",")) {
                jackTokenizer.advance();
                compileExpression();
                count++;
            }
        }
        return count;
    }

    /**
     * 'int' | 'char' | 'boolean' | className
     */
    private String compileType() throws IOException {
        String returnVal;
        TokenType tokenType = jackTokenizer.tokenType();
        returnVal = Objects.equals(tokenType, TokenType.ID) ? jackTokenizer.identifier() : jackTokenizer.keyword().xmlEle;
        jackTokenizer.advance();
        return returnVal;
    }

    private Segment getSegmentFromSymbolTable(String varName) {
        VariableKind variableKind = methodSymbolTable.kindOf(varName);
        return (Objects.isNull(variableKind) ? classSymbolTable.kindOf(varName) : variableKind).getSegment();
    }

    private int getIndexFromSymbolTable(String varName) {
        Integer index = methodSymbolTable.indexOf(varName);
        return Objects.isNull(index) ? classSymbolTable.indexOf(varName) : index;
    }

    private String getTypeFromSymbolTable(String varName) {
        String type = methodSymbolTable.typeOf(varName);
        return Objects.isNull(type) ? classSymbolTable.typeOf(varName) : type;
    }

}
