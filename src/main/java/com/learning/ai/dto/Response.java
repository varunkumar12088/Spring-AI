package com.learning.ai.dto;

public class Response {

    private String title;
    private String content;
    private String year;

    public Response(){}

    public Response(String title, String content, String year){
        this.title = title;
        this.content = content;
        this.year = year;
    }

    public String getContent() {
        return this.content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getYear() {
        return this.year;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "title: " + this.title + ", content: "
                + this.content + ", year: " + this.year;
    }
}
