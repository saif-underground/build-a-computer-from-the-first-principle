/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Saif
 */
public class RegExTest {

    public static void main(String args[]) {
        final String regex = "(d\\+b|c\\+d|!d|d)";
        final String string = "d+b";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        
            
        matcher.find();
        System.out.println(matcher.group(1));

    }
}

