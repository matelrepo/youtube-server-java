package io.matel.youtube.repository;

import io.matel.youtube.domain.SubscriptionTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepo extends JpaRepository<SubscriptionTrans,String> {
}
