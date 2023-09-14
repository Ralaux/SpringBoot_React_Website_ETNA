package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="charac")
public class Character {
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "name is mandatory")
	@Column(name = "name", unique=true, length = 50, nullable = false)
	private String name;
	
	@Column(name = "weight")
	private int weight;
	
	@Column(name = "speed")
	private float speed;
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "updated_date")
	private Date updated_date;

	@OneToOne
	@JoinColumn(name = "up_tilt_id", referencedColumnName="id", nullable = false)
	private UpTilt up_tilt;
	
	@OneToOne
	@JoinColumn(name = "forward_id", referencedColumnName="id", nullable = false)
	private ForwardTilt forward_tilt;
	
	@OneToOne
	@JoinColumn(name = "down_tilt_id", referencedColumnName="id", nullable = false)
	private DownTilt down_tilt;
	
	@OneToOne
	@JoinColumn(name = "dash_attack_id", referencedColumnName="id", nullable = false)
	private DashAttack dash_attack;
	
	@OneToOne
	@JoinColumn(name = "video_id", referencedColumnName="id", nullable = false)
	private Video video;
	
	public Character() {};

	public Character(@NotEmpty(message = "name is mandatory") String name, int weight, float speed, Date creation_date,
			Date updated_date, UpTilt up_tilt, ForwardTilt forward_tilt, DownTilt down_tilt, DashAttack dash_attack,
			Video video) {
		super();
		this.name = name;
		this.weight = weight;
		this.speed = speed;
		this.creation_date = creation_date;
		this.updated_date = updated_date;
		this.up_tilt = up_tilt;
		this.forward_tilt = forward_tilt;
		this.down_tilt = down_tilt;
		this.dash_attack = dash_attack;
		this.video = video;
	}



	public UpTilt getUp_tilt() {
		return up_tilt;
	}

	public void setUp_tilt(UpTilt up_tilt) {
		this.up_tilt = up_tilt;
	}

	public ForwardTilt getForward_tilt() {
		return forward_tilt;
	}

	public void setForward_tilt(ForwardTilt forward_tilt) {
		this.forward_tilt = forward_tilt;
	}

	public DownTilt getDown_tilt() {
		return down_tilt;
	}

	public void setDown_tilt(DownTilt down_tilt) {
		this.down_tilt = down_tilt;
	}

	public DashAttack getDash_attack() {
		return dash_attack;
	}

	public void setDash_attack(DashAttack dash_attack) {
		this.dash_attack = dash_attack;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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
	public String toString() {
		return "Character [id=" + id + ", name=" + name + ", weight=" + weight + ", speed=" + speed + ", creation_date="
				+ creation_date + ", updated_date=" + updated_date + ", up_tilt=" + up_tilt + ", forward_tilt="
				+ forward_tilt + ", down_tilt=" + down_tilt + ", dash_attack=" + dash_attack + ", video=" + video + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(creation_date, dash_attack, down_tilt, forward_tilt, id, name, speed, up_tilt, updated_date,
				video, weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		return Objects.equals(creation_date, other.creation_date) && Objects.equals(dash_attack, other.dash_attack)
				&& Objects.equals(down_tilt, other.down_tilt) && Objects.equals(forward_tilt, other.forward_tilt)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Float.floatToIntBits(speed) == Float.floatToIntBits(other.speed)
				&& Objects.equals(up_tilt, other.up_tilt) && Objects.equals(updated_date, other.updated_date)
				&& Objects.equals(video, other.video) && weight == other.weight;
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
