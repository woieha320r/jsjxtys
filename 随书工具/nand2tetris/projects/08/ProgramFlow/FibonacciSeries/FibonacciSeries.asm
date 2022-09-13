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
	// R4 = RAM[--SP]
	@R4
	D=A
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
	// RAM[SP++] = 0
	@0
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[that + 0] = RAM[--SP]
	@THAT
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
	// RAM[SP++] = 1
	@1
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[that + 1] = RAM[--SP]
	@THAT
	D=M
	@1
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
	// RAM[SP++] = 2
	@2
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
(MAIN_LOOP_START)
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
	// if (RAM[--SP] != 0) goto COMPUTE_ELEMENT
	@SP
	M=M-1
	A=M
	D=M
	@NOT_GOTO_1
	D;JEQ
	@COMPUTE_ELEMENT
	0;JMP
(NOT_GOTO_1)
	@END_PROGRAM
	0;JMP
(COMPUTE_ELEMENT)
	// RAM[SP++] = RAM[that + 0]
	@0
	D=A
	@THAT
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = RAM[that + 1]
	@1
	D=A
	@THAT
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
	// RAM[that + 2] = RAM[--SP]
	@THAT
	D=M
	@2
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
	// RAM[SP++] = RAM[4]
	@R4
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
	// RAM[--SP] += D
	@SP
	M=M-1
	A=M
	M=M+D
	// SP++
	@SP
	M=M+1
	// R4 = RAM[--SP]
	@R4
	D=A
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
	@MAIN_LOOP_START
	0;JMP
(END_PROGRAM)
