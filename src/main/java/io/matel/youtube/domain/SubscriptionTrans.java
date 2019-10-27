package io.matel.youtube.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class SubscriptionTrans {

    public SubscriptionTrans(){};

    public SubscriptionTrans(String channelId, int userId) {
        this.channelId = channelId;
        this.userId = userId;
    }

    @Id
    private String channelId;
    private int userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creation;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime update;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OffsetDateTime getCreation() {
        return creation;
    }

    public void setCreation(OffsetDateTime creation) {
        this.creation = creation;
    }

    public OffsetDateTime getUpdate() {
        return update;
    }

    public void setUpdate(OffsetDateTime update) {
        this.update = update;
    }

}
