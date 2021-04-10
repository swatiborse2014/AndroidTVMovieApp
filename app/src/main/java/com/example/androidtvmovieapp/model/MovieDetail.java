package com.example.androidtvmovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetail implements Parcelable {
    private String id;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    private boolean adult;
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("genre_ids")
    @Expose
    private List<String> genreIds;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    private String title;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    private float popularity;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    private boolean video;

    @SerializedName("vote_average")
    @Expose
    private float voteAverage;

    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public MovieDetail() {
    }

    public String getId() {
        return id;
    }

    public MovieDetail setId(String id) {
        this.id = id;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieDetail setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public boolean isAdult() {
        return adult;
    }

    public MovieDetail setAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public MovieDetail setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public MovieDetail setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public MovieDetail setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public MovieDetail setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public MovieDetail setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieDetail setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public MovieDetail setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public float getPopularity() {
        return popularity;
    }

    public MovieDetail setPopularity(float popularity) {
        this.popularity = popularity;
        return this;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public MovieDetail setVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public boolean isVideo() {
        return video;
    }

    public MovieDetail setVideo(boolean video) {
        this.video = video;
        return this;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public MovieDetail setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.posterPath);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeStringList(this.genreIds);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backdropPath);
        dest.writeFloat(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.voteAverage);
    }

    protected MovieDetail(Parcel in) {
        this.id = in.readString();
        this.posterPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.genreIds = in.createStringArrayList();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readFloat();
        this.voteCount = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readFloat();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
