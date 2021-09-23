package com.example.newsreader.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.newsreader.models.NewsModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewsDataService {
    private Context context;

    public NewsDataService(Context context) {
        this.context = context;
    }

    // CALLBACKS
    public interface GetStoryIdsResponseListener {
        void onError(String message);
        void onResponse(List<NewsModel> newsModelList);
    }

    public interface GetStoriesResponseListener {
        void onError(String message);
        void onResponse(NewsModel newsModel);
    }

    public interface GetStoryListResponseListener {
        void onError(String message);
        void onResponse(List<NewsModel> newsModelList);
    }

    // get a list of the IDs
    public void getStoryIDs(String url, GetStoryIdsResponseListener listener) {
        List<NewsModel> returnList = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < 20; i++) {
                NewsModel model = new NewsModel();
                try {
                    String id = String.valueOf(response.get(i));
                    model.setId(id);
                    returnList.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listener.onResponse(returnList);
        }, error -> {
            listener.onError(error.toString());
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    // get a story in Json format
    public void getStory(String id, GetStoriesResponseListener listener) {
        String url = "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            NewsModel model = new NewsModel();
            try {
                model.setType(response.getString("type"));
                model.setUrl(response.getString("url"));
                model.setTitle(response.getString("title"));
                model.setBy(response.getString("by"));
                model.setId(response.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onResponse(model);
        }, error -> {
            listener.onError(error.toString());
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    // get a list of Stories in Json format
    public void getStoryList(String url, GetStoryListResponseListener listener) {
        getStoryIDs(url, new GetStoryIdsResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onResponse(List<NewsModel> newsModelList) {
                List<NewsModel> returnList = new ArrayList<>();
                for (int i = 0; i < newsModelList.size(); i++) {
                    getStory(newsModelList.get(i).getId(), new GetStoriesResponseListener() {
                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }

                        @Override
                        public void onResponse(NewsModel newsModel) {
                            returnList.add(newsModel);
                            if (returnList.size() == 20) {
                                listener.onResponse(returnList);
                            }
                        }
                    });
                }
            }
        });
    }
}
