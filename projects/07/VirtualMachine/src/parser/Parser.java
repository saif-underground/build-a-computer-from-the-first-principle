/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import enums.COMMAND_TYPE;
import enums.KEY_WORDS;
import java.util.regex.Matcher;
import wrappers.FileWrapper;

/**
 * goes through the code line by line. determines command type, extracts symbol,
 * destination, comp and jump portions
 *
 * @author Saif
 */
public class Parser {

    private FileWrapper file = null;
    private COMMAND_TYPE currentCmdType = null;
    private String arg1 = null;
    private String arg2 = null;
    private String currentCmd = null;
    private CommandPatterns cmdPatterns;

    public Parser(String fileName) {
        file = new FileWrapper(fileName);
        cmdPatterns = new CommandPatterns();
    }

    public boolean hasMoreCommands() {
        return file.hasNextLine();
    }

    public void advance() {
        currentCmd = file.getNextLine();
    }

    public COMMAND_TYPE commandType() {
        arg1 = null;
        arg2 = null;
        currentCmdType = null;

        if (currentCmd == null) {
            throw new RuntimeException();
        }

        if (currentCmd.matches(cmdPatterns.getArithmaticLogicalCmdPattern().toString())) {
            currentCmdType = COMMAND_TYPE.C_ARITHMATIC;
            Matcher matcher = cmdPatterns.getArithmaticLogicalCmdPattern().matcher(currentCmd);
            if (matcher.find()) {
                arg1 = matcher.group(1);
            }
        } else if(currentCmd.matches(cmdPatterns.getPushPopCmdPattern().toString())) {
            if (currentCmd.startsWith("push")) {
                currentCmdType = COMMAND_TYPE.C_PUSH;
            } else {
                currentCmdType = COMMAND_TYPE.C_POP;
            }
            Matcher matcher = cmdPatterns.getPushPopCmdPattern().matcher(currentCmd);
            if (matcher.find()) {
                arg1 = matcher.group(2);
                arg2 = matcher.group(3);
            }
        } else if(currentCmd.matches(cmdPatterns.getProgramFlowPattern().toString())){
            if(currentCmd.startsWith(KEY_WORDS.LABEL.getKeyWord())){
                currentCmdType = COMMAND_TYPE.C_LABEL;
            }else if(currentCmd.startsWith(KEY_WORDS.IF_GOTO.getKeyWord())){
                currentCmdType = COMMAND_TYPE.C_IF;
            }else if(currentCmd.startsWith(KEY_WORDS.GOTO.getKeyWord())){
                currentCmdType = COMMAND_TYPE.C_GOTO;
            }
            
            Matcher matcher = cmdPatterns.getProgramFlowPattern().matcher(currentCmd);
            if (matcher.find()) {
                arg1 = matcher.group(1);
            }            
        }

        if (currentCmdType == null) {
            throw new RuntimeException();
        }
        return currentCmdType;
    }

    public String arg1() {
        return arg1;
    }

    public String arg2() {
        return arg2;
    }
}
