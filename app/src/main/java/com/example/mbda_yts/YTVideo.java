package com.example.mbda_yts;

public class YTVideo {

    public String video_id;
    public String video_title;
    public String video_image_url;

    public YTVideo(String videoId, String videoTitle, String imageUrl) {
        video_id = videoId;
        video_title = videoTitle;
        video_image_url = imageUrl;
    }

    @Override
    public String toString() {
        return video_title;
    }

    @Override
    public int hashCode(){
        return this.video_title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YTVideo) {
            YTVideo ytv = (YTVideo) obj;
            return ytv.video_title.equals(this.video_title);
        } else {
            return false;
        }
    }

}
