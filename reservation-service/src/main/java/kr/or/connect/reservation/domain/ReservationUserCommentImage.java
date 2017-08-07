package kr.or.connect.reservation.domain;

public class ReservationUserCommentImage {
	private Long id;
	private Long reservationUserCommentId;
	private Long fileId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReservationUserCommentId() {
		return reservationUserCommentId;
	}
	public void setReservationUserCommentId(Long reservationUserCommentId) {
		this.reservationUserCommentId = reservationUserCommentId;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
}
