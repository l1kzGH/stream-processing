package likz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

public class TopicListener {

    @Autowired
    private ReviewService reviewService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(UserReview userReview) throws InterruptedException {
        System.out.println("------------------- Message arrive | " + userReview);
        reviewService.saveReview(userReview);
    }

}
