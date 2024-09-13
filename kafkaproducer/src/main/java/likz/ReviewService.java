package likz;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class ReviewService {

    @Value("${topic.name}")
    private String topic;
    private final KafkaTemplate<String, ChocolateReview> kafkaTemplate;
    private ChocolateReview chocolateReview;

    public ReviewService(KafkaTemplate<String, ChocolateReview> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void sendNewReview() {
        ChocolateReview newReview = createRandomReview();
        System.out.println(newReview);
        kafkaTemplate.send(topic, newReview);
    }

    private ChocolateReview createRandomReview() {
        Faker faker = new Faker();
        ChocolateReview review = new ChocolateReview(
                faker.number().numberBetween(1, 1000),
                faker.number().numberBetween(1, 100),
                faker.number().randomDouble(1, 3, 5),
                faker.lorem().sentence(),
                generateRandomLocation(faker),
                generateRandomDate(faker)
        );
        return review;
    }

    private static String generateRandomLocation(Faker faker) {
        return String.format("%s_%s_%s_%s",
                faker.address().country(),
                faker.address().city(),
                faker.address().streetName(),
                faker.address().buildingNumber());
    }

    private static String generateRandomDate(Faker faker) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date randomDate = null;
        try {
            randomDate = faker.date().between(dateFormat.parse("2023-01-01"), dateFormat.parse("2023-12-31"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dateFormat.format(randomDate);
    }
}