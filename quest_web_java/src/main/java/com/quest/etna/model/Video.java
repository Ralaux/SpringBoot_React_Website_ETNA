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
@Table(name="video")
public class Video {
	@Column(name = "id", unique=true, nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "yt_channel is mandatory")
	@Column(name = "yt_channel", length = 50, nullable = false)
	private String yt_channel;
	
	@NotEmpty(message = "url is mandatory")
	@Column(name = "url", length = 50, nullable = false)
	private String url;
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "updated_date")
	private Date updated_date;
	
	public Video() {};

	public Video(@NotEmpty(message = "yt_channel is mandatory") String yt_channel,
			@NotEmpty(message = "url is mandatory") String url) {
		this.yt_channel = yt_channel;
		this.url = url;
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

	public String getYt_channel() {
		return yt_channel;
	}

	public void setYt_channel(String yt_channel) {
		this.yt_channel = yt_channel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", yt_channel=" + yt_channel + ", url=" + url + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, url, yt_channel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(url, other.url) && Objects.equals(yt_channel, other.yt_channel);
	};
	
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
