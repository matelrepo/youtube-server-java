package io.matel.youtube.controller;

import io.matel.youtube.domain.VideoDetailsMaster;
import io.matel.youtube.repository.VideoDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api")
public class WebController {

    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @GetMapping("all")
    public List<VideoDetailsMaster> getVideos(){
        return videoDetailsRepository.findAll().subList(0,15);
    }

}
