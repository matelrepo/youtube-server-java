package io.matel.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class AppController {

    private final int NUM_ITERATIONS_PAGES_HISTO = 3;

    @Value("${api.key1}")
    private String apiKey;

    private YouTube youTube;
    private YouTube.Activities.List listActivitiesRequest;
    private ActivityListResponse listResponse;
    private int trailingCheckAlreadyExists =0;

    public void setYouTube(YouTube youTube){
        this.youTube = youTube;
    }

    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @Autowired
    ActivityRepository activityRepository;

    public void getVideoDetails(String videoId) throws IOException {
        if(videoDetailsRepository.existsVideoDetailsMasterByVideoId(videoId)){
            System.out.println("Video details " + videoId + " already acquired");
        }else {
            YouTube.Videos.List listVideosRequest = youTube.videos().list("statistics, contentDetails, snippet, recordingDetails");
            listVideosRequest.setId(videoId); // add list of video IDs here
            listVideosRequest.setKey(this.apiKey);

            listVideosRequest.setFields("items(contentDetails(duration), " +
                    "id, snippet(channelId, channelTitle, description, title, publishedAt, tags, thumbnails), statistics)");


            VideoListResponse listResponse = listVideosRequest.execute();

            Video video = listResponse.getItems().get(0);

//            System.out.println(video.toPrettyString());

            String thumbDetails = youTube.getJsonFactory().toString( video.getSnippet().getThumbnails());
//            ThumbnailDetails thumb =  youTube.getJsonFactory().fromString(thumbString,ThumbnailDetails.class );
//            System.out.println(thumb.toString());

            StatisticsMaster statistics = new StatisticsMaster(video.getStatistics().getCommentCount(),
                    video.getStatistics().getDislikeCount(), video.getStatistics().getFavoriteCount(),
                    video.getStatistics().getLikeCount(), video.getStatistics().getViewCount());

            VideoDetailsMaster videoDetailsMaster = new VideoDetailsMaster(video.getId(), video.getSnippet().getChannelId(),
                    video.getSnippet().getChannelTitle(), video.getContentDetails().getDuration(),
                    video.getSnippet().getDescription(), video.getSnippet().getTitle(),
                    video.getSnippet().getTags(),
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(video.getSnippet().getPublishedAt().getValue()), ZoneId.of("Asia/Bangkok")),
                    statistics, thumbDetails);

            videoDetailsRepository.save(videoDetailsMaster);
            System.out.println(videoDetailsMaster.toString());
        }
    }

    public void getActivityByChannelId(String channelId) throws IOException {
        listActivitiesRequest = youTube.activities().list("contentDetails, snippet, id");
        listActivitiesRequest.setChannelId(channelId); // add list of video IDs here
        listActivitiesRequest.setKey(this.apiKey);

        listActivitiesRequest.setFields("nextPageToken, items(contentDetails(upload(videoId))," +
                        "snippet(type,publishedAt,title,description,thumbnails(medium(url))))");


        boolean validRequest = true;
        boolean skipFirstActivityPageForTesting = false;

        for (int i = 0; i < NUM_ITERATIONS_PAGES_HISTO; i++) {
            listResponse = listActivitiesRequest.execute();
            if (validRequest && !skipFirstActivityPageForTesting) {
                validRequest = goThroughActivities(listResponse);
                System.out.println("First page results acquired - next token >> " + listResponse.getNextPageToken());
            }
            if(skipFirstActivityPageForTesting)
                listActivitiesRequest.setPageToken(listResponse.getNextPageToken());

            skipFirstActivityPageForTesting = false;
        }

    }

    private boolean goThroughActivities(ActivityListResponse listResponse){

        for(int j=0; j < listResponse.getItems().size(); j++){
            if(listResponse.getItems().get(j).getSnippet().getType().equals("upload")){
                ActivityTrans activityTrans = new ActivityTrans(listResponse.getItems().get(j).getContentDetails().getUpload().getVideoId(),
                        ZonedDateTime.ofInstant(Instant.ofEpochMilli(listResponse.getItems().get(j).getSnippet().getPublishedAt().getValue()), ZoneId.of("Asia/Bangkok")));



                if(activityRepository.existsActivityTransByVideoId(activityTrans.getVideoId())){
                        trailingCheckAlreadyExists++;       // bug that two consecutive activities were received but the queue of new videos wasn't over
                }else {
                    trailingCheckAlreadyExists = 0;
                    System.out.println(activityTrans.toString());
                    activityRepository.save(activityTrans);
                    try {
                        this.getVideoDetails(activityTrans.getVideoId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(trailingCheckAlreadyExists>2){
                    System.out.println("Activity item " + activityTrans.getVideoId() + " already acquired");
                    System.out.println("Terminating the query");
                    return false;
                }

            }
        }

        listActivitiesRequest.setPageToken(listResponse.getNextPageToken());
        return true;
    }

}
