package io.matel.youtube.repository;

import io.matel.youtube.domain.ActivityTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityTrans, String> {
    boolean existsActivityTransByVideoId(String videoId);
}
