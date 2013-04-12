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
 * entity representing a web source. It is ready for saving everything in a DB
 * as well is you use JPA.
 * 
 * TODO : add hibernate as a dependency and uncomment the lines if you want to
 * save the source in DB as a blob.
 * 
 */
@Entity
@Table(name = "source")
public class Source {
	// private static SimpleLogger logger = CatPawLogger.getLogger();
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "uuid")
	private String uuid;

	@Column(name = "data", columnDefinition = "MEDIUMBLOB")
	private Blob sourceBlob;

	@Transient
	private String source;

	@Column(name = "timestamp")
	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public Source() {
	}

	public Source(byte[] src, String uuid) {
		this.uuid = uuid;
		this.timestamp = System.currentTimeMillis();
		// this.sourceBlob = Hibernate.createBlob(src);
	}

	public Source(String src, String uuid) {
		this.uuid = uuid;
		this.timestamp = System.currentTimeMillis();
		// this.sourceBlob = Hibernate.createBlob(src.getBytes());
		this.source = src;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Blob getSourceBlob() {
		return sourceBlob;
	}

	public String getSource() {
		// logger.entering();
		if (source == null && sourceBlob != null) {
			try {
				source = new String(sourceBlob.getBytes(1L, (int) sourceBlob
						.length()));
			} catch (SQLException e) {
				// logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		//Not logging the value of source because this clogs the detailed logs.
		// logger.exiting();
		return source;
	}

	public void setSources(String src) {
		this.source = src;
	}

	public void setSourcesBlob(Blob src) {
		this.sourceBlob = src;
	}

	public long getId() {
		return id;
	}
}
