package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class FaqResp {

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    @SerializedName("type")
    private String type;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }
}