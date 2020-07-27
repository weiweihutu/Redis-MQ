package com.ibiz.redis.mq.topic;

public class Topic {
    private String element;
    private double score;

    public Topic(String element, double score) {
        this.element = element;
        this.score = score;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
