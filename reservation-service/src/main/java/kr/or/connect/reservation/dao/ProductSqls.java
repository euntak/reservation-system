package kr.or.connect.reservation.dao;

public class ProductSqls {

	final static String SELECT_BY_CATEGORY_ID =
			"SELECT P.id, P.category_id, P.name, P.description, DI.place_name, F.id AS file_id"
			+ " FROM product AS P"
			+ " JOIN display_info AS DI"
			+ " ON DI.id = P.id"
			+ " JOIN product_image AS PI"
			+ " ON P.id = PI.product_id"
			+ " JOIN file AS F"
			+ " ON F.id = PI.file_id"
			+ " WHERE P.category_id = :category_id"
			+ " AND PI.type=1"
			+ " ORDER BY P.id DESC"
			+ " LIMIT :offset, :limit";


	final static String SELECT_ALL =
			"SELECT P.id, P.category_id, P.name, P.description, DI.place_name, F.id AS file_id"
			+ " FROM product AS P"
			+ " JOIN display_info AS DI"
			+ " ON DI.product_id = P.id"
			+ " JOIN product_image AS PI"
			+ " ON P.id = PI.product_id"
			+ " JOIN file AS F"
			+ " ON F.id = PI.file_id"
			+ " WHERE PI.type=1"
			+ " ORDER BY P.id DESC"
			+ " LIMIT :offset, :limit";

	final static String SELECT_TOTAL_COUNT_BY_CATEGORY_ID =
			"SELECT count(*)"
			+ " FROM product"
			+ " WHERE category_id=:category_id";

	final static String SELECT_TOTAL_COUNT =
			"SELECT count(*)"
			+ " FROM product";

	final static String SELECT_DETAIL_PRODUCT_BY_ID =
			"SELECT "
			+ " P.id, P.name, P.description, P.event, P.sales_start, P.sales_end, P.sales_flag, "
			+ " DI.observation_time, DI.place_name, DI.place_lot, DI.place_street, DI.tel, DI.homepage, DI.email, "
			+ " PD.content"
			+ " FROM product AS P"
			+ " JOIN product_detail AS PD ON P.id = PD.product_id"
			+ " JOIN display_info AS DI ON P.id = DI.product_id"
			+ " WHERE P.id = :pid";

	////////////////////////////////여기
	final static String DETAIL_PRODUCT_IMGAE_LIST =
			"SELECT F.id AS fileId, F.file_name, F.save_file_name, F.file_length, F.content_type"
			+ " FROM product AS P"
			+ " JOIN product_image AS PI ON P.id = PI.product_id"
			+ " JOIN file AS F ON PI.file_id = F.id"
			+ " WHERE F.delete_flag = 0"
			+ " AND F.file_length > 0"
			+ " AND P.id = :pid"
			+ " ORDER BY PI.type DESC";

	final static String PREVIEW_COMMENTS_BY_PRODUCT_ID =
			"SELECT RUC.id AS cid, RUC.comment, RUC.score, RUC.modify_date,"
			+ " P.name,"
			+ " U.nickname"
			+ " FROM reservation_user_comment AS RUC"
			+ " JOIN product AS P"
			+ " ON RUC.product_id = P.id"
			+ " JOIN users AS U"
			+ " ON RUC.user_id = U.id"
			+ " WHERE P.id = :pid"
			+ " ORDER BY RUC.id DESC"
			+ " LIMIT :offset, :limit";

	////////////////////////////////여기
	final static String PREVIEW_COMMENTS_IMAGE_LIST =
			"SELECT F.id as fileId, F.file_name, F.save_file_name, F.file_length, F.content_type"
			+ " FROM product AS P"
			+ " JOIN reservation_user_comment AS RUC"
			+ " ON RUC.product_id = P.id"
			+ " JOIN reservation_user_comment_image AS RUCI"
			+ " ON RUC.id = RUCI.reservation_user_comment_id"
			+ " JOIN file AS F"
			+ " ON RUCI.file_id = F.id"
			+ " WHERE F.delete_flag = 0"
			+ " AND F.file_length > 0"
			+ " AND P.id = :pid"
			+ " AND RUC.id = :cid"
			+ " ORDER BY F.id DESC";

	final static String PREVIEW_COMMENTS_TOTAL_VALUES =
			"SELECT P.id AS pid, P.name, "
			+ " ROUND(AVG(RUC.score), 1) AS average, COUNT(RUC.id) AS totalCount"
			+ " FROM product AS P"
			+ " JOIN reservation_user_comment AS RUC"
			+ " ON P.id = RUC.product_id"
			+ " WHERE RUC.product_id = :pid";

	final static String DETAIL_PRODUCT_PRICE =

			"SELECT PR.id, PR.product_id, PR.price_type, PR.price, PR.discount_rate,"
			+ " PR.create_date, PR.modify_date "
			+ " FROM product AS P"
			+ " JOIN product_price AS PR "
			+ " ON P.id = PR.product_id"
			+ " WHERE PR.product_id = :pid";
}
