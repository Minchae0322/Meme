package minchae.meme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeApplication.class, args);
    }

}
