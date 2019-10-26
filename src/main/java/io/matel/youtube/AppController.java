package io.matel.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class AppController implements CommandLineRunner {

    @Value("${api.key1}")
    private String apiKey;

    public  final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public  final JsonFactory JSON_FACTORY = new JacksonFactory();

    public String getVideoById(String id) throws IOException {
        try {
            YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube").build();

            YouTube.Videos.List listVideosRequest = youtube.videos().list("statistics, contentDetails, snippet, recordingDetails");
            listVideosRequest.setId(id); // add list of video IDs here
            listVideosRequest.setKey(this.apiKey);

            listVideosRequest.setFields("items(contentDetails(duration), "+
            "id, recordingDetails(recordingDate), "+
            "snippet(channelId, channelTitle, description, title, publishedAt, tags, thumbnails), statistics)");


            VideoListResponse listResponse = listVideosRequest.execute();

            Video video = listResponse.getItems().get(0);

            StatisticsMaster statistics = new StatisticsMaster(video.getStatistics().getCommentCount(),
                    video.getStatistics().getDislikeCount(), video.getStatistics().getFavoriteCount(),
                    video.getStatistics().getLikeCount(), video.getStatistics().getViewCount());

            VideoMaster videoMaster = new VideoMaster(video.getId(), video.getSnippet().getChannelId(),
                    video.getSnippet().getChannelTitle(), video.getContentDetails().getDuration(),
                    video.getSnippet().getDescription(), video.getSnippet().getTitle(),
                    null, ZonedDateTime.ofInstant(Instant.ofEpochMilli(video.getRecordingDetails().getRecordingDate().getValue()), ZoneId.of("Asia/Bangkok")),
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(video.getSnippet().getPublishedAt().getValue()), ZoneId.of("Asia/Bangkok")), statistics, null);

            System.out.println(videoMaster.toString());



        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void run(String... args) throws Exception {
        getVideoById("nABiSKQuMvQ");
    }
}
