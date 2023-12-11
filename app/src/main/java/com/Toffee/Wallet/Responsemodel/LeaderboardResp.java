package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LeaderboardResp {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("code")
	private String code;

	@SerializedName("msg")
	private String msg;

	public String getMsg() {
		return msg;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getCode() {
		return code;
	}

	public static class DataItem{

		@SerializedName("balance")
		private int balance;

		@SerializedName("name")
		private String name;

		@SerializedName("profile")
		private String profile;

		@SerializedName("refer")
		private String refer;

		@SerializedName("image")
		private String image;

		@SerializedName("country")
		private String country;

		@SerializedName("totalCoin")
		private String totalCoin;

		public String getTotalCoin() {
			return totalCoin;
		}

		public String getRefer() {
			return refer;
		}

		public String getImage() {
			return image;
		}

		public String getCountry() {
			return country;
		}

		@SerializedName("inserted_at")
		private String inserted_at;

		@SerializedName("type")
		private String type;

		public String getProfile() {
			return profile;
		}

		public String getType() {
			return type;
		}

		public int getBalance(){
			return balance;
		}

		public String getName(){
			return name;
		}

		public String getInserted_at() {
			return inserted_at;
		}
	}
}