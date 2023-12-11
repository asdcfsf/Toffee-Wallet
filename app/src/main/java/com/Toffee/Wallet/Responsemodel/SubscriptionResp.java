package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionResp {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("status")
	private int status;

	public List<DataItem> getData(){
		return data;
	}

	public int getStatus(){
		return status;
	}

	public class DataItem{
		@SerializedName("id")
		private String id;

		@SerializedName("title")
		private String title;

		@SerializedName("description")
		private String description;

		@SerializedName("amount")
		private String amount;

		@SerializedName("valid_for")
		private String valid_for;

		@SerializedName("task_spin")
		private String task_spin;

		@SerializedName("currency")
		private String currency;

		@SerializedName("promo_coin")
		private String promo_coin;

		@SerializedName("task_scratch")
		private String task_scratch;

		@SerializedName("refer_bonus")
		private String refer_bonus;
		@SerializedName("currency_posi")
		private String currency_posi;

		public String getCurrency_posi() {
			return currency_posi;
		}

		@SerializedName("task_web")
		private String task_web;

		@SerializedName("task_video")
		private String task_video;

		@SerializedName("task_videozone")
		private String task_videozone;


		@SerializedName("task_quiz")
		private String task_quiz;

		@SerializedName("productID")
		private String productID;

		@SerializedName("task_daily_offer")
		private String task_daily_offer;

		public String getTask_daily_offer() {
			return task_daily_offer;
		}

		@SerializedName("image")
		private String image;

		public String getImage() {
			return image;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public String getAmount() {
			return amount;
		}

		public String getValid_for() {
			return valid_for;
		}

		public String getPromo_coin() {
			return promo_coin;
		}

		public String getTask_web() {
			return task_web;
		}

		public String getTask_video() {
			return task_video;
		}

		public String getTask_videozone() {
			return task_videozone;
		}

		public String getTask_quiz() {
			return task_quiz;
		}

		public String getTask_scratch() {
			return task_scratch;
		}

		public String getRefer_bonus() {
			return refer_bonus;
		}

		public String getProductID() {
			return productID;
		}


		public String getTask_spin() {
			return task_spin;
		}

		public String getCurrency() {
			return currency;
		}
	}

}