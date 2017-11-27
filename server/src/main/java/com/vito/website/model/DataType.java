package com.vito.website.model;

import com.vito.common.util.date.DateUtil;
import com.vito.common.util.validate.Validator;

public enum DataType {
    DATE {
        @Override
        protected Object parseStr(String valStr) {
            return DateUtil.changeStrToDate(valStr);
        }
    },
    LONG {
        @Override
        protected Object parseStr(String valStr) {
            String str = valStr.replaceAll("[^0-9]", "");
            return Long.valueOf(str);
        }
    },
    INT {
        @Override
        protected Object parseStr(String valStr) {
            String str = valStr.replaceAll("[^0-9]", "");
            return Integer.valueOf(str);
        }
    },
    SHORT {
        @Override
        protected Object parseStr(String valStr) {
            String str = valStr.replaceAll("[^0-9]", "");
            return Short.valueOf(str);
        }
    };

    public Object parse(String valStr) {
        if (Validator.isNull(valStr)) {
            return null;
        } else {
            return parseStr(valStr);
        }
    }

    protected abstract Object parseStr(String valStr);

    public static void main(String[] args) {
        System.out.println(INT.parse("2015-05-01"));
    }
}