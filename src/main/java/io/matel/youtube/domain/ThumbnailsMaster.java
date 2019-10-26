package io.matel.youtube.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ThumbnailsMaster {

    @Id
    private String videoId;
    private String type;
    private int height;
    private int width;
    private String url;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ThumbnailsMaster{" +
                "videoId='" + videoId + '\'' +
                ", type='" + type + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", url='" + url + '\'' +
                '}';
    }
}
