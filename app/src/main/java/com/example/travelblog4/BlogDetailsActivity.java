package com.example.travelblog4;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.travelblog4.http.Blog;
import com.example.travelblog4.http.BlogHttpClient;

import java.util.List;

public class BlogDetailsActivity extends AppCompatActivity {

    private BlogPreferences preferences;
    private TextView textTitle;
    private TextView textDate;
    private TextView textAuthor;
    private TextView textRating;
    private TextView textDescription;
    private TextView textViews;
    private RatingBar ratingBar;
    private ImageView imageAvatar;
    private ImageView imageMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        preferences = new BlogPreferences(this);

        imageMain = findViewById(R.id.imageMain);
        imageAvatar = findViewById(R.id.imageAvatar);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> logOutAndfinish());

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textAuthor = findViewById(R.id.textAuthor);
        textRating = findViewById(R.id.textRating);
        textViews = findViewById(R.id.textViews);
        textDescription = findViewById(R.id.textDescription);
        ratingBar = findViewById(R.id.ratingBar);

        // start data loading
        loadData();
    }

    private void logOutAndfinish() {
        preferences.setLoggedIn(false);
        finish();
    }

    private void loadData() {
        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogHttpClient.BlogArticlesCallback() { // 1, 2
            @Override
            public void onSuccess(List<Blog> blogList) { // 3
                runOnUiThread(() -> showData(blogList.get(0))); // 4
            }

            @Override
            public void onError() {
                // handle error
            }
        });
    }
    private void showData(Blog blog) {
        textTitle.setText(blog.getTitle());
        textDate.setText(blog.getDate());
        textAuthor.setText(blog.getAuthor().getName());
        textRating.setText(String.valueOf(blog.getRating()));
        textViews.setText(String.format("(%d views)", blog.getViews()));
        textDescription.setText(blog.getDescription());
        ratingBar.setRating(blog.getRating());

        Glide.with(this)
                .load(blog.getImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageMain);

        Glide.with(this)
                .load(blog.getAuthor().getAvatar())
                .transform(new CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageAvatar);
    }
}