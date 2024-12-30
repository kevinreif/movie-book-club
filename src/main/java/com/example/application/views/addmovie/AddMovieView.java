package com.example.application.views.addmovie;

import com.example.application.data.*;
import com.example.application.keys.ApiKeys;
import com.example.application.security.AuthenticatedUser;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PageTitle("Add Movie")
@Route("add-movie")
@Menu(order = 1, icon = "line-awesome/svg/user.svg")
@RolesAllowed({"ADMIN", "USER"})
public class AddMovieView extends Composite<VerticalLayout> {

    private AuthenticatedUser authenticatedUser;

    ListBox<String> movieDetails = new ListBox<>();

    String apiKey = ApiKeys.OMDB_KEY;
    QueuedMovie qMovie = new QueuedMovie();
    ReservedMovie rMovie = new ReservedMovie();
    Gson gson = new Gson();
    TextField movieSearch = new TextField("Find Movie");
    TextField yearSearch = new TextField("Year");
    HttpClient client = HttpClient.newHttpClient();
    Notification success = new Notification();
    Notification failed = new Notification();
    Details details = new Details();

    private final QueuedMovieRepository queuedMovieRepository;
    private final ReservedMovieRepository reservedMovieRepository;

    ComboBox picker = new ComboBox("Chosen By");
    ComboBox queueRes = new ComboBox("Queue/Reserve");
    VerticalLayout mainLayout = new VerticalLayout();
    HorizontalLayout searchLayout = new HorizontalLayout();
    HorizontalLayout horzLayout = new HorizontalLayout();
    VerticalLayout posterLayout = new VerticalLayout();
    VerticalLayout detailsLayout = new VerticalLayout();
    VerticalLayout saveLayout = new VerticalLayout();
    private Image poster = new Image();
    private Grid<QueuedMovie> results = new Grid<>();
    private List<QueuedMovie> searchResults = new ArrayList<>();
    private Notification notification = new Notification();


