// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.
	// if (RAM[0] > RAM[1]) {
	// 	count = RAM[1];
	// 	num = RAM[0];
	// } else {
	// 	count = RAM[0];
	// 	num = RAM[1];
	// }
	// res = 0;
	// count--;
	// for (i = 0; i <= count; i++) {
	// 	res += num;
	// }
	@R0
	D=M
	@R1
	D=D-M
	@R0_BIGGER
	D;JGT

	// r0 <= r1
	@R0
	D=M
	@count
	M=D
	@R1
	D=M
	@num
	M=D
(CORE)
	// res = 0
	@0
	D=A
	@res
	M=D
	// n--
	@1
	D=A
	@count
	M=M-D
	// i = 0
	@0
	D=A
	@i
	M=D
(LOOP)
	@i
	D=M
	@count
	D=D-M
	@STORE_RESULT
	D;JGT
	// res += num
	@num
	D=M
	@res
	M=D+M
	// i++
	@1
	D=A
	@i
	M=D+M
	@LOOP
	0;JMP

// r0 > r1
(R0_BIGGER)
	@R1
	D=M
	@count
	M=D
	@R0
	D=M
	@num
	M=D
	@CORE
	0;JMP

(STORE_RESULT)
	@res
	D=M
	@R2
	M=D
(END)
	@END
	0;JMP