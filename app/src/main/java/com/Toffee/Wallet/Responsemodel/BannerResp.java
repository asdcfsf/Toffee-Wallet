package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BannerResp {

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

		@SerializedName("onclick")
		private String onclick;

		@SerializedName("link")
		private String link;

		@SerializedName("bannertype")
		private String bannertype;

		@SerializedName("banner")
		private String banner;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("id")
		private int id;

		@SerializedName("status")
		private int status;

		public String getBannertype() {
			return bannertype;
		}

		public String getOnclick(){
			return onclick;
		}

		public String getLink(){
			return link;
		}

		public String getBanner(){
			return banner;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public int getId(){
			return id;
		}

		public int getStatus(){
			return status;
		}
	}
}