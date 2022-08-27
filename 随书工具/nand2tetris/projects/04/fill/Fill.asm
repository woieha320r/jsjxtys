// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

	// while (1) {
	// 	color = RAM[KBD] > 0 ? -1 : 0;
	// 	屏幕256行，每行512像素，这里只弄前524个像素，要不没完了，太慢太慢
	// 	// screen_max_address = SCREEN + 256*512/16;
	// 	screen_max_address = SCREEN + 2*512/16;
	// 	for (i = SCREEN; i < screen_max_address; i++) {
	// 		RAM[i] = color;
	// 	}
	// }

	// @8192
	@64
	D=A
	@SCREEN
	D=D+A
	@screen_max_address
	M=D

(LOOP_OUT)
	// 默认填充白色，默认0以便黑色时直接-1
	@0
	D=A
	@color
	M=D
	// 是否要填充黑色
	@KBD
	D=M
	@BLACK_VALUE
	D;JGT
(CORE)
	@SCREEN
	D=A
	@i
	M=D
(LOOP_IN)
	@i
	D=M
	@screen_max_address
	D=D-M
	@LOOP_OUT
	D;JGE
	@color
	D=M
	@i
	A=M
	M=D
	@i
	M=M+1
	@LOOP_IN
	0;JMP

(BLACK_VALUE)
	@color
	M=M-1
	@CORE
	0;JMP