    public AddMovieView(QueuedMovieRepository queuedMovieRepository,
                        ReservedMovieRepository reservedMovieRepository,
                        AuthenticatedUser authenticatedUser) throws IOException, InterruptedException {

        this.reservedMovieRepository = reservedMovieRepository;
        this.queuedMovieRepository = queuedMovieRepository;
        this.authenticatedUser = authenticatedUser;

        configurePicker();
        queueRes.setItems("Queued List", "Reserved List");

        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();

        Button search = new Button("Search");
        search.addClickListener(e -> {
            try {
                //SearchMovie();
                SearchAll();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });



        buttonPrimary.setText("Save");
        buttonPrimary.addClickListener(e -> {
            try {
                SaveMovie();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");


        getContent().add(mainLayout);
        mainLayout.add(searchLayout, horzLayout);
        searchLayout.add(movieSearch, yearSearch, search);
        horzLayout.add(posterLayout, detailsLayout, saveLayout);
        horzLayout.setWidthFull();
        posterLayout.setWidthFull();
        detailsLayout.setWidthFull();

        searchLayout.setWidthFull();
        searchLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        searchLayout.setAlignItems(Alignment.BASELINE);

        posterLayout.add(poster);
        posterLayout.setAlignSelf(Alignment.END, poster);

        //setDetails();
        detailsLayout.add(createResultsGrid());
        detailsLayout.setMaxWidth(300, Unit.PIXELS);

        saveLayout.setWidthFull();
        saveLayout.add(picker, queueRes, buttonPrimary);






    }

    private void setDetails() {
        UnorderedList movieDetails = new UnorderedList(new ListItem("Title: " + qMovie.getTitle()),
                new ListItem("Year: " + qMovie.getYear()), new ListItem("Runtime: " + qMovie.getRuntime()),
                new ListItem("Rating: " + qMovie.getRating()), new ListItem("Genre: " + qMovie.getGenre()),
                new ListItem("Director: " + qMovie.getDirector()), new ListItem("Actors: " + qMovie.getActors()),
                new ListItem("Plot: " + qMovie.getPlot()));


        //VerticalLayout content = new VerticalLayout(title, year, runtime, rated,
        //        genre, director, actors, plot);
        //content.setSpacing(false);
        //content.setPadding(false);

        details = new Details("Movie Details", movieDetails);
        details.setOpened(true);
        details.setMaxWidth(300, Unit.PIXELS);
        details.setHeightFull();

    }

    private void SearchMovie() throws IOException, InterruptedException {
        String urlTitle = results.asSingleSelect().getValue().getImdbID();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create("https://www.omdbapi.com/?i=" + urlTitle + "&apikey=" + apiKey))
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());


        Map<String, String> map = gson.fromJson(response.body(), new TypeToken<>() {
        }.getType());

        qMovie.setTitle(map.get("Title"));
        qMovie.setYear(map.get("Year"));
        qMovie.setRuntime(map.get("Runtime"));
        qMovie.setRating(map.get("Rated"));
        qMovie.setDirector(map.get("Director"));
        qMovie.setActors(map.get("Actors"));
        qMovie.setGenre(map.get("Genre"));
        qMovie.setPlot(map.get("Plot"));
        qMovie.setPosterURL(map.get("Poster"));
    }

    private void SaveMovie() throws IOException, InterruptedException {

        SearchMovie();

        System.out.println(queueRes.getValue().toString());
        if (qMovie.getTitle() == null) {
            System.out.println("choose a movie");
        } else if (picker.getValue().toString() == null) {
            System.out.println("Choose a picker");
        } else if (queueRes.getValue().toString() == null) {
            System.out.println("Choose queued or reserved list");
        } else {
            switch (queueRes.getValue().toString()) {
                case "Queued List":
                    SaveQueuedMovie();
                    UI.getCurrent().navigate("add-movie");
                    break;
                case "Reserved List":
                    SaveReservedMovie();
                    UI.getCurrent().navigate("add-movie");
                    break;
            }
        }
    }

    private void SaveQueuedMovie() {
        if (queuedMovieRepository.countByPicker(picker.getValue().toString()) >= 5) {
            System.out.println("Queued movie list full for " + picker.getValue().toString() + ". Please save to reserved list");
        } else {
            qMovie.setPicker(picker.getValue().toString());
            queuedMovieRepository.save(qMovie);
        }
    }

    private void SaveReservedMovie() {
        rMovie.setPicker(picker.getValue().toString());
        rMovie.setTitle(qMovie.getTitle());
        rMovie.setYear(qMovie.getYear());
        rMovie.setRuntime(qMovie.getRuntime());
        rMovie.setRating(qMovie.getRating());
        rMovie.setDirector(qMovie.getDirector());
        rMovie.setActors(qMovie.getActors());
        rMovie.setGenre(qMovie.getGenre());
        rMovie.setPlot(qMovie.getPlot());
        rMovie.setPosterURL(qMovie.getPosterURL());
        reservedMovieRepository.save(rMovie);
    }

    private void SearchAll() throws IOException, InterruptedException {
        HttpRequest getRequest;
        if (!searchResults.isEmpty()) {
            searchResults.clear();
        }

        poster.setSrc("");
        String urlTitle = movieSearch.getValue().trim().replace(" ", "-");
        String year = yearSearch.getValue().trim();

        if (!yearSearch.isEmpty()) {
            getRequest = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create("https://www.omdbapi.com/?s=" + urlTitle + "&type=movie" + "&y=" + year +
                            "&page=1" + "&apikey=" + apiKey))
                    .build();
        } else {
            getRequest = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create("https://www.omdbapi.com/?s=" + urlTitle + "&type=movie" + "&apikey=" + apiKey))
                    .build();
        }

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        JsonElement element = gson.fromJson(response.body(), JsonElement.class);

        if (element.isJsonObject()) {

            //if (!element.isJsonArray()) {

                Map<String, String> notArray = gson.fromJson(response.body(), new TypeToken<>() {
                }.getType());

                if (notArray.containsKey("Error")) {

                    if (notArray.get("Error").equals("Too many results.")) {
                        notification.show("Too many results. Please narrow search");
                        return;
                    }

                    if (notArray.get("Error").equals("Movie not found!")) {
                        notification.show("No movies found. Try a better search dickhead.");
                        return;
                    }
                }


            JsonObject obj = element.getAsJsonObject();
            JsonArray search = obj.getAsJsonArray("Search");
            Type stringType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(search, stringType);

            for (JsonObject result : list) {
                System.out.println(result);

                Map<String, String> movie = gson.fromJson(result, new TypeToken<>() {}.getType());

                QueuedMovie qmovie = new QueuedMovie();
                qmovie.setTitle(movie.get("Title"));
                qmovie.setPosterURL(movie.get("Poster"));
                qmovie.setImdbID(movie.get("imdbID"));
                qmovie.setYear(movie.get("Year"));

                searchResults.add(qmovie);
            }

            //clearGrid();
            results.setItems(searchResults);

        }
    }

    private Component createResultsGrid() {
        results = new Grid<>(QueuedMovie.class, false);
        results.addColumn("title").setAutoWidth(true);
        results.addColumn("year").setAutoWidth(true);

        results.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        results.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        results.addThemeVariants(GridVariant.LUMO_COMPACT);
        results.addSelectionListener(e -> setPoster());



        return results;

    }

    private void setPoster() {
        if (results.asSingleSelect().isEmpty()) {
            return;
        } else {
            poster.setSrc(results.asSingleSelect().getValue().getPosterURL());
        }
    }

    private void configurePicker() {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            if (user.getRoles().contains(Role.ADMIN)) {
                picker.setItems("Ben", "Dylan", "Eli", "Kevin", "Phill", "Robbie");
            } else {
                picker.setItems(user.getName());
                picker.setValue(user.getName());
                picker.setReadOnly(true);
            }
        }
    }



}
