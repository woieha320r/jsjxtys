	// RAM[SP++] = 0
	@0
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[local + 0] = RAM[--SP]
	@LCL
	D=M
	@0
	D=D+A
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
(LOOP_START)
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
	// RAM[local + 0] = RAM[--SP]
	@LCL
	D=M
	@0
	D=D+A
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
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
	// RAM[SP++] = 1
	@1
	D=A
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
	// RAM[argument + 0] = RAM[--SP]
	@ARG
	D=M
	@0
	D=D+A
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
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
	// if (RAM[--SP] != 0) goto LOOP_START
	@SP
	M=M-1
	A=M
	D=M
	@NOT_GOTO_1
	D;JEQ
	@LOOP_START
	0;JMP
(NOT_GOTO_1)
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
