package com.example.application.views.selectmovie;

import com.example.application.data.ReservedMovie;
import com.example.application.data.ReservedMovieRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@PageTitle("Find Movie")
@Route("find-movie")
@Menu(order = 6, icon = "line-awesome/svg/search_solid.svg")
@RolesAllowed("ADMIN")
public class FindMovieView extends Composite<VerticalLayout> {

    String apiKey = "4d09b83a";
    ReservedMovie rMovie = new ReservedMovie();
    Gson gson = new Gson();
    TextField movieSearch = new TextField("Find Movie");
    HttpClient client = HttpClient.newHttpClient();
    Button search = new Button("Search");
    Button add = new Button("Add");
    ComboBox picker = new ComboBox("Chosen By");

    private final ReservedMovieRepository reservedMovieRepository;
    VerticalLayout mainLayout = new VerticalLayout();

    public FindMovieView(ReservedMovieRepository reservedMovieRepository) {
        this.reservedMovieRepository = reservedMovieRepository;
        picker.setItems("Ben", "Dylan", "Eli", "Kevin", "Phill", "Robbie");

        getContent().add(mainLayout);
        mainLayout.add(movieSearch, search, picker, add);

        search.addClickListener(e -> {
            try {
                SearchMovie();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        add.addClickListener(e -> Add());
    }

    private void SearchMovie() throws IOException, InterruptedException {
        String urlTitle = movieSearch.getValue().trim();
        String apiKey = "4d09b83a";

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create("https://www.omdbapi.com/?i=" + urlTitle + "&apikey=" + apiKey))
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());


        Map<String, String> map = gson.fromJson(response.body(), new TypeToken<>() {
        }.getType());

        rMovie.setTitle(map.get("Title"));
        rMovie.setYear(map.get("Year"));
        rMovie.setRuntime(map.get("Runtime"));
        rMovie.setRating(map.get("Rated"));
        rMovie.setDirector(map.get("Director"));
        rMovie.setActors(map.get("Actors"));
        rMovie.setGenre(map.get("Genre"));
        rMovie.setPlot(map.get("Plot"));
        rMovie.setPosterURL(map.get("Poster"));
    }

    private void Add() {
        if (rMovie.getTitle() == null) {
            Notification.show("No movie selected");
            return;
        }

        rMovie.setPicker(picker.getValue().toString());
        reservedMovieRepository.save(rMovie);
    }
}
