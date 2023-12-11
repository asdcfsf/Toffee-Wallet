package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MissionResponse{

	@SerializedName("data")
	private List<Data> data;;

	@SerializedName("status")
	private int status;

	@SerializedName("total")
	private int total;

	@SerializedName("left")
	private int left;

	public int getTotal() {
		return total;
	}

	public int getLeft() {
		return left;
	}

	public List<Data> getData() {
		return data;
	}

	public int getStatus(){
		return status;
	}

	public class Data{

		@SerializedName("title")
		private String title;

		@SerializedName("id")
		private String id;

		@SerializedName("limit")
		private String limit;

		@SerializedName("count")
		private String count;

		@SerializedName("status")
		private String status;

		@SerializedName("btn_name")
		private String btn_name;

		@SerializedName("btn_action")
		private String btn_action;

		@SerializedName("coin")
		private int coin;

		@SerializedName("promo_coin")
		private int promo_coin;

		@SerializedName("image")
		private String image;

		public String getImage() {
			return image;
		}

		public String getTitle() {
			return title;
		}

		public String getLimit() {
			return limit;
		}

		public String getCount() {
			return count;
		}

		public int getCoin() {
			return coin;
		}

		public int getPromo_coin() {
			return promo_coin;
		}

		public String getStatus() {
			return status;
		}

		public String getBtn_name() {
			return btn_name;
		}

		public String getBtn_action() {
			return btn_action;
		}

		public String getId() {
			return id;
		}
	}
}