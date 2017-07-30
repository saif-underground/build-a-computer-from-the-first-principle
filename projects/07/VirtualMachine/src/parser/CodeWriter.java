/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import enums.COMMAND_TYPE;
import enums.KEY_WORDS;
import enums.RAM_ADDRESS;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Saif
 */
public class CodeWriter {

    private static String NL = "\n";
    private static int labelCount = 0;
    String fileName;
    String output = "";
    FileWriter fileWriter = null;

    public CodeWriter(String fileName) throws IOException {
        setFileName(fileName);
        fileWriter = new FileWriter(fileName);
        writeBootStrapCode();
    }

    public void setFileName(String fileName) throws IOException {
        this.fileName = fileName;
    }

    //EVENTUALLY REMOVE THIS METHOD AND CALL SYS.INIT INSTEAD
    private void writeBootStrapCode() throws IOException {

        String code = "";
        //SP=256
        //code = "@" + RAM_ADDRESS.STACK.getBegin() + "\n";
        //code += "D=A\n"
        //        + "@SP\n"
        //        + "M=D\n";
        code += RegEqVal("SP", "256");
        
        //LCL=256
        code += RegEqVal("LCL", "256");
        
        //ARG=251
        code += RegEqVal("ARG", "251");
        
        //THIS=3000
        code += RegEqVal("THIS", "3000");
        
        //THAT=4000
        code += RegEqVal("THAT", "4000");
        
        //code += "@Sys.init\n";
        //code += "0;JMP\n";
        fileWriter.write(code);
        writeCall("Sys.init", 0);
    }

    public void writeArithmatic(String command) throws IOException {
        String code = null;

        if (command.equalsIgnoreCase(KEY_WORDS.ADD.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.SUB.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.OR.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.AND.getKeyWord())) {
            String operator;
            if (command.equalsIgnoreCase(KEY_WORDS.ADD.getKeyWord())) {
                operator = "+";
            } else if (command.equalsIgnoreCase(KEY_WORDS.SUB.getKeyWord())) {
                operator = "-";
            } else if (command.equalsIgnoreCase(KEY_WORDS.AND.getKeyWord())) {
                operator = "&";
            } else {
                operator = "|";
            }
            code = "@SP\n"
                    + "A=M-1\n"
                    + "D=M\n"
                    + "A=A-1\n"
                    + "M=M" + operator + "D\n"
                    + "D=A+1\n"
                    + "@SP\n"
                    + "M=D\n";
        }

        if (command.equalsIgnoreCase(KEY_WORDS.EQ.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.GT.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.LT.getKeyWord())) {
            String jmpMnemonic = "JGT";
            if (command.equalsIgnoreCase(KEY_WORDS.EQ.getKeyWord())) {
                jmpMnemonic = "JEQ";
            } else if (command.equalsIgnoreCase(KEY_WORDS.LT.getKeyWord())) {
                jmpMnemonic = "JLT";
            }
            String label = getLabel();
            code = "@SP\n"
                    + "M=M-1\n"
                    + "A=M\n"
                    + "D=M\n"
                    + "A=A-1\n"
                    + "D=M-D\n"
                    + "M=-1\n"
                    + "@" + label + "\n"
                    + "D;" + jmpMnemonic + "\n"
                    + "@SP\n"
                    + "A=M-1\n"
                    + "M=0\n"
                    + "(" + label + ")\n";
        }
        
        if (command.equalsIgnoreCase(KEY_WORDS.NEG.getKeyWord())
                || command.equalsIgnoreCase(KEY_WORDS.NOT.getKeyWord())) {
            String operator ;
            if (command.equalsIgnoreCase(KEY_WORDS.NEG.getKeyWord())) {
                operator = "-";
            } else {
                operator = "!";
            }
            code = "@SP\n"
                    + "A=M-1\n"
                    + "D=" + operator+ "M\n"
                    + "M=D\n";
        }

        fileWriter.write(code);
        fileWriter.flush();
    }

