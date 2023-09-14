package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.*;
import com.quest.etna.config.WebSecurityConfig;

@Entity
@Table(name="\"user\"")
public class User {
	
	public enum UserRole {
		ROLE_USER("ROLE_USER"), 
		ROLE_ADMIN("ROLE_ADMIN");
		
		private String value;

	    private UserRole(String string) {
	        this.value = string;
	    }

	    public String getValue() {
	        return this.value;
	    }
	}
	
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "username is mandatory")
	@Column(name = "username", unique=true, length = 255, nullable = false)
	private String username;
	
	@NotEmpty(message = "password is mandatory")
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	
	@Column(name = "role")
	private String role = UserRole.ROLE_USER.toString();
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "updated_date")
	private Date updated_date;
	
	public User() {};
	
	public User(Integer id, String username, String password, String role, Date creation_date, Date updated_date) {
		this.id = id;
		this.username = username;
		WebSecurityConfig webSecurityConfig = new WebSecurityConfig();
		this.password = webSecurityConfig.passwordEncoder().encode(password);
		if (role != null) {
			this.role = role;
		}
		this.creation_date = creation_date;
		this.updated_date = updated_date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		WebSecurityConfig webSecurityConfig = new WebSecurityConfig();
		this.password = webSecurityConfig.passwordEncoder().encode(password);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	@Override
	public int hashCode() {
		return Objects.hash(creation_date, id, password, role, updated_date, username);
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
		return Objects.equals(creation_date, other.creation_date)
				&& Objects.equals(id, other.id) && Objects.equals(password, other.password)
				&& Objects.equals(role, other.role) && Objects.equals(updated_date, other.updated_date)
				&& Objects.equals(username, other.username);
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role +
				", creation_date=" + creation_date + ", updated_date=" + updated_date + "]";
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

}
