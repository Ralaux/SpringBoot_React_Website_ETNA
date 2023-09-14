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

@Entity
@Table(name="comment")
public class Comment {
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "body is mandatory")
	@Column(name = "body", columnDefinition = "text", nullable = false)
	private String body;
	
	@ManyToOne
	@JoinColumn(name = "character_id", referencedColumnName="id", nullable = false)
	private Character character;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
	private User user;
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "updated_date")
	private Date updated_date;
	
	public Comment() {};
	
	public Comment(Integer id, @NotEmpty(message = "body is mandatory") String body, Character character, User user,
			Date creation_date, Date updated_date) {
		this.id = id;
		this.body = body;
		this.character = character;
		this.user = user;
		this.creation_date = creation_date;
		this.updated_date = updated_date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(body, character, creation_date, id, updated_date, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		return Objects.equals(body, other.body) && Objects.equals(character, other.character)
				&& Objects.equals(creation_date, other.creation_date) && Objects.equals(id, other.id)
				&& Objects.equals(updated_date, other.updated_date) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", body=" + body + ", character=" + character + ", user=" + user
				+ ", creation_date=" + creation_date + ", updated_date=" + updated_date + "]";
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
