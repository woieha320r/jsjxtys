	// RAM[SP++] = 3030
	@3030
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// R3 = RAM[--SP]
	@R3
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
	// RAM[SP++] = 3040
	@3040
	D=A
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
	// RAM[SP++] = 32
	@32
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[this + 2] = RAM[--SP]
	@THIS
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
	// RAM[SP++] = 46
	@46
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[that + 6] = RAM[--SP]
	@THAT
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
	// RAM[SP++] = RAM[3]
	@R3
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = RAM[4]
	@R4
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
	// RAM[SP++] = RAM[this + 2]
	@2
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
	// RAM[--SP] -= D
	@SP
	M=M-1
	A=M
	M=M-D
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = RAM[that + 6]
	@6
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
