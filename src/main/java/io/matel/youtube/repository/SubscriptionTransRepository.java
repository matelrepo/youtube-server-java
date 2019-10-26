package io.matel.youtube.repository;

import io.matel.youtube.domain.SubscriptionTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTransRepository extends JpaRepository<SubscriptionTrans,String> {
}
