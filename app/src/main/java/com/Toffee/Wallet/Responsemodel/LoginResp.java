package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class LoginResp{

	@SerializedName("resp")
	private String resp;

	@SerializedName("message")
	private String message;

	@SerializedName("code")
	private String code;

	@SerializedName("user")
	private User user;

	public String getResp() {
		return resp;
	}

	public String getMessage(){
		return message;
	}

	public String getCode() {
		return code;
	}

	public User getUser(){
		return user;
	}

	@SerializedName("noti")
	private int noti;

	public class User{

		@SerializedName("country")
		private String country;

		@SerializedName("from_refferal_id")
		private int fromRefferalId;

		@SerializedName("balance")
		private int balance;

		@SerializedName("refer")
		private String refer;

		@SerializedName("profile")
		private String profile;

		@SerializedName("name")
		private String name;

		@SerializedName("refferal_id")
		private String refferalId;

		@SerializedName("cust_id")
		private String custId;

		@SerializedName("userid")
		private Object userid;

		@SerializedName("inserted_at")
		private String insertedAt;

		@SerializedName("email")
		private String email;

		@SerializedName("person_id")
		private String personId;

		@SerializedName("promo_balance")
		private int promo_balance;

		public int getPromo_balance() {
			return promo_balance;
		}

		public String getCountry(){
			return country;
		}

		public int getFromRefferalId(){
			return fromRefferalId;
		}

		public int getBalance(){
			return balance;
		}

		public String getRefer(){
			return refer;
		}

		public String getProfile(){
			return profile;
		}

		public String getName(){
			return name;
		}

		public String getRefferalId(){
			return refferalId;
		}

		public String getCustId(){
			return custId;
		}

		public Object getUserid(){
			return userid;
		}

		public String getInsertedAt(){
			return insertedAt;
		}

		public String getEmail(){
			return email;
		}

		public String getPersonId(){
			return personId;
		}
	}

	public int getNoti() {
		return noti;
	}
}