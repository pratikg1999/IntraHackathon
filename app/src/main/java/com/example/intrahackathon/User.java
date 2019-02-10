package com.example.intrahackathon;

public class User {

    String followedUsers[];
    String[] followedBy;
    String[] posts;
    String name;
    String userId;
    int reputation =1 ;

    public User(String[] followedUsers,String[] followedBy, String[] pictures, String name, String userId) {
        this.followedUsers = followedUsers;
        this.followedBy = followedBy;
        this.posts = pictures;
        this.name = name;
        this.userId = userId;
    }

    public User() {
    }
}
