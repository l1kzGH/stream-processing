package likz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class SparkConsumer {
    public static void main(String[] args) {
        SpringApplication.run(SparkConsumer.class, args);
    }
}