    public void writePushPop(COMMAND_TYPE cmdType, String segment, String index) throws IOException {
        String code = "";
        if (cmdType == COMMAND_TYPE.C_PUSH) {
            if (segment.equalsIgnoreCase(KEY_WORDS.CONSTANT.getKeyWord())) {
                code = "@" + index + "\n"
                        + "D=A\n"
                        + "@SP\n"
                        + "A=M\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "M=M+1\n";
            } else if (segment.equalsIgnoreCase(KEY_WORDS.LOCAL.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.ARGUMENT.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.THIS.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.THAT.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);

                code = "@" + index + "\n"
                        + "D=A\n"
                        + "@" + ramAddr + "\n"
                        + "A=D+M\n"
                        + "D=M\n"
                        + "@SP" + "\n"
                        + "A=M\n"
                        + "M=D\n"
                        // + "@" + ramAddr + "\n"
                        + "@SP" + "\n"
                        + "M=M+1\n";

            } else if (segment.equalsIgnoreCase(KEY_WORDS.POINTER.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);
                code = "@" + ramAddr + "\n"
                        + "D=M\n"
                        + "@SP\n"
                        + "A=M\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "M=M+1\n";
            } else if (segment.equalsIgnoreCase(KEY_WORDS.TEMP.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.STATIC.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);
                code = "@" + ramAddr + "\n"
                        + "D=M\n"
                        + "@SP\n"
                        + "A=M\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "M=M+1\n";
            }

        } else //POP
        {
            if (segment.equalsIgnoreCase(KEY_WORDS.LOCAL.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.ARGUMENT.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.THIS.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.THAT.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);

                code = "@" + index + "\n"
                        + "D=A\n"
                        + "@" + ramAddr + "\n"
                        + "D=D+M\n"
                        + "M=D\n" //ramAddr = ramAddr + OFFSET

                        + "@SP" + "\n"
                        + "M=M-1\n"
                        + "A=M\n"
                        + "D=M\n" //D=STACK[SP--]

                        + "@" + ramAddr + "\n"
                        + "A=M\n"
                        + "M=D\n" //store data to LCL + offset

                        + "@" + index + "\n"
                        + "D=A\n"
                        + "@" + ramAddr + "\n"
                        + "D=M-D\n"
                        + "M=D\n" //ramAddr = ramAddr - OFFSET; PUTTING POINTER
                        ;
            } else if (segment.equalsIgnoreCase(KEY_WORDS.POINTER.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);
                code = "@SP\n"
                        + "M=M-1\n"
                        + "A=M\n"
                        + "D=M\n"
                        + "@" + ramAddr + "\n"
                        + "M=D\n";
            } else if (segment.equalsIgnoreCase(KEY_WORDS.TEMP.getKeyWord())
                    || segment.equalsIgnoreCase(KEY_WORDS.STATIC.getKeyWord())) {
                String ramAddr = getRamAddress(segment, index);
                code = "@SP\n"
                        + "M=M-1\n"
                        + "A=M\n"
                        + "D=M\n"
                        + "@" + ramAddr + "\n"
                        + "M=D\n";
            }
        }

        fileWriter.write(code);
        fileWriter.flush();

    }

    private String getRamAddress(String segment, String index) {
        String ramAddr = null;
        if (segment.equalsIgnoreCase(KEY_WORDS.LOCAL.getKeyWord())) {
            ramAddr = "LCL";
        } else if (segment.equalsIgnoreCase(KEY_WORDS.ARGUMENT.getKeyWord())) {
            ramAddr = "ARG";
        } else if (segment.equalsIgnoreCase(KEY_WORDS.POINTER.getKeyWord())) {
            ramAddr = "THAT";
            if (index.equalsIgnoreCase("0")) {
                ramAddr = "THIS";
            }
        } else if (segment.equalsIgnoreCase(KEY_WORDS.TEMP.getKeyWord())) {
            int offset = Integer.parseInt(index);
            if (offset >= 0 && offset < 16) {
                ramAddr = "R" + index;
            }
        } else if (segment.equalsIgnoreCase(KEY_WORDS.STATIC.getKeyWord())) {
            int offset = Integer.parseInt(index);
            if (offset >= 0 && offset <= (RAM_ADDRESS.STATIC.getEnd() - RAM_ADDRESS.STATIC.getBegin())) {
                ramAddr = "" + (RAM_ADDRESS.STATIC.getBegin() + offset);
            }
        } else if (segment.equalsIgnoreCase(KEY_WORDS.THAT.getKeyWord())) {
            ramAddr = "THAT";
        } else if (segment.equalsIgnoreCase(KEY_WORDS.THIS.getKeyWord())) {
            ramAddr = "THIS";
        }

        if (ramAddr == null) {
            throw new RuntimeException("RAM Address is null in getRamAddress method");
        }
        return ramAddr;
    }

    private String getLabel() {
        labelCount++;
        return "LABEL_" + labelCount;
    }

    public void writeLabel(String label) throws IOException {
        String code = "(" + label + ")\n";
        writeCode(code);
    }

    public void writeGoto(String label) throws IOException {
        String code = "@" + label + NL
                + "0;JMP" + NL;
        writeCode(code);

    }

    public void writeIf(String labelName) throws IOException {
        //pop stack and put value to D
        String code = "@SP" + NL
                + "M=M-1" + NL
                + "A=M" + NL
                + "D=M" + NL
                + "@" + labelName + NL
                + "D;JNE" + NL;
        writeCode(code);
    }

