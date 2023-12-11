package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class HistoryResp {

    @SerializedName("remained_balance")
    private String remainedBalance;

    @SerializedName("amount")
    private String amount;

    @SerializedName("tran_type")
    private String tranType;

    @SerializedName("type")
    private String type;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("id")
    private int id;

    @SerializedName("inserted_at")
    private String insertedAt;

    @SerializedName("remarks")
    private String remarks;

	@SerializedName("date")
	private String date;

	@SerializedName("orginal_amount")
	private String orginalAmount;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("mobile_no")
	private String mobileNo;

	@SerializedName("detail")
	private String detail;

	@SerializedName("request_id")
	private String requestId;

	@SerializedName("status")
	private String status;

    @SerializedName("image")
	private String image;

    public String getImage() {
        return image;
    }

    private int viewType = 0;

    public int getViewType() {
        return viewType;
    }

    public HistoryResp setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }

    public String getDate() {
		return date;
	}

	public String getOrginalAmount() {
		return orginalAmount;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public String getDetail() {
		return detail;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getStatus() {
		return status;
	}

	public String getRemainedBalance() {
        return remainedBalance;
    }

    public String getAmount() {
        return amount;
    }

    public String getTranType() {
        return tranType;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getInsertedAt() {
        return insertedAt;
    }

    public String getRemarks() {
        return remarks;
    }

}