package com.example.intrahackathon;

import java.util.ArrayList;

public class User {

    String userId;
    String name;
    ArrayList<String> posts;
    ArrayList<String> followedBy;
    ArrayList<String> followedUsers;
    int reputation =1 ;

    public User(String userId, String name, ArrayList<String> posts, ArrayList<String> followedBy, ArrayList<String> followedUsers, int reputation) {
        this.userId = userId;
        this.name = name;
        this.posts = posts;
        this.followedBy = followedBy;
        this.followedUsers = followedUsers;
        this.reputation = reputation;
    }

    public User() {
    }
}
