package com.example.intrahackathon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvFeed;
    ArrayList<Post> posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvFeed = findViewById(R.id.rvFeed);
        posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(layoutManager);
        RvAdapter rvAdapter = new RvAdapter(this, posts);
        rvFeed.setAdapter(rvAdapter);


/*
        downloadContent taskHandler = new downloadContent();
        try {
            Post[] allPosts = taskHandler.execute("google url").get();
            for (Post post : allPosts) {

                String imageUrl = post.imageUrl;
                Bitmap image =  new DownloadImage().execute(imageUrl).get();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View postCard = inflater.inflate(R.layout.card_post, null);
                addContentView(postCard, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.profile:
                break;
            case R.id.settings:
                break;
        }
        return true;
    }

    class downloadContent extends AsyncTask<String, Void, Post[]> {
        @Override
        protected Post[] doInBackground(String... strings) {


            return new Post[0];
        }

    }
    class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap toReturn = null;
            try {
                toReturn = BitmapFactory.decodeStream(new URL(strings[0]).openConnection().getInputStream());
                return  toReturn;
            } catch (IOException e) {
                e.printStackTrace();
                return toReturn;
            }
        }

            @Override
            protected void onPostExecute (Bitmap bitmap){
                super.onPostExecute(bitmap);
            }

    }
}
class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvFeedViewHolder>{
    Context ctx;
    ArrayList<Post> posts;

    public RvAdapter(Context ctx, ArrayList<Post> posts) {
        this.ctx = ctx;
        this.posts = posts;
    }

    @NonNull
    @Override
    public RvFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_post, viewGroup, false);
       RvFeedViewHolder holder = new RvFeedViewHolder(v);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvFeedViewHolder v, int i) {
        v.postPic.setImageResource(R.drawable.def_image);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class RvFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView postPic;

        public RvFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postPic = itemView.findViewById(R.id.postPic);
        }


    }
}
