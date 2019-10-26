package io.matel.youtube.domain;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Embeddable
public class StatisticsMaster {

    public StatisticsMaster(BigInteger commentCount, BigInteger disLikeCount, BigInteger favoriteCount, BigInteger likeCount, BigInteger viewCount) {
        this.commentCount = commentCount;
        this.disLikeCount = disLikeCount;
        this.favoriteCount = favoriteCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }

    private BigInteger commentCount;
    private BigInteger disLikeCount;
    private BigInteger favoriteCount;
    private BigInteger likeCount;
    private BigInteger viewCount;

    public BigInteger getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(BigInteger commentCount) {
        this.commentCount = commentCount;
    }

    public BigInteger getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(BigInteger disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    public BigInteger getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(BigInteger favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {
        this.likeCount = likeCount;
    }

    public BigInteger getViewCount() {
        return viewCount;
    }

    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "StatisticsMaster{" +
                "commentCount=" + commentCount +
                ", disLikeCount=" + disLikeCount +
                ", favoriteCount=" + favoriteCount +
                ", likeCount=" + likeCount +
                ", viewCount=" + viewCount +
                '}';
    }
}
