(SimpleFunction.test)
	// function start
	// RAM[SP++] = 0
	@SP
	A=M
	M=0
	@SP
	M=M+1
	// RAM[SP++] = 0
	@SP
	A=M
	M=0
	@SP
	M=M+1
	// function end
	// RAM[SP++] = RAM[local + 0]
	@0
	D=A
	@LCL
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = RAM[local + 1]
	@1
	D=A
	@LCL
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// D = RAM[--SP]
	@SP
	M=M-1
	A=M
	D=M
	// RAM[--SP] += D
	@SP
	M=M-1
	A=M
	M=M+D
	// SP++
	@SP
	M=M+1
	// RAM[SP-1] = !RAM[SP-1]
	@SP
	A=M-1
	M=!M
	// RAM[SP++] = RAM[argument + 0]
	@0
	D=A
	@ARG
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// D = RAM[--SP]
	@SP
	M=M-1
	A=M
	D=M
	// RAM[--SP] += D
	@SP
	M=M-1
	A=M
	M=M+D
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = RAM[argument + 1]
	@1
	D=A
	@ARG
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// D = RAM[--SP]
	@SP
	M=M-1
	A=M
	D=M
	// RAM[--SP] -= D
	@SP
	M=M-1
	A=M
	M=M-D
	// SP++
	@SP
	M=M+1
	// return start
	// R14 = RAM[LCL-5]
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R14
	M=D
	// RAM[ARG] = RAM[--SP]
	@SP
	M=M-1
	A=M
	D=M
	@ARG
	A=M
	M=D
	// SP = ARG+1
	D=A
	@SP
	M=D+1
	// THAT = RAM[LCL-1]
	@1
	D=A
	@LCL
	A=M-D
	D=M
	@THAT
	M=D
	// THIS = RAM[LCL-2]
	@2
	D=A
	@LCL
	A=M-D
	D=M
	@THIS
	M=D
	// ARG = RAM[LCL-3]
	@3
	D=A
	@LCL
	A=M-D
	D=M
	@ARG
	M=D
	// LCL = RAM[LCL-4]
	@4
	D=A
	@LCL
	A=M-D
	D=M
	@LCL
	M=D
	@R14
	A=M
	0;JMP
	// return end
