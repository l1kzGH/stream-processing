package likz;

import java.io.Serializable;

public class Review implements Serializable{

    private int id;
    private int chocolate_id;
    private int maker_id;
    private double rating;
    private String review_text;
    private String review_date;
    private String purchase_place;
    private User user;

    static class User implements Serializable {
        private String name;
        private int age;
        private String gender;
        private String city;
    }

    public int getId() {
        return id;
    }

    public int getChocolateId() {
        return chocolate_id;
    }

    public int getMakerId() {
        return maker_id;
    }

    public double getRating() {
        return rating;
    }

    public String getReviewText() {
        return review_text;
    }

    public String getReviewDate() {
        return review_date;
    }

    public String getPurchasePlace() {
        return purchase_place;
    }

    public User getUser() {
        return user;
    }
}
