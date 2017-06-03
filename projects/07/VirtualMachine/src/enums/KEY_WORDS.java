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
public enum KEY_WORDS {
    ADD("add"),
    SUB("sub"),
    NEG("neg"),
    EQ("eq"),
    GT("gt"),
    LT("lt"),
    AND("and"),
    OR("or"),
    NOT("not"),
    ARGUMENT("argument"),
    LOCAL("local"),
    STATIC("static"),
    CONSTANT("constant"),
    THIS("this"),
    THAT("that"),
    POINTER("pointer"),
    TEMP("temp"),
    LABEL("label"),
    GOTO("goto"),
    IF_GOTO("if-goto"),
    FUNCTION("function"),
    CALL("call"),
    RETURN("return"),
    PUSH("push"),
    POP("pop")
    ;
    
    
    private String cmdName;
    private KEY_WORDS(String cmdName) {
        this.cmdName = cmdName;
    }
    
    public String getKeyWord(){
        return cmdName;
    }
}
