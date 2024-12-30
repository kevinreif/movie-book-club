package com.example.application.views.home;

import com.example.application.data.*;
import com.example.application.keys.ApiKeys;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.checkerframework.checker.units.qual.Current;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@PageTitle("Home")
@Route("")
@Menu(order = 0, icon = "line-awesome/svg/home-solid.svg")
@RolesAllowed({"ADMIN", "USER"})
public class HomeView extends VerticalLayout {

    //String urlTitle = "Parasite";
    //String apiKey = ApiKeys.OMDB_KEY;
    //Movie movie = new QueuedMovie();
    //Gson gson = new Gson();
    //ListBox<String> movieDetails = new ListBox<>();
    CurrentMovie movie = new CurrentMovie();

    Details details = new Details();

    //HttpClient client = HttpClient.newHttpClient();

    //HttpRequest getRequest = HttpRequest.newBuilder()
    //        .GET()
    //        .header("accept", "application/json")
    //        .uri(URI.create("https://www.omdbapi.com/?t=" + urlTitle +"&apikey=" + apiKey))
    //        .build();


    public HomeView(CurrentMovieRepository currentMovieRepository) throws IOException, InterruptedException {
        movie = currentMovieRepository.getCurrent();
        //HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //System.out.println(response.body());

        //Map<String, String> map = gson.fromJson(response.body(), new TypeToken<>(){}.getType());

        //movie.setTitle(map.get("Title"));
        //movie.setYear(map.get("Year"));
        //movie.setRuntime(map.get("Runtime"));
        //movie.setRating(map.get("Rated"));
        //movie.setDirector(map.get("Director"));
        //movie.setActors(map.get("Actors"));
        //movie.setGenre(map.get("Genre"));
        //movie.setPlot(map.get("Plot"));
        //movie.setPosterURL(map.get("Poster"));



        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.addClassName("layout2");
        add(layoutColumn2);
        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.addClassName("layoutrow");
        layoutRow.setWidthFull();
        layoutColumn2.add(layoutRow);

        VerticalLayout posterLayout = new VerticalLayout();
        posterLayout.addClassName("posterLayout");
        VerticalLayout detailsLayout = new VerticalLayout();
        detailsLayout.addClassName("detailsLayout");


        posterLayout.setWidthFull();
        detailsLayout.setWidthFull();

        

        layoutRow.add(posterLayout, detailsLayout);

        Image poster = new Image(movie.getPosterURL(), "");
        setDetails();
        
        posterLayout.add(poster);
        posterLayout.setAlignSelf(Alignment.END, poster);


        detailsLayout.add(details);
        detailsLayout.setAlignSelf(Alignment.START, details);


    }

    private void setDetails() {
        Span title = new Span("Title: " + movie.getTitle());
        Span year = new Span("Year: " + movie.getYear());
        Span runtime = new Span("Runtime: " + movie.getRuntime());
        Span rated = new Span("Rating: " + movie.getRating());
        Span genre = new Span("Genre: " + movie.getGenre());
        Span director = new Span("Director: " + movie.getDirector());
        Span actors = new Span("Actors: " + movie.getActors());
        Span plot = new Span("Plot: " + movie.getPlot());

        UnorderedList movieDetails = new UnorderedList(new ListItem("Title: " + movie.getTitle()),
                new ListItem("Year: " + movie.getYear()), new ListItem("Runtime: " + movie.getRuntime()),
                new ListItem("Rating: " + movie.getRating()), new ListItem("Genre: " + movie.getGenre()),
                new ListItem("Director: " + movie.getDirector()), new ListItem("Actors: " + movie.getActors()),
                new ListItem("Plot: " + movie.getPlot()));


        VerticalLayout content = new VerticalLayout(title, year, runtime, rated,
                genre, director, actors, plot);
        content.setSpacing(false);
        content.setPadding(false);

        details = new Details("Movie Details", movieDetails);
        details.setOpened(true);
        details.setMaxWidth(300, Unit.PIXELS);
        details.setHeightFull();

    }



}
