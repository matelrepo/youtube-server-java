package io.matel.youtube.config;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.opencsv.CSVReader;
import io.matel.youtube.controller.AppController;
import io.matel.youtube.domain.SubscriptionTrans;
import io.matel.youtube.repository.SubscriptionRepo;
import io.matel.youtube.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class DbInit implements CommandLineRunner {

    public  final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public  final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Autowired
    private AppController appController;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

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

    @Scheduled(fixedDelay = 86400010, initialDelay = 1000) //24 hours = 86400000 ms
    private void updateSubscriptions(){
        System.out.println("Looking for new videos...");
        List<SubscriptionTrans> subs = subscriptionRepo.findAll();
//        subs.subList(0,5).forEach(subscriptionTrans ->{
        subs.forEach(subscriptionTrans ->{

        try {
                    appController.getActivityByChannelId(subscriptionTrans.getChannelId());
//            appController.getActivityByChannelId("UCGvjUWz3mGV9cssraATuEsw");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(">>> Channel " + subscriptionTrans.getChannelId() + " up to date <<<");
                System.out.println("");
            });

        mailService.sendEmail();
        System.out.println("The system is up to date");
    }

    private List<SubscriptionTrans> loadCsvChannelsList() throws IOException{
        List<SubscriptionTrans> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/channels_new.csv"));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(new SubscriptionTrans(values[0], 1));
            }
        }
        subscriptionRepo.saveAll(records);
        return records;
    }
}

