package com.woowacourse.f12.acceptance.support;

public class EscapeSqlUtil {

    public static String escapeTableName(String tableName) {
        if (tableName == null) {
            return null;
        }
        return tableName.replace(" ", "")
                .replace(";", "");
    }
}
