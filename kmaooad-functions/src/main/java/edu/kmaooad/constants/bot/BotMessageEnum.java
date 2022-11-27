package edu.kmaooad.constants.bot;

public enum BotMessageEnum {

    EXCEPTION_ILLEGAL_MESSAGE("That's something strange. I need to clean my glasses."),
    EXCEPTION_UNKNOWN("Something went wrong. If you want to ask for support, go ask a cat."),
    SUCCESSFULLY_ADD("Успішно додано");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
