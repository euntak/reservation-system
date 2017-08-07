package kr.or.connect.reservation.domain;

import java.sql.Date;
import java.sql.Timestamp;

public class FileDomain {
	private long id;
	private long userId;

	private String fileName;
	private String saveFileName;

	private long fileLength;
	private String contentType;
	private int deleteFlag;

	private Timestamp createDate;
	private Timestamp modifyDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "File [id=" + id + ", userId=" + userId + ", fileName=" + fileName + ", saveFileName=" + saveFileName
				+ ", fileLength=" + fileLength + ", contentType=" + contentType + ", deleteFlag=" + deleteFlag
				+ ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + deleteFlag;
		result = prime * result + (int) (fileLength ^ (fileLength >>> 32));
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((modifyDate == null) ? 0 : modifyDate.hashCode());
		result = prime * result + ((saveFileName == null) ? 0 : saveFileName.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

}
