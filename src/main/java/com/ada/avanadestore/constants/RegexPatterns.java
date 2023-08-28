package com.ada.avanadestore.constants;

import java.util.regex.Pattern;

public class RegexPatterns {

    public static final String ZIP_FORMAT = "\\d{5}-\\d{3}";
    public static final Pattern POSTGRES_DB_ERROR_PATTERN = Pattern.compile("Detail: Key \\(([^)]+)\\)=\\([^)]+\\) (.+)");

}
