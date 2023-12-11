package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OfferwallResp {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("code")
	private String code;

	public List<DataItem> getData(){
		return data;
	}

	public String getCode(){
		return code;
	}

	public class DataItem{

		@SerializedName("postback")
		private String postback;

		@SerializedName("data")
		private String data;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("u_tag")
		private String u_tag;

		@SerializedName("offer_slug")
		private String offer_slug;

		@SerializedName("thumb")
		private String thumb;

		@SerializedName("description")
		private String description;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("id")
		private int id;

		@SerializedName("title")
		private String title;

		@SerializedName("type")
		private String type;

		@SerializedName("status")
		private String status;

		@SerializedName("test_mode")
		private boolean test_mode;

		public boolean isTest_mode() {
			return test_mode;
		}

		public String getU_tag() {
			return u_tag;
		}

		public String getOffer_slug() {
			return offer_slug;
		}

		public String getPostback(){
			return postback;
		}

		public String getData(){
			return data;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public String getThumb(){
			return thumb;
		}

		public String getDescription(){
			return description;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public int getId(){
			return id;
		}

		public String getTitle(){
			return title;
		}

		public String getType(){
			return type;
		}

		public String getStatus(){
			return status;
		}
	}
}