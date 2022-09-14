# nand2tetris
```text
《计算机系统要素——从零开始构建现代计算机》学习记录
如下部分参考自：https://github.com/woai3c/nand2tetris
· PC
· Memory
```

### 要点
```text
· 把复杂问题分解为可管理的模块，当使用下一层的模块时，专注于抽象而忽略其实现细节。
· 多位二进制的表达顺序是[n]～[0]，不是[0]~[n]。
· 多注意二进制串的规律
· HDL语法的=代表线路连接。
· HDL语法可以直接置入多位0、1，并且内部节点不能直接使用子总线，需要先输出，下面是个综合的例子，用于判断in中是否含有有效位1。
    // 描述：if in[16] contains 1; then hasTrue=1; fi
    Or16(a=in, b[0..15]=false, out[0..7]=lowIn, out[8..15]=highIn);
    Or8Way(in=lowIn, out=lowHasTrue);
    Or8Way(in=highIn, out=highHasTrue);
    Or(a=lowHasTrue, b=highHasTrue, out=out);
    // 当有两个地方同时需要结果值时，可以被指定多次：Or(a=true, b=false, out=waitOtherOpt, out=out);
```