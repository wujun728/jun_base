package com.geek.liquibase;

import java.util.regex.Pattern;

import liquibase.changelog.IncludeAllFilter;

public class ChangeLogFilter implements IncludeAllFilter {

    @Override
    public boolean include(String arg0) {
        arg0 = arg0.replace("\\", "/");
        return Pattern.compile("db/changelog/changelog-.+\\.xml").matcher(arg0).matches();
    }

}
