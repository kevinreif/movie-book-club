package com.example.application.views.myview;

import com.example.application.data.QueuedMovie;
import com.example.application.data.QueuedMovieRepository;
import com.example.application.services.QueuedMovieService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.shared.ui.Transport;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//@Push(transport = Transport.LONG_POLLING)
@PageTitle("My View")
@Route("my-view")
@Menu(order = 2, icon = "line-awesome/svg/pencil-ruler-solid.svg")
@AnonymousAllowed
public class MyViewView extends Composite<VerticalLayout> {

    private final List<Div> movieDivs = new ArrayList<>();
    //private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();
    private final QueuedMovieRepository queuedMovieRepository;
    private QueuedMovie movie = new QueuedMovie();
    private Div lastHighlighted;
    private Div finalSelection;
    private Div selectedDiv;
    private int clickCount = 0;
    private VerticalLayout gridLayout = new VerticalLayout();
    private static final int MAX_CLICKS = 15; // Total number of steps before final selection
    private int stepCount = 0;
    private static final int MAX_STEPS = 15; // Number of steps before the final selection
    List<String> movieTitles = new ArrayList<>();

    public MyViewView(QueuedMovieRepository queuedMovieRepository) {
        this.queuedMovieRepository = queuedMovieRepository;

        //gridLayout.setWidthFull();
        getContent().add(gridLayout);

        Div grid = new Div();
        grid.addClassName("grid");
        gridLayout.add(grid);

        // List of movies
        movieTitles = queuedMovieRepository.getAllTitles();

        // Populate grid
        for (String movie : movieTitles) {
            Div movieDiv = new Div();
            movieDiv.setText(movie);
            String url = queuedMovieRepository.getPosterByTitle(movie);
            movieDiv.getElement().getStyle().set("background-image", "url(\"" + url + "\")");
            movieDiv.getElement().getStyle().set("background-position", "center");
            movieDiv.getElement().getStyle().set("backgroun-size", "cover");
            movieDiv.getElement().getStyle().set("background-repeat", "no-repeat");

            movieDiv.addClassName("grid-item");
            movieDivs.add(movieDiv);
            grid.add(movieDiv);
        }

        // Button to start the selection
        Button startButton = new Button("Start Selection");
        startButton.addClickListener( event -> startRandomSelection());
        //startButton.addClickListener(event -> incrementSelection());
        gridLayout.add(startButton);
    }

    private void startRandomSelection() {
        if (selectedDiv != null) {
            selectedDiv.removeClassName("final");
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new RandomHighlighter(), 0, 175, TimeUnit.MILLISECONDS);

        // Stop selection after 5 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getUI().ifPresent(ui -> ui.access(() -> {
                    stopRandomSelection(scheduler, queuedMovieRepository);
                }));
            }
        }, 5000);
    }

    private void stopRandomSelection(ScheduledExecutorService scheduler,
                                     QueuedMovieRepository repository) {
        scheduler.shutdown();
        //selectedDiv = movieDivs.get(random.nextInt(movieDivs.size()));
        selectedDiv = movieDivs.get(5);

        int row = movieDivs.indexOf(selectedDiv) / 6;
        int col = movieDivs.indexOf(selectedDiv) % 6;




        // Clear active states
        for (Div div : movieDivs) {
            div.removeClassName("active");
        }

        // Highlight final selection
        movie = repository.getByTitle(selectedDiv.getText());
        Image poster = new Image(movie.getPosterURL(), "");

        Element grid = selectedDiv.getParent().orElseThrow().getElement();
        selectedDiv.getElement().executeJs(
                "const grid = this.parentElement;" +
                        "const gridRect = grid.getBoundingClientRect();" +
                        "const gridCenterX = gridRect.left + gridRect.width / 2;" +
                        "const gridCenterY = gridRect.top + gridRect.height / 2;" +
                        "const itemRect = this.getBoundingClientRect();" +
                        "const itemCenterX = itemRect.left + itemRect.width / 2;" +
                        "const itemCenterY = itemRect.top + itemRect.height / 2;" +
                        //"const xOffset = gridCenterX - itemCenterX;" +
                        //"const yOffset = gridCenterY - itemCenterY;" +
                        "const xOffset = (gridCenterX - itemCenterX) / 2;" +
                        "const yOffset = (gridCenterY - itemCenterY) / 2;" +
                        "this.style.setProperty('--x-offset', `${xOffset}px`);" +
                        "this.style.setProperty('--y-offset', `${yOffset}px`);"
        );


        System.out.println(selectedDiv.getText());
        selectedDiv.addClassName("final");
    }

    private class RandomHighlighter implements Runnable {
        private Div lastHighlighted;

        @Override
        public void run() {
            getUI().ifPresent(ui -> ui.access(() -> {
                // Remove the previous highlight
                if (lastHighlighted != null) {
                    lastHighlighted.removeClassName("active");
                }

                // Highlight a random movie
                lastHighlighted = movieDivs.get(random.nextInt(movieDivs.size()));
                lastHighlighted.addClassName("active");
            }));
        }
    }

}
