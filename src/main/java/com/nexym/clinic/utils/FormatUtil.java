package com.nexym.clinic.utils;

import java.util.Collection;

public class FormatUtil {

    public static boolean isFilled(Collection<? extends Object> myCollection) {
        return myCollection != null && !myCollection.isEmpty();
    }

    public static boolean isFilled(String myString) {
        return myString != null && !myString.isEmpty();
    }

    private FormatUtil() {
        // Utility classes should not have public constructors.
    }

}
