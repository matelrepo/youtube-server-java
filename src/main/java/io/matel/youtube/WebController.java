package io.matel.youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
public class WebController {

    @Autowired
    AppController appController;


    @GetMapping("hello")
    public String sayHello(){
        try {
            appController.getVideoById("nABiSKQuMvQ");
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return "Hey!";
    }
}
