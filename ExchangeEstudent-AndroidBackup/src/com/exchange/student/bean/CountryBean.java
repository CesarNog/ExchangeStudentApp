/**
 * @author Cesar A. Nogueira 
 */
package com.exchange.student.bean;


/**
 * TO that contains USER data to be persisted
 * 
 * @author Cesar A. Nogueira
 * 
 */
public class CountryBean implements Comparable<CountryBean> {

	/**
	 * User id
	 */
	private Long countryId;

	/**
	 * SSN
	 */
	private String name;

	/**
	 * Address
	 */
	private String address;

	public CountryBean() {

	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CountryBean(Long countryId, String name, String address) {
		super();
		this.countryId = countryId;
		this.name = name;
		this.address = address;
	}

	@Override
	public String toString() {
		return "CountryBean [countryId=" + countryId + ", name=" + name
				+ ", address=" + address + "]";
	}

	@Override
	public int compareTo(CountryBean another) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CountryBean other = (CountryBean) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
