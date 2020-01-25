package com.benefit.utilities.staticClasses;

import android.content.res.Resources;

import java.util.Random;

/**
 * A general utilities class
 */
public class Converters {
    public static int convertDpToPx(double dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int convertPixelsToDp(float px){
        return (int)px / (int) (Resources.getSystem().getDisplayMetrics().density);
    }

    public static int convertDpToSp(float dp){
        return (int) convertDpToPx(dp) / (int) Resources.getSystem().getDisplayMetrics().scaledDensity;
    }

    public static Boolean isSubstring(String s1,String s2){
        for (int i = 0; i <= s2.length() - s1.length(); i++){
            int j;
            for (j =0; j < s1.length(); j++){
                if (s2.charAt(i + j) != s1.charAt(j)){
                    break;
                }
            }
            if (j == s1.length()){
                return true;
            }
        }
        return false;
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt() * random.nextInt();
    }
}
