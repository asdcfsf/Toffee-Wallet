package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class SupportResp {
    @SerializedName("subject")
    private String subject;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("ticketID")
    private String ticketID;

    public String getTicketID() {
        return ticketID;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
