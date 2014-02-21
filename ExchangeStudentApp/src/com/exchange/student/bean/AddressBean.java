package com.exchange.student.bean;

public class AddressBean implements Comparable<AddressBean> {
	
	/**
	 * User id
	 */
	private Long addressId;
	
	/**
	 * Street name
	 */
	private String streetName;
	
	private String streetNumber;
	
	private Long aptNumber;
	
	private String city;
	
	private String zipCode;
	
	private String state;
	
	private CountryBean country;
	
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public Long getAptNumber() {
		return aptNumber;
	}

	public void setAptNumber(Long aptNumber) {
		this.aptNumber = aptNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public AddressBean(Long addressId, String streetName,
			String streetNumber, Long aptNumber, String city, String zipCode,
			String state) {
		super();
		this.addressId = addressId;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.aptNumber = aptNumber;
		this.city = city;
		this.zipCode = zipCode;
		this.state = state;
	}

	public AddressBean() {
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CountryBean getCountry() {
		return country;
	}

	public void setCountry(CountryBean country) {
		this.country = country;
	}

	@Override
	public int compareTo(AddressBean arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "AddressBean [addressId=" + addressId + ", streetName="
				+ streetName + ", streetNumber=" + streetNumber
				+ ", aptNumber=" + aptNumber + ", city=" + city + ", zipCode="
				+ zipCode + ", state=" + state + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result
				+ ((aptNumber == null) ? 0 : aptNumber.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result
				+ ((streetName == null) ? 0 : streetName.hashCode());
		result = prime * result
				+ ((streetNumber == null) ? 0 : streetNumber.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		AddressBean other = (AddressBean) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (aptNumber == null) {
			if (other.aptNumber != null)
				return false;
		} else if (!aptNumber.equals(other.aptNumber))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (streetName == null) {
			if (other.streetName != null)
				return false;
		} else if (!streetName.equals(other.streetName))
			return false;
		if (streetNumber == null) {
			if (other.streetNumber != null)
				return false;
		} else if (!streetNumber.equals(other.streetNumber))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

}
