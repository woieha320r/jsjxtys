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
(Main.fibonacci)
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
	// RAM[SP] = (RAM[--SP] -= D) < 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_2
	D;JLT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_2
	0;JMP
(COMPARE_TRUE_2)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_2)
	// SP++
	@SP
	M=M+1
	// if (RAM[--SP] != 0) goto IF_TRUE
	@SP
	M=M-1
	A=M
	D=M
	@NOT_GOTO_3
	D;JEQ
	@IF_TRUE
	0;JMP
(NOT_GOTO_3)
	@IF_FALSE
	0;JMP
(IF_TRUE)
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
(IF_FALSE)
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
	@1
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Main.fibonacci
	0;JMP
(RETURN_ADDRESS_4)
	// call end
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
	@1
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Main.fibonacci
	0;JMP
(RETURN_ADDRESS_5)
	// call end
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
	// RAM[SP++] = 4
	@4
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// call start
	// RAM[SP++] = RETURN_ADDRESS_6
	@RETURN_ADDRESS_6
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
	@1
	D=D-A
	@ARG
	M=D
	// LCL = SP
	@SP
	D=M
	@LCL
	M=D
	@Main.fibonacci
	0;JMP
(RETURN_ADDRESS_6)
	// call end
(WHILE)
	@WHILE
	0;JMP
