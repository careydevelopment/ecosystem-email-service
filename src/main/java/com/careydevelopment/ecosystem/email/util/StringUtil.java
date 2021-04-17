package com.careydevelopment.ecosystem.email.util;

public class StringUtil {

    public static String removeZeroWidthNonJoiners(String str) {
        String updated = null;
        
        if (str != null) {
            updated = str.replaceAll("[\\p{Cf}]", "").trim();
        }
        
        return updated;
    }
}
