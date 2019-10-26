package io.matel.youtube.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
public class ActivityTrans {

    @Id
    private String videoId;
    private ZonedDateTime publishedAt;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creation;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime update;

    public ActivityTrans(String videoId, ZonedDateTime publishedAt) {
        this.videoId = videoId;
        this.publishedAt = publishedAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public ZonedDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(ZonedDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "ActivityTrans{" +
                "videoId='" + videoId + '\'' +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
