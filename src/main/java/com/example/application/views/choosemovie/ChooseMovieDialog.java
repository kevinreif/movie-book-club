package com.example.application.views.choosemovie;

import com.example.application.data.*;
import com.example.application.holder.Holder;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import jakarta.xml.bind.ValidationException;

public class ChooseMovieDialog extends Composite<VerticalLayout> {


    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    private VerticalLayout layout = new VerticalLayout();
    private DatePicker meetingDate = new DatePicker("Choose meeting date");
    private final QueuedMovieRepository queuedMovieRepository;


    public ChooseMovieDialog(QueuedMovie movie,
                             QueuedMovieRepository queuedMovieRepository) {
        this.queuedMovieRepository = queuedMovieRepository;

        getContent().add(layout);
        layout.add(meetingDate, saveButton, cancelButton);

        saveButton.addClickListener(e -> Save());
        cancelButton.addClickListener(e -> fireEvent(new ChooseMovieDialog.CloseEvent(this)));

        //System.out.println(qmovie.getTitle());
        //System.out.println(movie.getTitle());
    }



    private void Save() {
        QueuedMovie qMovie = Holder.getMovie();

        CurrentMovie cMovie = ConvertToCurrent(qMovie);
        cMovie.setMeetingDate(meetingDate.getValue());

        fireEvent(new SaveEvent(this, cMovie));

        queuedMovieRepository.deleteById(qMovie.getId());


    }

    private CurrentMovie ConvertToCurrent(QueuedMovie movie) {
        CurrentMovie newMovie = new CurrentMovie();
        newMovie.setTitle(movie.getTitle());
        newMovie.setPicker(movie.getPicker());
        newMovie.setYear(movie.getYear());
        newMovie.setRuntime(movie.getRuntime());
        newMovie.setRating(movie.getRating());
        newMovie.setDirector(movie.getDirector());
        newMovie.setActors(movie.getActors());
        newMovie.setGenre(movie.getGenre());
        newMovie.setPlot(movie.getPlot());
        newMovie.setPosterURL(movie.getPosterURL());
        newMovie.setCurrentMovie(true);
        return newMovie;
    }



    public static abstract class ChooseMovieDialogEvent extends ComponentEvent<ChooseMovieDialog> {
        private CurrentMovie currentMovie;

        protected ChooseMovieDialogEvent(ChooseMovieDialog source, CurrentMovie currentMovie) {
            super(source, false);
            this.currentMovie = currentMovie;
        }

        public CurrentMovie getCurrentMovie() {
            return currentMovie;
        }
    }

    public static class SaveEvent extends ChooseMovieDialog.ChooseMovieDialogEvent {
        SaveEvent(ChooseMovieDialog source, CurrentMovie currentMovie) {
            super(source, currentMovie);
        }
    }

    public static class CloseEvent extends ChooseMovieDialog.ChooseMovieDialogEvent {
        CloseEvent(ChooseMovieDialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
