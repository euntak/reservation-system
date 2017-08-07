package kr.or.connect.reservation.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class CommentDto {
    private long productId;
    private long userId;
    private double score;
    private String comment;
    private MultipartFile[] files;
    private List<Long> fileIdList;
    private String createDate;
    private String modifyDate;

    public CommentDto() {
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ReviewDto{" +
                "userId=" + userId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", files=" + Arrays.toString(files) +
                '}';
    }

    public List<Long> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
