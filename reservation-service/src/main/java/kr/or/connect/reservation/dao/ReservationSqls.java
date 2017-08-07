package kr.or.connect.reservation.dao;

public class ReservationSqls {
    final static String SELECT_RESERVATION =
    		"SELECT P.id AS pid, RI.id AS rid"
    		+ ", P.name, P.sales_start, P.sales_end, DI.place_name "
    		+ ", RI.id, RI.reservation_type, RI.general_ticket_count, RI.youth_ticket_count, RI.child_ticket_count"
    		+ ", (RI.general_ticket_count + RI.youth_ticket_count + RI.child_ticket_count) AS total_ticket_count"
    		+ ", SUM(IF(PR.price_type=1, (Floor(PR.price - PR.price * PR.discount_rate) * RI.general_ticket_count), 0)) AS general_total_price"
    		+ ", SUM(IF(PR.price_type=2, (Floor(PR.price - PR.price * PR.discount_rate) * RI.youth_ticket_count), 0)) AS youth_total_price"
    		+ ", SUM(IF(PR.price_type=3, (Floor(PR.price - PR.price * PR.discount_rate) * RI.child_ticket_count), 0)) AS child_total_price"
    		+ " FROM reservation_info AS RI"
    		+ " JOIN product AS P"
    		+ " ON P.id = RI.product_id"
    		+ " JOIN product_price AS PR"
    		+ " ON PR.product_id = RI.product_id"
    		+ " JOIN display_info AS DI"
    		+ " ON DI.product_id = RI.product_id"
    		+ " WHERE 1=1"
    		+ " AND RI.user_id = :id"
    		+ " GROUP BY RI.id, DI.place_name"
			+ " ORDER BY RI.id DESC";

    final static String UPDATE_RESERVATION =
			"UPDATE reservation_info"
			+ " SET reservation_type=:cancel_type"
			+ " WHERE id=:id"
			+ " AND reservation_type=:type";
	}
