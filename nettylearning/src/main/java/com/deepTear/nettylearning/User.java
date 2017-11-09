package com.deepTear.nettylearning;

public class User {

	private String userName;
	private int age;
	private String profile;
	private byte[] attachment;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", age=" + age + ", profile=" + profile + "]";
	}

}
