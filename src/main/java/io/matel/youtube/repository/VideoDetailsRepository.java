package io.matel.youtube.repository;

import io.matel.youtube.domain.VideoDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideoDetailsRepository extends JpaRepository<VideoDetailsMaster, String> {
    boolean existsVideoDetailsMasterByVideoId(String videoId);
}
