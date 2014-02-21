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
public class UniversityBean implements Comparable<UniversityBean> {

	/**
	 * User id
	 */
	private Long universityId;

	/**
	 * SSN
	 */
	private String name;

	/**
	 * Country
	 */
	private CountryBean country;
	
	/**
	 * Country
	 */
	private Long countryId;

	public UniversityBean() {

	}
	

	public Long getUniversityId() {
		return universityId;
	}

	public UniversityBean(Long universityId, String name, CountryBean country) {
		super();
		this.universityId = universityId;
		this.name = name;
		this.country = country;
	}

	public void setUniversityId(Long universityId) {
		this.universityId = universityId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public CountryBean getCountry() {
		return country;
	}



	public void setCountry(CountryBean country) {
		this.country = country;
	}



	@Override
	public int compareTo(UniversityBean another) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((universityId == null) ? 0 : universityId.hashCode());
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
		UniversityBean other = (UniversityBean) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (universityId == null) {
			if (other.universityId != null)
				return false;
		} else if (!universityId.equals(other.universityId))
			return false;
		return true;
	}


	public Long getCountryId() {
		return countryId;
	}


	public void setCountryId(Long countryId) {
		this.countryId = countryId;
		//this.country = new CountryBean(countryId, null, null);
	}

}
