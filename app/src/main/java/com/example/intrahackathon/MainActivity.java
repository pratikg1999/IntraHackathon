package com.example.intrahackathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvFeed;
    ArrayList<Post> posts;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference currentUserRef;
    StorageReference storage;
    DatabaseReference pictureCountRef;
    int pictureCount;
    DatabaseReference postReference;
    String picUrl;


    //StorageReference picturesDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        pictureCountRef = FirebaseDatabase.getInstance().getReference("pictureCount");
        postReference = FirebaseDatabase.getInstance().getReference("post");
        currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


        rvFeed = findViewById(R.id.rvFeed);
        posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(layoutManager);
        final RvAdapter rvAdapter = new RvAdapter(this, posts);
        rvFeed.setAdapter(rvAdapter);
        pictureCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("picturecount",dataSnapshot.getValue().toString());
                Toast.makeText(MainActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                pictureCount = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    posts.add(postSnapshot.getValue(Post.class));
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

    void imagePicker(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
                uploadToFirebase(data.getData());
        }
    }

    private void uploadToFirebase(Uri data) {

        final StorageReference file = storage.child("pictures/" + pictureCount);
        file.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        picUrl = uri.toString();
                        pictureCountRef.setValue(++pictureCount);
                    }
                });
                String postId = postReference.push().getKey();
                Post post = new Post(postId, currentUser.getUid(), 0, 0, picUrl, new ArrayList<String>(Arrays.asList("dummy")));
                postReference.child(postId).setValue(post);

                currentUserRef.child("posts").child(postId).setValue(post);

            }
        });
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
            case R.id.logoutButton:
                auth.signOut();
                startActivity(new Intent(this, login.class));
                break;

            case R.id.shareButton:
                imagePicker();
        }
        return true;
    }

    class downloadContent extends AsyncTask<String, Void, Post[]> {
        @Override
        protected Post[] doInBackground(String... strings) {


            return new Post[0];
        }

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
        final Post curPost = posts.get(i);
        final String postId = curPost.postId;
        String imageUrl = curPost.imageUrl;
        try {
            Bitmap image = new DownloadImage().execute(imageUrl).get();
            v.postPic.setImageBitmap(image);
            v.upvoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference postInDatabase = FirebaseDatabase.getInstance().getReference("post");
                    postInDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            postInDatabase.child(postId).child("upvotes").setValue(curPost.upvotes +1);
                            Log.i("curpost upvotes value", curPost.upvotes+"");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class RvFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView postPic;
        ImageView upvoteButton;
        ImageView downvoteButton;
        ImageView commentButton;
        public RvFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postPic = itemView.findViewById(R.id.postPic);
            upvoteButton = itemView.findViewById(R.id.postUpvote);
            downvoteButton = itemView.findViewById(R.id.bpostDownvote);
            commentButton = itemView.findViewById(R.id.bpostComment);
        }


    }
}
