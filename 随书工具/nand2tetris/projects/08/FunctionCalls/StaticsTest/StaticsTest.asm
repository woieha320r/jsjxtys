	// system boot start
	@256
	D=A
	@SP
	M=D
	// call start
	// RAM[SP++] = RETURN_ADDRESS_1
	@RETURN_ADDRESS_1
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = LCL
	@LCL
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = ARG
	@ARG
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THIS
	@THIS
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THAT
	@THAT
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// ARG = SP-nFrame-nArgs
	@5
	D=A
	@SP
	D=M-D
	@0
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Sys.init
	0;JMP
(RETURN_ADDRESS_1)
	// call end
(SYSTEM_EXECUTION_END)
	@SYSTEM_EXECUTION_END
	0;JMP
	// system boot end
(Class2.set)
	// function start
	// function end
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
	// Class2.0 = RAM[--SP]
	@Class2.0
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
	// Class2.1 = RAM[--SP]
	@Class2.1
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
(Class2.get)
	// function start
	// function end
	// RAM[SP++] = Class2.0
	@Class2.0
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = Class2.1
	@Class2.1
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
(Class1.set)
	// function start
	// function end
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
	// Class1.0 = RAM[--SP]
	@Class1.0
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
	// Class1.1 = RAM[--SP]
	@Class1.1
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
(Class1.get)
	// function start
	// function end
	// RAM[SP++] = Class1.0
	@Class1.0
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = Class1.1
	@Class1.1
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
(Sys.init)
	// function start
	// function end
	// RAM[SP++] = 6
	@6
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
	// call start
	// RAM[SP++] = RETURN_ADDRESS_2
	@RETURN_ADDRESS_2
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = LCL
	@LCL
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = ARG
	@ARG
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THIS
	@THIS
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THAT
	@THAT
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// ARG = SP-nFrame-nArgs
	@5
	D=A
	@SP
	D=M-D
	@2
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Class1.set
	0;JMP
(RETURN_ADDRESS_2)
	// call end
	// RAM[5+0] = RAM[--SP]
	@5
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
	// RAM[SP++] = 23
	@23
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 15
	@15
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// call start
	// RAM[SP++] = RETURN_ADDRESS_3
	@RETURN_ADDRESS_3
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = LCL
	@LCL
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = ARG
	@ARG
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THIS
	@THIS
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THAT
	@THAT
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// ARG = SP-nFrame-nArgs
	@5
	D=A
	@SP
	D=M-D
	@2
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Class2.set
	0;JMP
(RETURN_ADDRESS_3)
	// call end
	// RAM[5+0] = RAM[--SP]
	@5
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
	// call start
	// RAM[SP++] = RETURN_ADDRESS_4
	@RETURN_ADDRESS_4
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = LCL
	@LCL
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = ARG
	@ARG
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THIS
	@THIS
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THAT
	@THAT
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// ARG = SP-nFrame-nArgs
	@5
	D=A
	@SP
	D=M-D
	@0
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Class1.get
	0;JMP
(RETURN_ADDRESS_4)
	// call end
	// call start
	// RAM[SP++] = RETURN_ADDRESS_5
	@RETURN_ADDRESS_5
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = LCL
	@LCL
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = ARG
	@ARG
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THIS
	@THIS
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = THAT
	@THAT
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// ARG = SP-nFrame-nArgs
	@5
	D=A
	@SP
	D=M-D
	@0
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Class2.get
	0;JMP
(RETURN_ADDRESS_5)
	// call end
(WHILE)
	@WHILE
	0;JMP
