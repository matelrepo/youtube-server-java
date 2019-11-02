package io.matel.youtube.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class VideoDetailsMaster {

    public VideoDetailsMaster(){};

    public VideoDetailsMaster(String videoId, String channelId, String channelTitle, String duration, String videoDescription,
                              String videoTitle, List<String> tagsList, ZonedDateTime publishedAt,
                              StatisticsMaster statistics, String thumbnailsList, String defaultLanguage, String defaultAudioLanguage) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.duration = duration;
        this.videoDescription = videoDescription;
        this.videoTitle = videoTitle;
        this.tagsList = tagsList;
        if (tags != null)
            if (tags.length() > 0)
                this.tags = String.join(",", tagsList);
        this.publishedAt = publishedAt;
        this.statistics = statistics;
        this.thumbnailsList = thumbnailsList;
        this.defaultLanguage = defaultLanguage;
        this.defaultAudioLanguage = defaultAudioLanguage;
    }

    @Id
    private String videoId;
    private String channelId;
    private String channelTitle;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String videoDescription;
    private String videoTitle;

    @Transient
    private List<String> tagsList;

    @Column(columnDefinition= "TEXT")
    private String tags ="";

    private ZonedDateTime publishedAt;

    private String defaultAudioLanguage;
    private String defaultLanguage;

    public String getDefaultAudioLanguage() {
        return defaultAudioLanguage;
    }

    public void setDefaultAudioLanguage(String defaultAudioLanguage) {
        this.defaultAudioLanguage = defaultAudioLanguage;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creation;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime update;

    StatisticsMaster statistics;

    @Column(columnDefinition= "TEXT")
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

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public String getThumbnailsList() {
        return thumbnailsList;
    }

    public void setThumbnailsList(String thumbnailsList) {
        this.thumbnailsList = thumbnailsList;
    }

    @Override
    public String toString() {
        return "VideoMaster{" + "\n" +
                "videoId='" + videoId + "\n" +
                ", channelId='" + channelId + "\n" +
                ", channelTitle='" + channelTitle + "\n" +
                ", duration='" + duration + "\n" +
                ", videoDescription='" + videoDescription.substring(0,20) + "... " + "\n" +
                ", videoTitle='" + videoTitle + "\n" +
                ", tagsList=" + tagsList + "\n" +
                ", publishedAt=" + publishedAt + "\n" +
                ", statistics=" + statistics.toString() + "\n" +
                ", thumbnailsList=" + thumbnailsList + "\n" +
                '}';
    }

    public String getTags() {
        return  tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