    public void writeCall(String functionName, int numArgs) throws IOException {
        String returnAddr = getLabel();
        String code = pushVal(returnAddr);
        code += pushReg("LCL");
        code += pushReg("ARG");
        code += pushReg("THIS");
        code += pushReg("THAT");
        //ARG=SP-n-5
        code += DEqReg("SP");
        code += DeqOpVal(Integer.toString(numArgs), "-");
        code += DeqOpVal("5", "-");
        code += RegEqD("ARG");
        //LCL=SP
        code += DEqReg("SP");
        code += RegEqD("LCL");
        //goto functionName
        code += gotoAddr(functionName);
        code += label(returnAddr);
        
        writeCode(code);
    }

    public void writeReturn() throws IOException {
        String code = "";
        String FRAME = "R13"; //temp register to hold frame
        String RET = "R14";
        //FRAME=LCL
        code += DEqReg("LCL");
        code += RegEqD(FRAME);
        //*ARG=pop()
        code += popToD();
        code += contentRegEqD("ARG");
        //SP=ARG+1
        code += DEqReg("ARG");
        code += DeqOpVal("1", "+");
        code += RegEqD("SP");
        //THAT=*(FRAME-1)
        code += RegEqContentRegOpVal("THAT", FRAME, "-", "1");
        //THIS=*(FRAME-2)
        code += RegEqContentRegOpVal("THIS", FRAME, "-", "2");
        //ARG=*(FRAME-3)
        code += RegEqContentRegOpVal("ARG", FRAME, "-", "3");
        //LCL=*(FRAME-4)
        code += RegEqContentRegOpVal("LCL", FRAME, "-", "4");
        //RET=*(FRAME-5)
        code += RegEqContentRegOpVal(RET, FRAME, "-", "5");
        //goto RET
        code += gotoReg(RET);
        writeCode(code);
    }

    public void writeFunction(String functionName, int numLocals) throws IOException {
        String code = label(functionName);
        for(int i = 0; i < numLocals; i++){
            code += pushVal("0");
        }
        writeCode(code);
    }

    private void writeCode(String code) throws IOException {
        fileWriter.write(code);
        fileWriter.flush();
    }

    /////Helper methods
    private String DEqVal(String val) {
        String code = "@" + val + NL;
        code += "D=A" + NL;
        return code;
    }

    private String DEqReg(String reg) {
        String code = "@" + reg + NL;
        code += "D=M" + NL;
        return code;
    }

    
    private String pushD() {
        String code = "@SP" + NL;
        code += "A=M" + NL;
        code += "M=D" + NL;
        code += "@SP" + NL;
        code += "M=M+1" + NL;
        return code;
    }

    private String popToD() {
        String code = "@SP" + NL;
        code += "M=M-1" + NL;
        code += "A=M" + NL;
        code += "D=M" + NL;
        return code;
    }

    private String RegEqD(String reg) {
        String code = "@" + reg + NL;
        code += "M=D" + NL;
        return code;
    }
    
    private String RegEqVal(String reg, String val){
        String code = "";
        code += "@" + val + NL;
        code += "D=A" + NL;
        code += "@"+ reg + NL;
        code += "M=D" + NL;
        
        return code;
    }
    
    private String contentRegEqD(String reg){
        String code = "@" + reg + NL;
        code += "A=M" + NL;
        code += "M=D" + NL;
        return code;
    }

    private String pushVal(String addr) {
        String code = DEqVal(addr);
        code += pushD();
        return code;
    }
    
    private String pushReg(String reg) {
        String code = DEqReg(reg);
        code += pushD();
        return code;
    }
    
    private String DeqOpVal(String val, String op){
        String code = "@" + val + NL;
        code += String.format("D=D%sA" + NL, op);
        return code;
    }
    
    private String gotoAddr(String addr){
        String code = "@" + addr + NL
                + "0;JMP" + NL;
        return code;
    }
    
    private String label(String label){
        return String.format("(%s)"+NL, label);
    }
    
    private String gotoReg(String reg){
        String code = "@"+reg + NL;
        code += "A=M" + NL;
        code += "0;JMP" + NL;
        return code;
    }
    
    private String RegEqContentReg(String dest, String src){
        String code = "@" + src + NL;
        code += "A=M" + NL;
        code += "D=M" + NL;
        code += "@" + dest  + NL;
        code += "M=D" + NL;
        return code;
    }
    
    private String RegEqContentRegOpVal(String dest, String src, String op, String val){
        String temp = "R15";
        String code = DEqReg(src);
        code += DeqOpVal(val, op);
        code += RegEqD(temp);
        code += RegEqContentReg(dest, temp);
        return code;
    }
}
