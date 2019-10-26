package io.matel.youtube;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class VideoMaster {

    public VideoMaster(String videoId, String channelId, String channelTitle, String duration, String videoDescription,
                       String videoTitle, String tagsList, ZonedDateTime recordingDate, ZonedDateTime publishedAt,
                       StatisticsMaster statistics, String thumbnailsList) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.duration = duration;
        this.videoDescription = videoDescription;
        this.videoTitle = videoTitle;
        this.tagsList = tagsList;
        this.recordingDate = recordingDate;
        this.publishedAt = publishedAt;
        this.statistics = statistics;
        this.thumbnailsList = thumbnailsList;
    }

    @Id
    private String videoId;
    private String channelId;
    private String channelTitle;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String videoDescription;
    private String videoTitle;
    private String tagsList;

    private ZonedDateTime recordingDate;
    private ZonedDateTime publishedAt;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creation;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime update;

    StatisticsMaster statistics;
    String thumbnailsList;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getTagsList() {
        return tagsList;
    }

    public void setTagsList(String tagsList) {
        this.tagsList = tagsList;
    }

    public ZonedDateTime getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(ZonedDateTime recordingDate) {
        this.recordingDate = recordingDate;
    }

    public ZonedDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(ZonedDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public StatisticsMaster getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsMaster statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "VideoMaster{" +
                "videoId='" + videoId + "\n" +
                ", channelId='" + channelId + "\n" +
                ", channelTitle='" + channelTitle + "\n" +
                ", duration='" + duration + "\n" +
                ", videoDescription='" + videoDescription.substring(0,20) + "... " + "\n" +
                ", videoTitle='" + videoTitle + "\n" +
                ", tagsList=" + tagsList + "\n" +
                ", recordingDate=" + recordingDate + "\n" +
                ", publishedAt=" + publishedAt + "\n" +
                ", statistics=" + statistics.toString() + "\n" +
                ", thumbnailsList=" + thumbnailsList + "\n" +
                '}';
    }
}
