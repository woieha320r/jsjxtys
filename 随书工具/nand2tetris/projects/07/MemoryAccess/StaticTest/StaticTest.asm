	// RAM[SP++] = 111
	@111
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 333
	@333
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 888
	@888
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// StaticTest.8 = RAM[--SP]
	@StaticTest.8
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
	// StaticTest.3 = RAM[--SP]
	@StaticTest.3
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
	// StaticTest.1 = RAM[--SP]
	@StaticTest.1
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
	// RAM[SP++] = StaticTest.3
	@StaticTest.3
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = StaticTest.1
	@StaticTest.1
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
	// RAM[SP++] = StaticTest.8
	@StaticTest.8
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
