package com.example.application.data;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class CurrentMovie extends Movie {
    private LocalDate meetingDate;
    private boolean currentMovie;

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public boolean isCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(boolean currentMovie) {
        this.currentMovie = currentMovie;
    }
}
