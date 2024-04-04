# nand2tetris

《计算机系统要素：从零开始构建现代计算机（第2版）》学习记录

原名《The Elements of Computing Systems》

<img src="./img/readme/乒乓游戏运行效果.gif" alt="乒乓游戏运行效果" width="500">
<br>

### 概述

```text
与非门 -> 与、或、非、抑或 -> 选择器、分解器
      ↓
    半加器 -> 全加器 -> 自增、算术逻辑单元
          ↓ + DFF
        寄存器 -> 随机访问内存、程序计数器
              ↓ + 机器指令设计
         中央控制单元、内存 -> 计算机整体架构
                         ↓ + 任一门语言
                       汇编器
                       
                       汇编
                        ↑ + 任一门语言实现Jack VM翻译器
                  Jack VM中间代码
                        ↑ + Jack标准库 + 任一门语言实现Jack编译器
                        ↑       ↑
                     Jack 源码 -> + 算法
```

### 要点

* 把复杂问题按层次划分，当使用下层时，专注于抽象而忽略其实现细节
* 多位二进制的表达顺序是[n]~[0]，而不是[0]~[n]
* 处理二进制时多注意观察规律
* 书中的所有语法都是自创简化版，以HDL为例
  * 其"等号="代表线路连接而非赋值，固定给予高/低位需要使用true/false而不是1/0
  * 一个上游输出端可同时连接到多个下游输入端
    * 如：Or(a=x, b=y, out=out, out=toOtherChip);
  * 连入输入端的总线须与输入端位数对应
    * 不可：Or(a=x[0], b=y[1], out=out);
  * 一个综合示例，用于判断in中是否包含有效位1

```text
    // 描述：if in[16] contains 1; then hasTrue=1; fi
    Or16(a=in, b[0..15]=false, out[0..7]=lowIn, out[8..15]=highIn);
    Or8Way(in=lowIn, out=lowHasTrue);
    Or8Way(in=highIn, out=highHasTrue);
    Or(a=lowHasTrue, b=highHasTrue, out=out);
```
