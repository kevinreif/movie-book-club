package com.example.application.views.choosemovie;

import com.example.application.data.CurrentMovie;
import com.example.application.data.CurrentMovieRepository;
import com.example.application.data.QueuedMovie;
import com.example.application.data.QueuedMovieRepository;
import com.example.application.holder.Holder;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.shared.ui.Transport;
import jakarta.annotation.security.RolesAllowed;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@PageTitle("Choose Movie")
@Route("choose-movie")
@Menu(order = 4, icon = "line-awesome/svg/pencil-ruler-solid.svg")
@RolesAllowed({"ADMIN", "USER"})
public class ChooseMovieView extends Composite<VerticalLayout> {

    private List<Long> movieList;
    private List<String> posterURLs;
    private VerticalLayout wheelLayout = new VerticalLayout();
    //private HorizontalLayout buttonLayout = new HorizontalLayout();
    private final QueuedMovieRepository queuedMovieRepository;
    private final CurrentMovieRepository currentMovieRepository;
    private Button randomize = new Button("Randomize");
    private Image poster = new Image();
    private QueuedMovie movie;
    private Random random = new Random();
    private ChooseMovieDialog chooseMovieDialog;
    private Button confirm = new Button("Confirm");
    private Dialog confirmDialog = new Dialog();
    private int randomAngle;
    private int winner;
    private Dialog winDialog = new Dialog();


    //Wheel
    private Div wheelContainer;
    private Div wheel;
    private Div pointer;
    private List<String> movieTitles = new ArrayList<>();
    private int segmentCount = movieTitles.size(); // Total number of segments

    //private final int segmentCount;



    public ChooseMovieView(QueuedMovieRepository queuedMovieRepository,
                           CurrentMovieRepository currentMovieRepository) {
        this.queuedMovieRepository = queuedMovieRepository;
        this.currentMovieRepository = currentMovieRepository;

        randomAngle = ((int) (Math.random() * 360) + 1800); // 5 full rotations + random angle
        //randomAngle = 179 + 358;

        movieTitles = queuedMovieRepository.getAllTitles();


        randomize.addClickListener(e -> {
            //RandomizeList();
            wheel.getElement().getStyle().set("transform", "rotate(0deg)");
        });


        wheelLayout.setWidthFull();
        getContent().add(wheelLayout/*, buttonLayout*/);

        confirm.addClickListener(e -> confirmDialog.open());

        //buttonLayout.add(confirm);
        configureChooseMovieDialog();

        confirmDialog.add(chooseMovieDialog);
        confirmDialog.setCloseOnOutsideClick(true);

        //Make sure movie title list has 30 items
        if (movieTitles.size() != 30) {
            return;
        } else {
            Collections.shuffle(movieTitles);
            wheelLayout.add(createWheel());
        }
    }


    private void configureChooseMovieDialog() {
        chooseMovieDialog = new ChooseMovieDialog(movie, queuedMovieRepository);
        chooseMovieDialog.addListener(ChooseMovieDialog.SaveEvent.class, this::saveCurrentMovie);
        chooseMovieDialog.addListener(ChooseMovieDialog.CloseEvent.class, this::closeCurrentMovie);
    }

    private void saveCurrentMovie(ChooseMovieDialog.SaveEvent event) {
        currentMovieRepository.deleteAll();
        currentMovieRepository.save(event.getCurrentMovie());
        confirmDialog.close();
        UI.getCurrent().navigate("");

    }

    private void closeCurrentMovie(ChooseMovieDialog.CloseEvent event) {
        confirmDialog.close();
    }

    //Wheel
    private void spinWheel(Div wheel) {
        // Generate a random angle with multiple spins


        //int randomAngle = (int) (Math.random() * 360) + 3600; // 10 full rotations + random angle
        wheel.getElement().getStyle().set("transition", "transform 5s ease-in-out");
        wheel.getElement().getStyle().set("transform", "rotate(" + (randomAngle + 358) + "deg)");

        // Calculate the winning movie
        int segmentAngle = 360 / segmentCount;
        int resultIndex = (segmentCount - ((randomAngle % 360) / segmentAngle)) % segmentCount;



        double normalizedAngle = ((randomAngle) % 360 + 360) % 360;
        double segmentSize = 360 / 30;
        int index = (int) (normalizedAngle / segmentSize);
        System.out.println(index);

        winner = resultIndex;

        if (resultIndex >= 5) {
            winner = resultIndex - 4;
        } else {
            switch (resultIndex) {
                case 4:
                    winner = 0;
                    break;
                case 3:
                    winner = 29;
                    break;
                case 2:
                    winner = 28;
                    break;
                case 1:
                    winner = 27;
                    break;
                case 0:
                    winner = 26;
            }
        }

        System.out.println(movieTitles.get(winner));

        //Add next rotation
        randomAngle += ((int) (Math.random() * 360) + 1800); // 5 full rotations + random angle

        //Wait 5 seconds before announcing the selected movie
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getUI().ifPresent(ui -> ui.access(() -> {
                    AnnounceWinner();
                }));
            }
        }, 5000);


    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Set initial wheel style
        wheel.getElement().getStyle().set("transition", "transform 4s ease-out");
    }

    private Component createWheel() {
        //Wheel
        // List of 30 movies
        segmentCount = movieTitles.size(); // 30 segments

        // Wheel Container
        wheelContainer = new Div();
        wheelContainer.setClassName("container");

        wheel = new Div();
        wheel.setClassName("wheel");
        wheel.setId("spinWheel");

        // Add segments and titles dynamically
        for (String title : movieTitles) {
            Div segment = new Div();
            Span span = new Span();
            span.setText(title);
            span.setClassName("title");
            segment.add(span);
            segment.setClassName("segment");
            wheel.add(segment);
        }

        // Create Pointer
        pointer = new Div();
        pointer.setClassName("pointer");
        pointer.setText("SPIN");

        // Spin Button
        Button spinButton = new Button("Spin", event -> spinWheel(wheel));
        pointer.addClickListener(event -> spinWheel(wheel));

        // Add components to layout
        wheelContainer.add(wheel, pointer, spinButton);

        return wheelContainer;
    }

    private void AnnounceWinner() {
        movie = queuedMovieRepository.getByTitle(movieTitles.get(winner));
        winDialog = createWinnerDialog(movie.getTitle(), movie.getPosterURL());
        winDialog.setCloseOnOutsideClick(true);

        winDialog.open();
    }

    //Creates and returns Winner's Dialog to AnnounceWinner()
    private Dialog createWinnerDialog(String title, String poster) {
        Dialog dialog = new Dialog();
        VerticalLayout movieLayout = new VerticalLayout();
        Div movieTitle = new Div(title);
        Image moviePoster = new Image(poster, title);
        movieLayout.add(movieTitle, moviePoster);

        dialog.add(movieLayout);

        return dialog;
    }
}
