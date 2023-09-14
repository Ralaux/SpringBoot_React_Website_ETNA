package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="dash_attack")
public class DashAttack {
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", unique=true, length = 50)
	private String name;
	
	@NotEmpty(message = "damage is mandatory")
	@Column(name = "damage", nullable = false)
	private int damage;
	
	@NotEmpty(message = "frame is mandatory")
	@Column(name = "frame", nullable = false)
	private int frame;
	
	@NotEmpty(message = "endlag is mandatory")
	@Column(name = "endlag", nullable = false)
	private int endlag;
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "updated_date")
	private Date updated_date;
	
	public DashAttack() {};

	public DashAttack(String name,
			@NotEmpty(message = "damage is mandatory") int damage, @NotEmpty(message = "frame is mandatory") int frame,
			@NotEmpty(message = "endlag is mandatory") int endlag) {
		this.name = name;
		this.damage = damage;
		this.frame = frame;
		this.endlag = endlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public int getEndlag() {
		return endlag;
	}

	public void setEndlag(int endlag) {
		this.endlag = endlag;
	}

	public Integer getId() {
		return id;
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
		return Objects.hash(damage, endlag, frame, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DashAttack other = (DashAttack) obj;
		return damage == other.damage && endlag == other.endlag
				&& frame == other.frame && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "DashAttack [id=" + id + ", name=" + name + ", damage=" + damage + ", frame=" + frame + ", endlag=" + endlag + "]";
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
