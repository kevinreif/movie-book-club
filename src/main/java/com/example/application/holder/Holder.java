package com.example.application.holder;

import com.example.application.data.QueuedMovie;

public class Holder {
    private static QueuedMovie movie;

    public static QueuedMovie getMovie() {
        return movie;
    }

    public static void setMovie(QueuedMovie movie) {
        Holder.movie = movie;
    }
}
