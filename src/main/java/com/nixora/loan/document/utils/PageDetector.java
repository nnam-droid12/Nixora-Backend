package com.nixora.loan.document.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageDetector {

    private static final Pattern PAGE_PATTERN =
            Pattern.compile("---PAGE (\\d+)---");

    public static int findPage(String text, int offset) {
        String before = text.substring(0, offset);
        Matcher matcher = PAGE_PATTERN.matcher(before);

        int page = 1;
        while (matcher.find()) {
            page = Integer.parseInt(matcher.group(1));
        }

        return page;
    }
}
