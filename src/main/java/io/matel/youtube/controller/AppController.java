package io.matel.youtube.controller;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import io.matel.youtube.domain.ActivityTrans;
import io.matel.youtube.domain.StatisticsMaster;
import io.matel.youtube.domain.VideoDetailsMaster;
import io.matel.youtube.repository.ActivityRepository;
import io.matel.youtube.repository.VideoDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppController {

//    private final int NUM_DAYS_HISTO = 1;
    private final ZoneId  zoneId = ZoneId.of("Asia/Bangkok");

    @Value("${api.key1}")
    private String apiKey;

    private YouTube youTube;
    private YouTube.Activities.List listActivitiesRequest;
    private ActivityListResponse listResponse;
    private int trailingCheckAlreadyExists =0;

    private List<String> ignoreList = new ArrayList<>();


    public void setYouTube(YouTube youTube){
        this.youTube = youTube;
    }

    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @Autowired
    ActivityRepository activityRepository;

    public VideoDetailsMaster getVideoDetails(String videoId) throws IOException {

        if(videoDetailsRepository.existsVideoDetailsMasterByVideoId(videoId)){
            System.out.println("Video details " + videoId + " already acquired");
            return videoDetailsRepository.findById(videoId).get();
        }else {
            YouTube.Videos.List listVideosRequest = youTube.videos().list("statistics, contentDetails, snippet, recordingDetails");
            listVideosRequest.setId(videoId); // add list of video IDs here
            listVideosRequest.setKey(this.apiKey);

            listVideosRequest.setFields("items(contentDetails(duration), " +
                    "id, snippet(channelId, channelTitle, description, title, publishedAt, tags, defaultLanguage, defaultAudioLanguage, thumbnails), statistics)");

            VideoListResponse listResponse = listVideosRequest.execute();
            Video video = listResponse.getItems().get(0);
//            if(video.getSnippet().getChannelId().equals("UC3VydBGBl132baPCLeDspMQ")){
//            System.out.println("coucou");
//            }
            String thumbDetails = youTube.getJsonFactory().toString( video.getSnippet().getThumbnails());

            StatisticsMaster statistics = new StatisticsMaster(video.getStatistics().getCommentCount(),
                    video.getStatistics().getDislikeCount(), video.getStatistics().getFavoriteCount(),
                    video.getStatistics().getLikeCount(), video.getStatistics().getViewCount());

            VideoDetailsMaster videoDetailsMaster = new VideoDetailsMaster(video.getId(), video.getSnippet().getChannelId(),
                    video.getSnippet().getChannelTitle(), video.getContentDetails().getDuration(),
                    video.getSnippet().getDescription(), video.getSnippet().getTitle(),
                    video.getSnippet().getTags(),
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(video.getSnippet().getPublishedAt().getValue()), zoneId),
                    statistics, thumbDetails, video.getSnippet().getDefaultLanguage(), video.getSnippet().getDefaultAudioLanguage());

            if(videoDetailsMaster.getDefaultAudioLanguage() == null
                    || videoDetailsMaster.getDefaultAudioLanguage().contains("en")
                    || videoDetailsMaster.getDefaultAudioLanguage().equals("fr")
                    || videoDetailsMaster.getDefaultAudioLanguage().equals("th") ) {

                if (checkCanSave(videoDetailsMaster.getChannelId(), videoDetailsMaster.getVideoTitle()))
                    videoDetailsRepository.save(videoDetailsMaster);

            }
            return videoDetailsMaster;
        }
    }

    private boolean checkCanSave(String channelId, String videoTitle){
        // for the channel PackT group videos by course
//
//        if(channelId.equals("UC3VydBGBl132baPCLeDspMQ") && videoTitle.contains(":")){
//            for(int i=0; i< ignoreList.size(); i++){
//                if(videoTitle.charAt(videoTitle.indexOf(":")) == String.valueOf(" ").charAt(0)){
//                    videoTitle = videoTitle.substring(0, videoTitle.length()-2) + ":";
//                }
//                if(videoTitle.substring(0,videoTitle.indexOf(":")).equals(ignoreList.get(i))){
//                    return false;
//                }
//            }
//            ignoreList.add(videoTitle.substring(0,videoTitle.indexOf(":")));
//        }
        return true;
    }

    public void getActivityByChannelId(String channelId) throws IOException {
        listActivitiesRequest = youTube.activities().list("contentDetails, snippet, id");
        listActivitiesRequest.setChannelId(channelId); // add list of video IDs here
        listActivitiesRequest.setKey(this.apiKey);
        listActivitiesRequest.setFields("nextPageToken, items(contentDetails(upload(videoId))," +
                        "snippet(type,publishedAt,title,description,thumbnails(medium(url))))");

        boolean validRequest = true;
        ZonedDateTime timeFromYoutube = ZonedDateTime.now(zoneId).minusDays(2);
        Instant now = Instant.now();
        ZonedDateTime today = ZonedDateTime.ofInstant(now, zoneId);
        while(Duration.between(timeFromYoutube, today).toDays()<3) {
            System.out.println("Mapping " + channelId + " (" + timeFromYoutube + " " + today + " " + Duration.between(timeFromYoutube, today).toDays() +")");
            listResponse = listActivitiesRequest.execute();
          try {
               timeFromYoutube = ZonedDateTime.ofInstant(Instant
                       .ofEpochMilli(listResponse.getItems().get(listResponse.getItems().size() - 1).getSnippet().getPublishedAt().getValue()), zoneId);
               if (validRequest) {
                   validRequest = goThroughActivities(listResponse, channelId);
                   System.out.println("Page results acquired - channel " + channelId + " -> next token >> "
                           + listResponse.getNextPageToken());
               } else {
                   break;
               }
           }catch(IndexOutOfBoundsException e){
               System.out.println(">>>> Activities empty for channel " + channelId);
               break;
           }
        }

    }

    private boolean goThroughActivities(ActivityListResponse listResponse, String channelId){

        for(int j=0; j < listResponse.getItems().size(); j++){
            if(listResponse.getItems().get(j).getSnippet().getType().equals("upload")){
                ActivityTrans activityTrans = new ActivityTrans(listResponse.getItems().get(j).getContentDetails().getUpload().getVideoId(),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(listResponse.getItems().get(j).getSnippet().getPublishedAt().getValue()), zoneId));

                if(activityRepository.existsActivityTransByVideoId(activityTrans.getVideoId())){
                        trailingCheckAlreadyExists++;   // bug that two consecutive activities were received but the queue of new videos wasn't over
                }else {
                    trailingCheckAlreadyExists = 0;
                    activityRepository.save(activityTrans);
                    System.out.println(" >>Video " + activityTrans.getVideoId());
                    try {
                        this.getVideoDetails(activityTrans.getVideoId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(trailingCheckAlreadyExists > 2 ){    //threshold not really important as it will loop doing nothing and go out
                    System.out.println("Channel activity (" + channelId + ") item " + activityTrans.getVideoId() + " already acquired");
                    return false;
                }

            }
        }

        listActivitiesRequest.setPageToken(listResponse.getNextPageToken());
        if(listActivitiesRequest.getPageToken() == null){
            return false;
        }else{
            return true;
        }
    }

}
