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

(BEGIN)
    @8191
    D=A
    @R0
    M=D
//check key board content
    @24576
    D=M

//jump to black portion if D is zero
    @BLACK
    D, JEQ

//make everything white
(WHITE)
    @SCREEN
    D=A
    @R0
    D=D+M
    A=D
    M=-1
    //decrement RO
    @R0
    M=M-1
    @WHITE
    M;JGE
    //back to beginning
    @BEGIN
    0;JMP

(BLACK)
    //make everything black
    @SCREEN
    D=A
    @R0
    D=D+M
    A=D
    M=0
    //decrement RO
    @R0
    M=M-1
    @WHITE
    M;JGE
    //back to beginning
    @BEGIN
    0;JMP
    
    //go back to the beginning
    @BEGIN
    0;JMP
