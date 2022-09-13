	// RAM[SP++] = 7
	@7
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 8
	@8
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
