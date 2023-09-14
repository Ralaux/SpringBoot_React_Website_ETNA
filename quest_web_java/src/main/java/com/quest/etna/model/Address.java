package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="address")
public class Address {
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "street is mandatory")
	@Column(name = "street", length = 100, nullable = false)
	private String street;
	
	@NotEmpty(message = "postalCode is mandatory")
	@Column(name = "postalCode", length = 30, nullable = false)
	private String postalCode;
	
	@NotEmpty(message = "city is mandatory")
	@Column(name = "city", length = 50, nullable = false)
	private String city;
	
	@NotEmpty(message = "country is mandatory")
	@Column(name = "country", length = 50, nullable = false)
	private String country;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "creation_date")
	private Date creation_date;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "updated_date")
	private Date updated_date;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
	private User user;
	
	public Address() {};

	public Address(@NotEmpty(message = "street is mandatory") String street,
			@NotEmpty(message = "postalCode is mandatory") String postalCode,
			@NotEmpty(message = "city is mandatory") String city,
			@NotEmpty(message = "country is mandatory") String country, Date creation_date, Date updated_date,
			User user) {
		super();
		this.street = street;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.creation_date = creation_date;
		this.updated_date = updated_date;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	@PrePersist
	protected void onCreate() {
	    this.creation_date = new Date();
	    this.updated_date = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
	    this.updated_date = new Date();
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", postalCode=" + postalCode + ", city=" + city
				+ ", country=" + country + ", creation_date=" + creation_date + ", updated_date=" + updated_date
				+ ", user=" + user + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, creation_date, id, postalCode, street, updated_date, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(creation_date, other.creation_date) && Objects.equals(id, other.id)
				&& Objects.equals(postalCode, other.postalCode) && Objects.equals(street, other.street)
				&& Objects.equals(updated_date, other.updated_date) && Objects.equals(user, other.user);
	}
	
	
}
