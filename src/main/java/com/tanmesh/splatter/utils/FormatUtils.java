package com.tanmesh.splatter.utils;

import java.text.BreakIterator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {

    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-])(?=\\S+$).{6,}$");
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+=-]{3,}$");
    public static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]+$");
    public static final Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static boolean isValidName(String name){
        if(name == null){
            return false;
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    public static boolean isValidEmailId(String emailId){
        if(emailId == null){
            return false;
        }
        Matcher matcher = EMAIL_ADDRESS.matcher(emailId);
        if(matcher.find()){
            return true;
        }
        return false;
    }


    public static String sanitise(String str) {
        return str.trim().toLowerCase();
    }


    public static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    public static List<String> getWords(String text) {
        List<String> words = new ArrayList<String>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }

        return words;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(String conversationId) {
        return conversationId == null || conversationId.length() == 0;
    }

    public static <T> T coalesce(T... items) {
        for (T i : items) if (i != null) return i;
        return null;
    }

    public static boolean exists(String veryHugeString, Set<String> words) {
        if (words == null || words.isEmpty() || veryHugeString == null || veryHugeString.isEmpty())
            return false;

        String[] strings = veryHugeString.split(" ");
        for (String singleWord : strings) {
            if (words.contains(singleWord))
                return true;
        }
        return false;
    }

}
