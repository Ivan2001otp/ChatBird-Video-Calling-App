package com.example.composejava.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static void hideSoftKeyboardUtil(Context context, View view){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(view!=null){
            inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(),0);
        }
    }

    public static int passwordChecker(String password){
        if(password.length()<6){
            return -1;
        }

        String regex = "[a-zA-Z]|[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if(matcher.find() && password.length()>=6){
            return 1;
        }

        return 0;
    }

    public static String randomCodeGenerator(){
             final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

             Random rand = new Random();
             StringBuilder sb = new StringBuilder();

             for(int i=0;i<8;i++){
                 int index = rand.nextInt(CHARACTERS.length());
                 char ch = CHARACTERS.charAt(index);
                 sb.append(ch);
             }

             return sb.toString();
    }
}
