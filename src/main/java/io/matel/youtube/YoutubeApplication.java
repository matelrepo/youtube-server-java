package io.matel.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:credentials.properties")
})
public class YoutubeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeApplication.class, args);
    }

}
