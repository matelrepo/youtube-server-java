package io.matel.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.opencsv.CSVReader;
import io.matel.youtube.domain.VideoDetailsMaster;
import io.matel.youtube.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
public class DbInit implements CommandLineRunner {

    public  final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public  final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Autowired
    private AppController appController;

    @Autowired
    private MailService mailService;

    @Override
    public void run(String... args) throws Exception {
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("youtube").build();
        appController.setYouTube(youtube);
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 1000) //24 hours = 86400000 ms
    private void updateSubscriptions(){
//        System.out.println("Looking for new videos...");
//        try {
////           VideoDetailsMaster video = appController.getVideoDetails("Q9MtlmmN4Q0");
////           System.out.println(video.toString());
//            loadCsvChannelsList().forEach(result ->{
//                try {
//                    appController.getActivityByChannelId(result.get(0));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
////            appController.getActivityByChannelId("UCFesKMMwxlGXWKxMQmndPYQ");
//
//
//        } catch (GoogleJsonResponseException e) {
//            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
//                    + e.getDetails().getMessage());
//        } catch (IOException e) {
//            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
//        }

        mailService.sendEmail();
        System.out.println("The system is up to date");
    }

    private List<List<String>> loadCsvChannelsList() throws IOException{
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/channels_new.csv"));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }
//        return records.subList(100,100);
        return records;
    }
}
