package com.example.intrahackathon;

public class Post {
    User user;
    String[] comments;
    int upvotes;
    int downvotes;
    String imageUrl;

    public Post() {
    }

    public Post(User user, String[] comments, int upvotes, int downvotes, String imageUrl) {
        this.user = user;
        this.comments = comments;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.imageUrl = imageUrl;
    }
}


