package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RewardResp {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("code")
	private String code;

	public String getCode() {
		return code;
	}

	public List<DataItem> getData(){
		return data;
	}

	public class DataItem{

		@SerializedName("image")
		private String image;

		@SerializedName("pointvalue")
		private String pointvalue;

		@SerializedName("task")
		private String task;

		@SerializedName("placeholder")
		private String placeholder;

		@SerializedName("input_type")
		private String input_type;

		@SerializedName("description")
		private String description;

		@SerializedName("id")
		private String id;

		@SerializedName("category")
		private String category;

		@SerializedName("title")
		private String title;

		@SerializedName("points")
		private String points;

		@SerializedName("status")
		private int status;

		@SerializedName("refer")
		private String refer;

		@SerializedName("quantity")
		private String quantity;

		public String getQuantity() {
			return quantity;
		}

		public String getTask() {
			return task;
		}

		public String getRefer() {
			return refer;
		}

		public String getPlaceholder() {
			return placeholder;
		}

		public String getInput_type() {
			return input_type;
		}

		public String getImage(){
			return image;
		}

		public String getPointvalue(){
			return pointvalue;
		}

		public String getDescription(){
			return description;
		}

		public String getId(){
			return id;
		}

		public String getCategory(){
			return category;
		}

		public String getTitle(){
			return title;
		}

		public String getPoints(){
			return points;
		}

		public int getStatus(){
			return status;
		}
	}
}