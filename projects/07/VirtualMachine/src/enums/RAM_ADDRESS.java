/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 *
 * @author Saif
 */
public enum RAM_ADDRESS {
    //virtual register segment 0..15
    SP(0, 0),
    LCL(1, 1),
    ARG(2, 2),
    THIS(3, 3),
    THAT(4, 4),
    R0(0, 0),
    R1(1, 1),
    R2(2, 2),
    R3(3, 3),
    R4(4, 4),
    R5(5, 5),
    R6(6, 6),
    R7(7, 7),
    R8(8, 8),
    R9(9, 9),
    R10(10, 10),
    R11(11, 11),
    R12(12, 12),
    R13(13, 13),
    R14(14, 14),
    R15(15, 15),
    //static segment 16-155
    STATIC(16, 155),
    //stack 256..2047
    STACK(256, 2047),
    //heap 2048..16383
    HEAP(2048, 16383),
    //memory_mapped_io 16384..24575
    MEMORY_MAPPED_IO(16384, 24575);
    private int begin, end;
    RAM_ADDRESS(int begin, int end){
        this.begin = begin;
        this.end = end;
    }
    public int getBegin(){
        return begin;
    }
    
    public int getEnd(){
        return end;
    }
}
