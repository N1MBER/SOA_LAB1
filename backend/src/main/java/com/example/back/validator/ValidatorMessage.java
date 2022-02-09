package com.example.back.validator;

public class ValidatorMessage {
    private boolean status;
    private String message;

    public ValidatorMessage() {
        this.status = false;
        this.message = "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}
