package com.unique_secure.meposconfiglite.networkconfig;

public class NetworkConfigurationProgress {
    private int step;
    private String message;

    public NetworkConfigurationProgress(int step, String message) {
        this.step = step;
        this.message = message;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
