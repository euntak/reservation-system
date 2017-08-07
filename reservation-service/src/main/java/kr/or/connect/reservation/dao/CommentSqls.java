package kr.or.connect.reservation.dao;

public class CommentSqls {

	final static String updateCommentImageFileStatus = "UPDATE FILE SET delete_flag = 0 WHERE id = :fileId";
}
