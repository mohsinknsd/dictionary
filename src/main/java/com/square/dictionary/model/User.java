package com.square.dictionary.model;

import java.util.Date;

public class User {
	private int id;
	private String frstName;
	private String email;
	private String password;
	private byte gender;
	private String mobile;
	private String address;
	private String city;
	private String state;
	private String country;
	private boolean verified;
	private Date reg_date;
	private byte status;	
	
	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
	}

	public synchronized String getFrstName() {
		return frstName;
	}

	public synchronized void setFrstName(String frstName) {
		this.frstName = frstName;
	}

	public synchronized String getEmail() {
		return email;
	}

	public synchronized void setEmail(String email) {
		this.email = email;
	}

	public synchronized String getPassword() {
		return password;
	}

	public synchronized void setPassword(String password) {
		this.password = password;
	}

	public synchronized byte getGender() {
		return gender;
	}

	public synchronized void setGender(byte gender) {
		this.gender = gender;
	}

	public synchronized String getMobile() {
		return mobile;
	}

	public synchronized void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public synchronized String getAddress() {
		return address;
	}

	public synchronized void setAddress(String address) {
		this.address = address;
	}

	public synchronized String getCity() {
		return city;
	}

	public synchronized void setCity(String city) {
		this.city = city;
	}

	public synchronized String getState() {
		return state;
	}

	public synchronized void setState(String state) {
		this.state = state;
	}

	public synchronized String getCountry() {
		return country;
	}

	public synchronized void setCountry(String country) {
		this.country = country;
	}

	public synchronized boolean isVerified() {
		return verified;
	}

	public synchronized void setVerified(boolean verified) {
		this.verified = verified;
	}

	public synchronized Date getReg_date() {
		return reg_date;
	}

	public synchronized void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

	public synchronized byte getStatus() {
		return status;
	}

	public synchronized void setStatus(byte status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((frstName == null) ? 0 : frstName.hashCode());
		result = prime * result + gender;
		result = prime * result + id;
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((reg_date == null) ? 0 : reg_date.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + status;
		result = prime * result + (verified ? 1231 : 1237);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (frstName == null) {
			if (other.frstName != null)
				return false;
		} else if (!frstName.equals(other.frstName))
			return false;
		if (gender != other.gender)
			return false;
		if (id != other.id)
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (reg_date == null) {
			if (other.reg_date != null)
				return false;
		} else if (!reg_date.equals(other.reg_date))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (status != other.status)
			return false;
		if (verified != other.verified)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", frstName=" + frstName + ", email=" + email
				+ ", password=" + password + ", gender=" + gender + ", mobile="
				+ mobile + ", address=" + address + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", verified="
				+ verified + ", reg_date=" + reg_date + ", status=" + status
				+ "]";
	}
}
