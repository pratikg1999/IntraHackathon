package com.example.intrahackathon;

import java.util.ArrayList;

public class Post {
    String postId;
    String userId;
    int upvotes;
    int downvotes;
    String imageUrl;
    ArrayList<String> comments;

    public Post() {
    }

    public Post(String postId, String userId, int upvotes, int downvotes, String imageUrl, ArrayList<String> comments) {
        this.postId = postId;
        this.userId = userId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.imageUrl = imageUrl;
        this.comments = comments;
    }
}


