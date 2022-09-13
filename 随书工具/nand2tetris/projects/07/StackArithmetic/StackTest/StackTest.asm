	// RAM[SP++] = 17
	@17
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 17
	@17
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
	// RAM[SP] = (RAM[--SP] -= D) == 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_1
	D;JEQ
	@SP
	A=M
	M=0
	@COMPARE_AFTER_1
	0;JMP
(COMPARE_TRUE_1)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_1)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 17
	@17
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 16
	@16
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
	// RAM[SP] = (RAM[--SP] -= D) == 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_2
	D;JEQ
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
	// RAM[SP++] = 16
	@16
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 17
	@17
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
	// RAM[SP] = (RAM[--SP] -= D) == 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_3
	D;JEQ
	@SP
	A=M
	M=0
	@COMPARE_AFTER_3
	0;JMP
(COMPARE_TRUE_3)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_3)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 892
	@892
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 891
	@891
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
	@COMPARE_TRUE_4
	D;JLT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_4
	0;JMP
(COMPARE_TRUE_4)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_4)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 891
	@891
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 892
	@892
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
	@COMPARE_TRUE_5
	D;JLT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_5
	0;JMP
(COMPARE_TRUE_5)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_5)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 891
	@891
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 891
	@891
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
	@COMPARE_TRUE_6
	D;JLT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_6
	0;JMP
(COMPARE_TRUE_6)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_6)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 32767
	@32767
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 32766
	@32766
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
	// RAM[SP] = (RAM[--SP] -= D) > 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_7
	D;JGT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_7
	0;JMP
(COMPARE_TRUE_7)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_7)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 32766
	@32766
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 32767
	@32767
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
	// RAM[SP] = (RAM[--SP] -= D) > 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_8
	D;JGT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_8
	0;JMP
(COMPARE_TRUE_8)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_8)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 32766
	@32766
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 32766
	@32766
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
	// RAM[SP] = (RAM[--SP] -= D) > 0 ? -1 : 0
	@SP
	M=M-1
	A=M
	D=M-D
	@COMPARE_TRUE_9
	D;JGT
	@SP
	A=M
	M=0
	@COMPARE_AFTER_9
	0;JMP
(COMPARE_TRUE_9)
	@SP
	A=M
	M=-1
(COMPARE_AFTER_9)
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 57
	@57
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 31
	@31
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	// RAM[SP++] = 53
	@53
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
	// RAM[SP++] = 112
	@112
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
	// RAM[SP-1] = !RAM[SP-1]
	@SP
	A=M-1
	M=-M
	// D = RAM[--SP]
	@SP
	M=M-1
	A=M
	D=M
	// RAM[--SP] &= D
	@SP
	M=M-1
	A=M
	M=M&D
	// SP++
	@SP
	M=M+1
	// RAM[SP++] = 82
	@82
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
	// RAM[--SP] |= D
	@SP
	M=M-1
	A=M
	M=M|D
	// SP++
	@SP
	M=M+1
	// RAM[SP-1] = !RAM[SP-1]
	@SP
	A=M-1
	M=!M
