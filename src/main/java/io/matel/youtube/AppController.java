package io.matel.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import io.matel.youtube.domain.ActivityTrans;
import io.matel.youtube.domain.StatisticsMaster;
import io.matel.youtube.domain.VideoMaster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class AppController implements CommandLineRunner {

    @Value("${api.key1}")
    private String apiKey;

    private YouTube youtube;

    public  final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public  final JsonFactory JSON_FACTORY = new JacksonFactory();

    public String getVideoDetails(String videoId) throws IOException {
            YouTube.Videos.List listVideosRequest = youtube.videos().list("statistics, contentDetails, snippet, recordingDetails");
            listVideosRequest.setId(videoId); // add list of video IDs here
            listVideosRequest.setKey(this.apiKey);

            listVideosRequest.setFields("items(contentDetails(duration), "+
            "id, snippet(channelId, channelTitle, description, title, publishedAt, tags, thumbnails), statistics)");


            VideoListResponse listResponse = listVideosRequest.execute();

            Video video = listResponse.getItems().get(0);

            StatisticsMaster statistics = new StatisticsMaster(video.getStatistics().getCommentCount(),
                    video.getStatistics().getDislikeCount(), video.getStatistics().getFavoriteCount(),
                    video.getStatistics().getLikeCount(), video.getStatistics().getViewCount());

            VideoMaster videoMaster = new VideoMaster(video.getId(), video.getSnippet().getChannelId(),
                    video.getSnippet().getChannelTitle(), video.getContentDetails().getDuration(),
                    video.getSnippet().getDescription(), video.getSnippet().getTitle(),
                    null,
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(video.getSnippet().getPublishedAt().getValue()), ZoneId.of("Asia/Bangkok")), statistics, null);

            System.out.println(videoMaster.toString());

        return null;
    }

    public String getActivityByChannelId(String channelId) throws IOException {
            YouTube.Activities.List listActivitiesRequest = youtube.activities().list("contentDetails, snippet, id");
        listActivitiesRequest.setChannelId(channelId); // add list of video IDs here
        listActivitiesRequest.setKey(this.apiKey);

        listActivitiesRequest.setFields("items(contentDetails(upload(videoId))," +
                        "snippet(type,publishedAt,title,description,thumbnails(medium(url))))");


            ActivityListResponse listResponse = listActivitiesRequest.execute();
            listResponse.getItems().forEach(activity ->{
                    if(activity.getSnippet().getType().equals("upload")){
                        ActivityTrans activityTrans = new ActivityTrans(activity.getContentDetails().getUpload().getVideoId(),
                                ZonedDateTime.ofInstant(Instant.ofEpochMilli(activity.getSnippet().getPublishedAt().getValue()), ZoneId.of("Asia/Bangkok")));

                        System.out.println(activityTrans.toString());
                        try {
                            this.getVideoDetails(activityTrans.getVideoId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
            });
        return null;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube").build();

//            getVideoDetails("nABiSKQuMvQ");
            getActivityByChannelId("UCVHFbqXqoYvEWM1Ddxl0QDg");

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }
    }
}
