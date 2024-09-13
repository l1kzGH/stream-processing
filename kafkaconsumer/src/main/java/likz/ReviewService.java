package likz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    public void saveReview(UserReview userReview) {
        DBReview dbReview = new DBReview(userReview.getChocolate_id(), userReview.getRating());
        reviewRepo.save(dbReview);
    }

    class DBReview {
        private int chocolate_id;
        private double rating;

        public DBReview(int chocolate_id, double rating) {
            this.chocolate_id = chocolate_id;
            this.rating = rating;
        }
    }


}
