package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.newsreader.adapters.ViewAdapter;
import com.example.newsreader.models.NewsModel;
import com.example.newsreader.services.NewsDataService;
import com.example.newsreader.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ViewAdapter.OnRowClickListener {
    ViewAdapter adapter;
    RecyclerView recyclerView;
    List<NewsModel> newsList;
    Button topStories, newStories, bestStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        recyclerView = findViewById(R.id.newsRecyclerView);
        topStories = findViewById(R.id.btn1);
        newStories = findViewById(R.id.btn2);
        bestStories = findViewById(R.id.btn3);

        getDataFromAPI(Constants.TOP_STORIES);
        topStories.setEnabled(false);

        topStories.setOnClickListener(v -> {
            resetButtons();
            topStories.setEnabled(false);
            getDataFromAPI(Constants.TOP_STORIES);
        });

        newStories.setOnClickListener(v -> {
            resetButtons();
            newStories.setEnabled(false);
            getDataFromAPI(Constants.NEW_STORIES);
        });

        bestStories.setOnClickListener(v -> {
            resetButtons();
            bestStories.setEnabled(false);
            getDataFromAPI(Constants.BEST_STORIES);
        });
    }

    private void resetButtons() {
        topStories.setEnabled(true);
        newStories.setEnabled(true);
        bestStories.setEnabled(true);
    }

    private void getDataFromAPI(String url) {
        NewsDataService service = new NewsDataService(this);
        service.getStoryList(url, new NewsDataService.GetStoryListResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<NewsModel> newsModelList) {
                if (newsList != null) {
                    newsList.clear();
                }
                newsList = new ArrayList<>();
                newsList.addAll(newsModelList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new ViewAdapter(newsModelList, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
        intent.putExtra("URL", newsList.get(position).getUrl());
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        Toast.makeText(MainActivity.this, newsList.get(position).toString(), Toast.LENGTH_LONG).show();
    }
}