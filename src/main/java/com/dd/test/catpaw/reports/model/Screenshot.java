package com.dd.test.catpaw.reports.model;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;




/**
 * entity representing a web screenshot. It is ready for saving everything in a
 * DB as well is you use JPA.
 * 
 * TODO : add hibernate as a dependency and uncomment the lines if you want to
 * save the screenshot in DB.
 * 
 */
@Entity
@Table(name = "screenshot")
public class Screenshot {
	// private static SimpleLogger logger = CatPawLogger.getLogger();
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "uuid")
	private String uuid;

	@Transient
	private byte[] screenshot;

	@Column(name = "data", columnDefinition = "MEDIUMBLOB")
	private Blob screenshotBlob;

	@Column(name = "timestamp")
	private long timestamp;

	public Screenshot() {

	}

	public Screenshot(byte[] img, String uuid) {
		timestamp = System.currentTimeMillis();
		setScreenshot(img);
		this.uuid = uuid;
	}

	public Screenshot(byte[] img, String uuid, long timestamp) {
		this.timestamp = timestamp;
		setScreenshot(img);
		this.uuid = uuid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setScreenshot(byte[] image) {
		this.screenshot = image;
		// this.screenshotBlob = Hibernate.createBlob(image);
	}

	public byte[] getScreenshot() {
		// logger.entering();
		if (screenshot == null && screenshotBlob != null) {
			try {
				this.screenshot = screenshotBlob.getBytes(1,
						(int) screenshotBlob.length());
			} catch (SQLException e) {
				// logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		// logger.exiting();
		return this.screenshot;
	}

	protected Blob getScreenshotBlob() {
		return screenshotBlob;
	}

	protected void setScreenshotBlob(Blob screenshotBlob) {
		this.screenshotBlob = screenshotBlob;

	}

	public long getId() {
		return id;
	}

}
