package io.matel.youtube.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class SubscriptionTrans {

    @Id
    private String channelId;
    private int userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creation;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime update;
}
