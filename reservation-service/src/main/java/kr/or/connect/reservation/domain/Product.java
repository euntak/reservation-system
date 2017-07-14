package kr.or.connect.reservation.domain;

import java.sql.Date;

public class Product {
	private long id;
	private long categoryId;
	private String name;
	private String description;
	private Date sales_start;
	private Date sales_end;
	private int sales_flag;
	private String event;
	private Date create_date;
	private Date modify_date;

	public Product() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getSales_start() {
		return sales_start;
	}

	public void setSales_start(Date sales_start) {
		this.sales_start = sales_start;
	}

	public Date getSales_end() {
		return sales_end;
	}

	public void setSales_end(Date sales_end) {
		this.sales_end = sales_end;
	}

	public int getSales_flag() {
		return sales_flag;
	}

	public void setSales_flag(int sales_flag) {
		this.sales_flag = sales_flag;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", categoryId=" + categoryId + ", name=" + name + ", description=" + description
				+ ", sales_start=" + sales_start + ", sales_end=" + sales_end + ", sales_flag=" + sales_flag
				+ ", event=" + event + ", create_date=" + create_date + ", modify_date=" + modify_date + "]";
	}

}
