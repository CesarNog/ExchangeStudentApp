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

	@Override
	public String toString() {
		return "CountryBean [countryId=" + countryId + ", name=" + name
				+ "]";
	}

	@Override
	public int compareTo(CountryBean another) {
		// TODO Auto-generated method stub
		return 0;
	}


}
