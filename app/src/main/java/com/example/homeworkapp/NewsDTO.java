package com.example.homeworkapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * News data
 */
public class NewsDTO {

    private String title="Google（中文译名：谷歌[7][注 2]）是源自美国的跨国科技公司，为Alphabet Inc.的子公司，业务范围涵盖互联网广告、互联网搜索、云计算等领域，开发并提供大量基于互联网的产品与服务[9]，其主要利润来自于AdWords等广告服务[10][11]。";

    private String url;

    private String image;

    private String date="mm/dd/yyyy";

    private String section_id;

    private String newsid;

    boolean refresh=true;

    public NewsDTO(JSONObject news_item) {
        try {
            this.title = news_item.getString("webTitle");
            this.image=news_item.getString("thumbnail");
            if (this.image.equals("guardian-default-image")){
                this.image="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
            }
            this.date = news_item.getString("timeDiff");
            this.section_id =news_item.getString("sectionName");
            this.url=news_item.getString("webUrl");
            this.newsid=news_item.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {return date;}

    public String getImage(){return image;}

    public String getSection_id(){return section_id;}

    public String getNewsid(){return newsid;}

    public void setRefresh(){
        this.refresh=!this.refresh;
    }
}
