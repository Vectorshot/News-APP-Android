package com.example.homeworkapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DataRepository {

    private static RequestQueue requestQueue;

    private static Context context;

    /*init was static*/
    public static void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        DataRepository.context = context;
    }

    /**
     * @param mutableLiveData NewsDTO construct,then insert into the list,then the LiveData changed,the ViewModels can get the change and render again
     */
    public static void getNews_homepage(MutableLiveData<List<NewsDTO>> mutableLiveData) {
        String apikey = "fac9c85d-c32d-40e8-91b8-f04743fa9d5c";
        List<NewsDTO> list = new ArrayList<>();
//        final String URL = "http://csci571-mobile-qiaoyiyin.us-east-1.elasticbeanstalk.com/G/home";
        final String URL ="https://hw9-app-android-ruoyuchen.ue.r.appspot.com/guardian/home";
        StringRequest request = new StringRequest(URL, response -> {
            Log.d("responsestr", "getNews_homepage: " + response);
            try {
                JSONObject responseJson = new JSONObject(response);
                JSONObject responseJsonJSONObject=responseJson.getJSONObject("response");
                Log.e("stringtojson", String.valueOf(responseJson));
                JSONArray response_results = responseJsonJSONObject.getJSONArray("results");
                int i;
                for (i = 0; i < Math.min(10, response_results.length()); i++) {
                    JSONObject news_content = (JSONObject) response_results.get(i);
//                    String title = news_content.getString("title");
                    NewsDTO newsDTO = new NewsDTO(news_content);
                    list.add(newsDTO);
                }
                mutableLiveData.setValue(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getNews_headlines(MutableLiveData<List<NewsDTO>> mutableLiveData, String section_news) {
        List<NewsDTO> list = new ArrayList<>();
//        final String URL = "http://csci571-mobile-qiaoyiyin.us-east-1.elasticbeanstalk.com/G/" + section_news;
        final String URL="https://hw9-app-android-ruoyuchen.ue.r.appspot.com/guardian/headlines/"+section_news;
        StringRequest request = new StringRequest(URL, response -> {
            Log.d("responsestr", "getNews_" + section_news + ": " + response);
            try {
                JSONObject response_json = new JSONObject(response);
                JSONObject response_obj=response_json.getJSONObject("response");
                JSONArray response_results = response_obj.getJSONArray("results");
                int i;
                for (i = 0; i < Math.min(10, response_results.length()); i++) {
                    JSONObject news_content = (JSONObject) response_results.get(i);
                    NewsDTO newsDTO = new NewsDTO(news_content);
                    list.add(newsDTO);
                }
                mutableLiveData.setValue(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getNews_searching(MutableLiveData<List<NewsDTO>> mutableLiveData, String q_words) {
        List<NewsDTO> list = new ArrayList<>();
//        final String URL = "http://csci571-mobile-qiaoyiyin.us-east-1.elasticbeanstalk.com/G/articles/search?q=" + q_words;
        final String URL="https://hw9-app-android-ruoyuchen.ue.r.appspot.com/guardian/article/search?q="+q_words;
        StringRequest request = new StringRequest(URL, response -> {
            try {
                JSONObject response_json = new JSONObject(response);
                JSONObject response_obj=response_json.getJSONObject("response");
                JSONArray response_results = response_obj.getJSONArray("results");
                int i;
                for (i = 0; i < Math.min(10, response_results.length()); i++) {
                    JSONObject news_content = (JSONObject) response_results.get(i);
                    NewsDTO newsDTO = new NewsDTO(news_content);
                    list.add(newsDTO);
                }
                mutableLiveData.setValue(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getNews_detail(MutableLiveData<List<String>> mutableLiveData, String id) {
        List<String> list = new ArrayList<>();
//        final String URL = "http://csci571-mobile-qiaoyiyin.us-east-1.elasticbeanstalk.com/G/details/article?id=" + id;
        final String URL="https://hw9-app-android-ruoyuchen.ue.r.appspot.com/guardian/detail/article?id="+id;
        StringRequest request = new StringRequest(URL, response -> {
            try {
                JSONObject response_json = new JSONObject(response);
                JSONObject response_detail_article=response_json.getJSONObject("response");
                JSONObject jsonObject = response_detail_article.getJSONObject("content");
                String title = jsonObject.getString("webTitle");
                list.add(title);
                String image = jsonObject.getString("imageURL");
                if (image.equals("guardian-default-image")) {
                    image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                }
                list.add(image);
                String source = jsonObject.getString("sectionName");
                list.add(source);
                String time = jsonObject.getString("article_publication");
                list.add(time);
                String content = jsonObject.getString("news_description");
                list.add(content);
                String url_news = jsonObject.getString("webUrl");
                list.add(url_news);
                String thumbnail = jsonObject.getString("imageURL");
                list.add(thumbnail);
                mutableLiveData.setValue(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getLocation(MutableLiveData<List<String>> mutableLiveData, String location_name) {
        List<String> list = new ArrayList<>();
        String link_source =
                "https://api.openweathermap.org/data/2.5/weather?q=" + location_name + "&units=metric&appid=e5a8fea6b4b4cf44fdb076f65dc08e70";
        StringRequest request = new StringRequest(link_source, response -> {
            try {
                JSONObject response_json = new JSONObject(response);
                JSONArray weather = response_json.getJSONArray("weather");
                String w = weather.getJSONObject(0).getString("main");
//                String.valueOf(Math.round(Double.parseDouble(weather.getJSONObject(0).getString("main"))));
                list.add(w);
                String t = String.valueOf(Math.round(Double.parseDouble(response_json.getJSONObject("main").getString("temp"))));
                list.add(t);
                String background = "https://csci571.com/hw/hw9/images/android/sunny_weather.jpg";
                switch (w) {
                    case "Clouds":
                        background = "https://csci571.com/hw/hw9/images/android/cloudy_weather.jpg";
                        break;
                    case "Clear":
                        background = "https://csci571.com/hw/hw9/images/android/clear_weather.jpg";
                        break;
                    case "Snow":
                        background = "https://csci571.com/hw/hw9/images/android/snowy_weather.jpeg";
                        break;
                    case "Rain":
                        background = "https://csci571.com/hw/hw9/images/android/rainy_weather.jpg";
                        break;
                    case "Drizzle":
                        background = "https://csci571.com/hw/hw9/images/android/rainy_weather.jpg";
                        break;
                    case "ThunderStorm":
                        background = "https://csci571.com/hw/hw9/images/android/thunder_weather.jpg";
                        break;
                }
                list.add(background);
                mutableLiveData.setValue(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getNews_favor(MutableLiveData<List<String>> mutableLiveData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Bookmarks", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<String> bookmark_list = new ArrayList<>(sharedPreferences.getAll().keySet());
        mutableLiveData.setValue(bookmark_list);
    }

    public static void getTrending(MutableLiveData<LineData> mutableLiveData, String query) {
//        final String URL = "http://csci571-mobile-qiaoyiyin.us-east-1.elasticbeanstalk.com/api/google-trends?q=" + query;
        final String URL="https://hw9-app-android-ruoyuchen.ue.r.appspot.com/google/trending?id="+query;
        List<Integer> color = new ArrayList<>();
        List<Entry> list = new ArrayList<>();
        StringRequest request = new StringRequest(URL, response -> {
            try {
                JSONObject response_json = new JSONObject(response);
                JSONArray response_results = response_json.getJSONArray("values");
                int i;
                for (i = 0; i < Math.max(10, response_results.length()); i++) {
                    JSONObject data_axis = (JSONObject) response_results.get(i);
                    Entry data = new Entry(data_axis.getInt("id"), data_axis.getInt("value"));
                    list.add(data);
                    color.add(Color.parseColor("#6200EE"));
//                    NewsDTO newsDTO = new NewsDTO(news_content);
//                    list.add(newsDTO);
                }
                String chart_title="Trending Chart for "+query;
                LineDataSet datachart = new LineDataSet(list, chart_title);
                datachart.setColors(Color.parseColor("#6200EE"));
                datachart.setFillAlpha(65);
//                datachart.setFillColor(R.color.colorPrimary);
                datachart.setCircleColor(Color.parseColor("#6200EE"));
                datachart.setCircleHoleColor(Color.parseColor("#6200EE"));

                List<ILineDataSet> dataresult = new ArrayList<>();
                dataresult.add(datachart);
                LineData data_charting = new LineData(dataresult);
                mutableLiveData.setValue(data_charting);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error"));
        requestQueue.add(request);
    }

    public static void getsuggestions(ArrayAdapter<String> arrayAdapter, String query) {
        final String url = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?mkt=fr-FR&q=" + query;
        StringRequest request = new StringRequest(url, response -> {
            try {
                arrayAdapter.clear();
                JSONObject jsonObject_response = new JSONObject(response);
                JSONArray suggestiongroup = jsonObject_response.getJSONArray("suggestionGroups");
                JSONArray query_suggestion = suggestiongroup.getJSONObject(0).getJSONArray("searchSuggestions");
                for (int i = 0; i < query_suggestion.length(); i++) {
                    String suggestion = query_suggestion.getJSONObject(i).getString("query");
                    arrayAdapter.add(suggestion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->
                Log.d("response", "onErrorResponse: Error")) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", "b96c22506118490a8b6ed375f0899d79");

                return params;
            }
        };

        requestQueue.add(request);
    }
}
