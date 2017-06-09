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

    private static int labelCount = 0;
    String fileName;
    String output = "";
    FileWriter fileWriter = null;

    public CodeWriter(String fileName) throws IOException {
        setFileName(fileName);
    }

    public void setFileName(String fileName) throws IOException {
        this.fileName = fileName;
        writeBootStrapCode();
    }

    //EVENTUALLY REMOVE THIS METHOD AND CALL SYS.INIT INSTEAD
    private void writeBootStrapCode() throws IOException {
        fileWriter = new FileWriter(fileName);
        String code = null;
        code = "@" + RAM_ADDRESS.STACK.getBegin() + "\n";
        code = code
                + "D=A\n"
                + "@SP\n"
                + "M=D\n";
        //REMOVE THIS LCL=1000
        code = code
                + "@1000\n"
                + "D=A\n"
                + "@LCL\n"
                + "M=D\n";

        code = code
                + "@2000\n"
                + "D=A\n"
                + "@ARG\n"
                + "M=D\n";

        fileWriter.write(code);
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
                    + "M=D" + operator + "M\n"
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
                    + "D=D-M\n"
                    + "M=-1\n"
                    + "@" + label + "\n"
                    + "D;" + jmpMnemonic + "\n"
                    + "@SP\n"
                    + "A=M-1\n"
                    + "M=0\n"
                    + "(" + label + ")\n";
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
                        + "@" + ramAddr + "\n"
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
                code =  "@SP\n"
                        + "M=M-1\n"
                        + "A=M\n"
                        + "D=M\n"
                        + "@" + ramAddr + "\n"
                        + "M=D\n" 
                        ;
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
        } else if(segment.equalsIgnoreCase(KEY_WORDS.STATIC.getKeyWord())){
            int offset = Integer.parseInt(index);
            if (offset >= 0 && offset <= (RAM_ADDRESS.STATIC.getEnd() - RAM_ADDRESS.STATIC.getBegin())) {
                ramAddr = "" + (RAM_ADDRESS.STATIC.getBegin() + offset);
            }
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
}
