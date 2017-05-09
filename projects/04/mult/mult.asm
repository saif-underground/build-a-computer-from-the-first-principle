// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
@R2
M=0

(COMPUTE)
//if r0 ==0 break
@R0
D=M
@END
D;JEQ

//R2 = R2+R1    
@R1
D=M
@R2
D=D+M

//store R2
@R2
M=D

//decrease R0
@R0
M=M-1

//jump to start
@COMPUTE
0;JMP

//Infinite loop
(End)
@End
0;JMP
