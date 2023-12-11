package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CoinStoreResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("code")
	private String code;

	@SerializedName("info")
	private String info;

	public String getInfo() {
		return info;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getCode() {
		return code;
	}

	public class DataItem{

		@SerializedName("amount")
		private String amount;

		@SerializedName("productID")
		private String productID;

		@SerializedName("title")
		private String title;

		@SerializedName("update_at")
		private Object updateAt;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("currency_posi")
		private String currency_posi;

		public String getCurrency_posi() {
			return currency_posi;
		}

		@SerializedName("id")
		private String id;

		@SerializedName("coin")
		private String coin;

		@SerializedName("inr_amount")
		private String inr_amount;

		public String getInr_amount() {
			return inr_amount;
		}

		@SerializedName("status")
		private String status;

		@SerializedName("country")
		private String country;

		@SerializedName("currency")
		private String currency;

		public String getCurrency() {
			return currency;
		}

		public String getCountry() {
			return country;
		}

		public String getAmount(){
			return amount;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getProductID() {
			return productID;
		}

		public Object getUpdateAt(){
			return updateAt;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public String getId(){
			return id;
		}

		public String getCoin(){
			return coin;
		}

		public String getStatus(){
			return status;
		}
	}

}