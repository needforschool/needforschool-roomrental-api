package fr.antoinek.roomrental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDataJpaMongodbApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootDataJpaMongodbApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJpaMongodbApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application started");
    }
}

