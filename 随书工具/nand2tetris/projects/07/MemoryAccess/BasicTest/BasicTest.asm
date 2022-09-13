	// RAM[SP++] = 10
	@10
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
	// RAM[SP++] = 21
	@21
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 22
	@22
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[argument + 2] = RAM[--SP]
	@ARG
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
	// RAM[argument + 1] = RAM[--SP]
	@ARG
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
	// RAM[SP++] = 36
	@36
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[this + 6] = RAM[--SP]
	@THIS
	D=M
	@6
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
	// RAM[SP++] = 42
	@42
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 45
	@45
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[that + 5] = RAM[--SP]
	@THAT
	D=M
	@5
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
	// RAM[SP++] = 510
	@510
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[5+6] = RAM[--SP]
	@5
	D=A
	@6
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
	// RAM[SP++] = RAM[that + 5]
	@5
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
	// RAM[SP++] = RAM[this + 6]
	@6
	D=A
	@THIS
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = RAM[this + 6]
	@6
	D=A
	@THIS
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
	// RAM[SP++] = RAM[5+6]
	@5
	D=A
	@6
	A=D+A
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
