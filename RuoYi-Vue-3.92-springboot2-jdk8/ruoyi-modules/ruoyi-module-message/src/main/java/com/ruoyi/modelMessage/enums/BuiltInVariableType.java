package com.ruoyi.modelMessage.enums;

import com.ruoyi.auth.common.utils.RandomCodeUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.modelMessage.domain.MessageSystem;

public enum BuiltInVariableType {
    TIME("time", "HH:mm:ss") {
        @Override
        public String generateValue(MessageSystem message) {
            return DateUtils.dateTimeNow(format);
        }
    },
    DATE("date", "yyyy-MM-dd") {
        @Override
        public String generateValue(MessageSystem message) {
            return DateUtils.dateTimeNow(format);
        }
    },
    DATETIME("datetime", "yyyy-MM-dd HH:mm:ss") {
        @Override
        public String generateValue(MessageSystem message) {
            return DateUtils.dateTimeNow(format);
        }
    },
    ADDRESSER("addresser", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return SecurityUtils.getUsername();
        }
    },
    CODE("code", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return RandomCodeUtil.numberCode(CODE_LENGTH);
        }
    },
    RANDOMN_DIGITS("RandomnDigits", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return RandomCodeUtil.numberCode(CODE_LENGTH);
        }
    },
    RANDOMN_CHARACTERS("RandomnCharacters", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return RandomCodeUtil.code(CODE_LENGTH);
        }
    },
    RANDOMN_DIGIT_LETTERS("RandomN-digitLetters", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return RandomCodeUtil.code(CODE_LENGTH);
        }
    },
    RANDOMN_DIGIT_LETTERS_ALT("RandomNDigitLetters", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return RandomCodeUtil.code(CODE_LENGTH);
        }
    },
    RECIPIENTS("recipients", null) {
        @Override
        public String generateValue(MessageSystem message) {
            return message.getMessageRecipient();
        }
    };

    private final String identifier;
    protected final String format;
    private static final int CODE_LENGTH = 6;

    BuiltInVariableType(String identifier, String format) {
        this.identifier = identifier;
        this.format = format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract String generateValue(MessageSystem message);

    public static BuiltInVariableType fromIdentifier(String identifier) {
        if (identifier == null) return null;
        for (BuiltInVariableType type : values()) {
            if (type.identifier.equals(identifier)) {
                return type;
            }
        }
        return null;
    }

    public static boolean isBuiltInVariable(String identifier) {
        return fromIdentifier(identifier) != null;
    }
}


