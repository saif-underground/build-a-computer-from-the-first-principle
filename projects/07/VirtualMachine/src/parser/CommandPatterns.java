/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import enums.KEY_WORDS;
import java.util.regex.Pattern;

/**
 *
 * @author Saif
 */
public class CommandPatterns {
    private static String segment = 
            KEY_WORDS.ARGUMENT.getKeyWord() + "|" +
            KEY_WORDS.LOCAL.getKeyWord() + "|"+
            KEY_WORDS.STATIC.getKeyWord() + "|"+
            KEY_WORDS.CONSTANT.getKeyWord() + "|"+
            KEY_WORDS.THIS.getKeyWord() + "|"+
            KEY_WORDS.THAT.getKeyWord() + "|"+
            KEY_WORDS.POINTER.getKeyWord() + "|"+
            KEY_WORDS.TEMP.getKeyWord() ;
    private static String pushPop = KEY_WORDS.PUSH.getKeyWord() + "|" + 
            KEY_WORDS.POP.getKeyWord();
    private static String symbolPattern = "[a-zA-Z:\\$_\\.]+[a-zA-Z:\\$_\\.0-9]*";
    private static String programFlowCommands = KEY_WORDS.LABEL.getKeyWord() + "|"+
            KEY_WORDS.IF_GOTO.getKeyWord() + "|"+
            KEY_WORDS.GOTO.getKeyWord();
    private static String arithmatiLogicalCommands = KEY_WORDS.ADD.getKeyWord() + "|" + 
            KEY_WORDS.SUB.getKeyWord() + "|" + 
            KEY_WORDS.NEG.getKeyWord() + "|" + 
            KEY_WORDS.EQ.getKeyWord() + "|" + 
            KEY_WORDS.GT.getKeyWord() + "|" + 
            KEY_WORDS.LT.getKeyWord() + "|" + 
            KEY_WORDS.AND.getKeyWord() + "|" + 
            KEY_WORDS.OR.getKeyWord() + "|" + 
            KEY_WORDS.NOT.getKeyWord() + "|" + 
            KEY_WORDS.NOT.getKeyWord();
    
    private String arithmaticLogicalCommandPatternStr = String.format("^(%s)$", arithmatiLogicalCommands);
    private static String pushPopPatternStr = String.format("^(%s) (%s) (\\d+)$", pushPop, segment); 
    public static String programFlowPatternStr = String.format("^(%s) (%s)$", programFlowCommands, symbolPattern);
    public static String functionDeclPatternStr = String.format("^function (%s) (\\d+)$", symbolPattern); 
    public static String callPatternStr = String.format("^call (%s) (\\d+)$", symbolPattern);
    public static String returnPattternStr = "^return$";
    
    public Pattern getArithmaticLogicalCmdPattern(){
        return Pattern.compile(arithmaticLogicalCommandPatternStr);
    }
    
    public Pattern getPushPopCmdPattern(){
        return Pattern.compile(pushPopPatternStr);
    }
    
    public Pattern getProgramFlowPattern(){
        return Pattern.compile(programFlowPatternStr);
    }
}
