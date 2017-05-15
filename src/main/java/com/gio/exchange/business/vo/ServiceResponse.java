package com.gio.exchange.business.vo;

public class ServiceResponse {
    /**
     * Attribute to capture the Response Code to
     * be sent to the Rest Client
     */
    private int responseCode;

    /**
     * Attribute to capture the Response Message
     * to be sent to the Rest Client
     */
    private String message;

    /**
     * This stores field level error messages
     */
    private Object payload;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
