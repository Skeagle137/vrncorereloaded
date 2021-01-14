package net.skeagle.vrncore.utils;

import lombok.Getter;

public final class TimeFormatException extends Exception {

    private static final long serialVersionUID = -8147740739527052671L;
    @Getter
    private final String message;

    public TimeFormatException(final String message) {
        super("");
        this.message = message;
    }
}
