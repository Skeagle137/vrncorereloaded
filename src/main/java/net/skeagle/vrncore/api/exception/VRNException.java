package net.skeagle.vrncore.api.exception;


public class VRNException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public VRNException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
