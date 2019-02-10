package com.example.intrahackathon;

import java.util.Date;

public class Comment {
    String commentId;
    String message;
    Date time;

    public Comment() {
    }

    public Comment(String commentId, String message, Date time) {
        this.commentId = commentId;
        this.message = message;
        this.time = time;
    }
}
