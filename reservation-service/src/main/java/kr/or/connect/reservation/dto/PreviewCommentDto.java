package kr.or.connect.reservation.dto;

import java.sql.Date;
import java.util.List;

public class PreviewCommentDto {
	private long cid;
	private float score;
	private String comment;
	private String name;
	private String nickname;
	private Date modifyDate;
	private List<ImageDto> images;

	public PreviewCommentDto() {

	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public List<ImageDto> getImages() {
		return images;
	}

	public void setImages(List<ImageDto> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "PreviewCommentDto{" +
				"cid=" + cid +
				", score=" + score +
				", comment='" + comment + '\'' +
				", name='" + name + '\'' +
				", nickname='" + nickname + '\'' +
				", modifyDate=" + modifyDate +
				", images=" + images +
				'}';
	}
}
