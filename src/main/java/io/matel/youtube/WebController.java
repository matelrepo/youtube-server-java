package io.matel.youtube;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Value("${api.key}")
    private String apiKey;

    @GetMapping("hello")
    public String sayHello(){
        System.out.println(apiKey);
        return "Hello!";
    }
}
