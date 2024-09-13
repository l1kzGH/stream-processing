package likz;

import lombok.Data;

@Data
public class UserReview {

    private int user_id;
    private int chocolate_id;
    private double rating;
    private String review_text;
    private String purchase_place;
    private String review_date;

    public int getChocolate_id() {
        return chocolate_id;
    }

    public double getRating() {
        return rating;
    }
}
