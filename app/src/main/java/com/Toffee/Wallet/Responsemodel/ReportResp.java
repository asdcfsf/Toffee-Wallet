package com.Toffee.Wallet.Responsemodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReportResp {

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

		@SerializedName("total")
		private int total;

		@SerializedName("webId")
		private int webId;

		@SerializedName("inserted_at")
		private String insertedAt;

		public int getTotal(){
			return total;
		}

		public int getWebId(){
			return webId;
		}

		public String getInsertedAt(){
			return insertedAt;
		}
	}
}