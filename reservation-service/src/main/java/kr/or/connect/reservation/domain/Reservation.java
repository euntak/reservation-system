package kr.or.connect.reservation.domain;

public class Reservation {
	private long productId;
	private long userId;
	private int generalTicketCount;
	private int youthTicketCount;
	private int childTicketCount;
	private String reservationName;
	private String reservationTel;
	private String reservationEmail;
	private String reservationDate;
	private long reservationType;
	private String createDate;
	private String modifyDate;

	public Reservation() {
	}

	public String getReservationEmail() {
		return reservationEmail;
	}

	public void setReservationEmail(String reservationEmail) {
		this.reservationEmail = reservationEmail;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getGeneralTicketCount() {
		return generalTicketCount;
	}

	public void setGeneralTicketCount(int generalTicketCount) {
		this.generalTicketCount = generalTicketCount;
	}

	public int getYouthTicketCount() {
		return youthTicketCount;
	}

	public void setYouthTicketCount(int youthTicketCount) {
		this.youthTicketCount = youthTicketCount;
	}

	public int getChildTicketCount() {
		return childTicketCount;
	}

	public void setChildTicketCount(int childTicketCount) {
		this.childTicketCount = childTicketCount;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	public String getReservationTel() {
		return reservationTel;
	}

	public void setReservationTel(String reservationTel) {
		this.reservationTel = reservationTel;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public long getReservationType() {
		return reservationType;
	}

	public void setReservationType(long reservationType) {
		this.reservationType = reservationType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "Reservation [productId=" + productId + ", userId=" + userId + ", generalTicketCount="
				+ generalTicketCount + ", youthTicketCount=" + youthTicketCount + ", childTicketCount="
				+ childTicketCount + ", reservationName=" + reservationName + ", reservationTel=" + reservationTel
				+ ", reservationEmail=" + reservationEmail + ", reservationDate=" + reservationDate
				+ ", reservationType=" + reservationType + ", createDate=" + createDate + ", modifyDate=" + modifyDate
				+ "]";
	}

}
