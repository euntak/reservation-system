package kr.or.connect.reservation.dto;

import java.sql.Date;

public class MyReservationDto {
	private Long pid;
	private Long rid;

	private String name;
	private Date salesStart;
	private Date salesEnd;
	private String placeName;
	private int reservationType;

	private int generalTotalPrice;
	private int youthTotalPrice;
	private int childTotalPrice;

	private int generalTicketCount;
	private int youthTicketCount;
	private int childTicketCount;

	private int totalTicketCount;
	private int totalPrice;

	public MyReservationDto() {

	}

	public MyReservationDto(String name, int reservationType) {
		this.name = name;
		this.reservationType = reservationType;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getSalesStart() {
		return salesStart;
	}

	public void setSalesStart(Date salesStart) {
		this.salesStart = salesStart;
	}

	public Date getSalesEnd() {
		return salesEnd;
	}

	public void setSalesEnd(Date salesEnd) {
		this.salesEnd = salesEnd;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public int getReservationType() {
		return reservationType;
	}

	public void setReservationType(int reservationType) {
		this.reservationType = reservationType;
	}

	public int getGeneralTotalPrice() {
		return generalTotalPrice;
	}

	public void setGeneralTotalPrice(int generalTotalPrice) {
		this.generalTotalPrice = generalTotalPrice;
	}

	public int getYouthTotalPrice() {
		return youthTotalPrice;
	}

	public void setYouthTotalPrice(int youthTotalPrice) {
		this.youthTotalPrice = youthTotalPrice;
	}

	public int getChildTotalPrice() {
		return childTotalPrice;
	}

	public void setChildTotalPrice(int childTotalPrice) {
		this.childTotalPrice = childTotalPrice;
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

	public int getTotalTicketCount() {
		return totalTicketCount;
	}

	public void setTotalTicketCount(int totalTicketCount) {
		this.totalTicketCount = totalTicketCount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
