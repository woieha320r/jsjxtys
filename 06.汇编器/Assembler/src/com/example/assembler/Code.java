package com.example.assembler;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 汇编器模块：代码
 * 将解析器拆解的结果解析为二进制代码
 */
public class Code {

    private final Map<String, String> compMap;

    public Code() {
        compMap = new HashMap<>(18);
        compMap.put("0", "0101010");
        compMap.put("1", "0111111");
        compMap.put("-1", "0111010");
        compMap.put("D", "0001100");
        compMap.put("A", "0110000");
        compMap.put("M", "1110000");
        compMap.put("!D", "0001101");
        compMap.put("!A", "0110001");
        compMap.put("!M", "1110001");
        compMap.put("-D", "0001111");
        compMap.put("-A", "0110011");
        compMap.put("-M", "1110011");
        compMap.put("D+1", "0011111");
        compMap.put("A+1", "0110111");
        compMap.put("M+1", "1110111");
        compMap.put("D-1", "0001110");
        compMap.put("A-1", "0110010");
        compMap.put("M-1", "1110010");
        compMap.put("D+A", "0000010");
        compMap.put("D+M", "1000010");
        compMap.put("D-A", "0010011");
        compMap.put("D-M", "1010011");
        compMap.put("A-D", "0000111");
        compMap.put("M-D", "1000111");
        compMap.put("D&A", "0000000");
        compMap.put("D&M", "1000000");
        compMap.put("D|A", "0010101");
        compMap.put("D|M", "1010101");
    }

    /**
     * 将解析器拆解出的dest转为二进制串
     *
     * @param dest 解析器拆解出的dest字符串
     * @return String 二进制串
     */
    public String dest(String dest) {
        dest = Objects.isNull(dest) ? "" : dest;
        String destBinStr = "";
        destBinStr += dest.contains("A") ? "1" : "0";
        destBinStr += dest.contains("D") ? "1" : "0";
        destBinStr += dest.contains("M") ? "1" : "0";
        return destBinStr;
    }

    /**
     * 将解析器拆解出的comp转为二进制串
     * TODO:看是否有什么规律可以像dest和jump一样实现而不用查找键值对，比如把单数填充成双数
     *
     * @param comp 解析器拆解出的comp字符串
     * @return String 二进制串
     */
    public String comp(@NotNull String comp) {
        if (Objects.isNull(comp)) {
            throw new RuntimeException("语法错误：comp不可为null");
        }
        return compMap.get(comp.replace(" ", ""));
    }

    /**
     * 将解析器拆解出的jump转为二进制串
     *
     * @param jump 解析器拆解出的jump字符串
     * @return String 二进制串
     */
    public String jump(String jump) {
        jump = Objects.isNull(jump) ? "" : jump;
        // != -> < && >、无条件 -> < && > && =
        jump = jump.replace("NE", "LG").replace("JMP", "LGE");
        String jumpBinStr = "";
        jumpBinStr += jump.contains("L") ? "1" : "0";
        jumpBinStr += jump.contains("E") ? "1" : "0";
        jumpBinStr += jump.contains("G") ? "1" : "0";
        return jumpBinStr;
    }

    // 测试
    public static void main(String[] args) {
        Code code = new Code();
        System.out.println("\ndest\n============================================");
        Stream.of(null, "M", "D", "DM", "A", "AM", "AD", "ADM").forEachOrdered(
                destStr -> System.out.println(code.dest(destStr))
        );
        System.out.println("\njump\n============================================");
        Stream.of(null, "JGT", "JEQ", "JGE", "JLT", "JNE", "JLE", "JMP").forEachOrdered(
                jumpStr -> System.out.println(code.jump(jumpStr))
        );
    }

}
