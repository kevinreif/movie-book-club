package com.example.application.views.movielist;

import com.example.application.data.QueuedMovie;
import com.example.application.data.QueuedMovieRepository;
import com.example.application.data.ReservedMovie;
import com.example.application.data.ReservedMovieRepository;
import com.example.application.services.QueuedMovieService;
import com.example.application.services.ReservedMovieService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;

@PageTitle("MovieList")
@Route("movie-list")
@Menu(order = 3, icon = "line-awesome/svg/film-solid.svg")
@RolesAllowed({"ADMIN", "USER"})
@Uses(Icon.class)
public class MovieListView extends Div {

    private Grid<QueuedMovie> qGrid;
    private Grid<ReservedMovie> rGrid;

    private Filters qFilters;
    private RFilters rFilters;
    private final QueuedMovieService queuedMovieService;
    private final ReservedMovieService reservedMovieService;
    private final QueuedMovieRepository queuedMovieRepository;
    private final ReservedMovieRepository reservedMovieRepository;
    private static HorizontalLayout actionsLayout = new HorizontalLayout();
    private HorizontalLayout convertLayout = new HorizontalLayout();
    private static Select<String> selectGrid = new Select<>();
    private VerticalLayout layout = new VerticalLayout();
    private Button sendQeueud = new Button("To Queued");
    private Button sendReserved = new Button("To Reserved");
    private Button deleteQueued = new Button("Delete");
    private Button deleteRes = new Button("Delete");
    private Notification notification = new Notification();
    private ConfirmDialog deleteDialog = new ConfirmDialog();
    private boolean isQueued = true;

    public MovieListView(QueuedMovieService queuedMovieService,
                         ReservedMovieService reservedMovieService,
                         QueuedMovieRepository queuedMovieRepository,
                         ReservedMovieRepository reservedMovieRepository) {
        this.queuedMovieService = queuedMovieService;
        this.reservedMovieService = reservedMovieService;
        this.queuedMovieRepository = queuedMovieRepository;
        this.reservedMovieRepository = reservedMovieRepository;

        setSizeFull();
        addClassNames("movie-list-view");

        selectGrid.setLabel("Select List");
        selectGrid.setItems("Queued List", "Reserved List");
        selectGrid.setValue("Queued List");

        sendQeueud.addClickListener(e -> SendToQueued());
        sendReserved.addClickListener(e -> SendToReserved());

        deleteQueued.addClickListener(e -> DeleteQueued());
        deleteRes.addClickListener(e -> DeleteReserved());


        //selectLayout.add(selectGrid);
        //selectGrid.getStyle().set("margin-left", "auto");

        System.out.println(selectGrid.getValue());

        //if (selectGrid.getValue().equals("Queued List")) {
            actionsLayout.removeAll();
            qFilters = new Filters(() -> refreshQueuedGrid());
            layout.add(qFilters, createQueuedGrid());
            convertLayout.add(sendReserved, deleteQueued);
        //} else {//   rFilters = new RFilters(() -> refreshResGrid());
        //    layout.add(rFilters, createResGrid());
        //}
        //qFilters = new Filters(() -> refreshQueuedGrid());

        //layout.add(/*createMobileFilters(),*/ actionsLayout, createQueuedGrid());
        //rFilters = new RFilters(() -> refreshResGrid());


        convertLayout.setWidthFull();
        convertLayout.setPadding(true);
        convertLayout.setSpacing(true);
        convertLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        layout.setWidthFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout, convertLayout);

        selectGrid.addValueChangeListener(e -> {
            if (selectGrid.getValue() == null) {
                isQueued = true;
            } else if (selectGrid.getValue().toString().equals("Queued List")) {
                isQueued = true;
            } else {
                isQueued = false;
            }
            chooseGrid();
        });
    }

    private void chooseGrid() {
        if (isQueued == true) {
            layout.removeAll();
            actionsLayout.removeAll();
            convertLayout.removeAll();
            qFilters = new Filters(() -> refreshQueuedGrid());
            layout.add(qFilters, createQueuedGrid());
            convertLayout.add(sendReserved, deleteQueued);
        } else {
            layout.removeAll();
            actionsLayout.removeAll();
            convertLayout.removeAll();
            rFilters = new RFilters(() -> refreshResGrid());
            layout.add(rFilters, createResGrid());
            convertLayout.add(sendQeueud, deleteRes);

        }


    }

    /*private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }*/

    //Queued Filters
    public static class Filters extends Div implements Specification<QueuedMovie> {

        private final CheckboxGroup<String> person = new CheckboxGroup<>("Chosen by: ");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            person.setItems("Ben", "Dylan", "Eli", "Kevin", "Phill", "Robby");
            person.addClassName("double-width");
            person.addValueChangeListener(e -> onSearch.run());

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                person.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            actionsLayout.add(person, resetBtn/*, searchBtn*/, selectGrid);
            actionsLayout.setWidthFull();
            actionsLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
            selectGrid.getStyle().set("margin-left", "auto");
            actionsLayout.addClassName(LumoUtility.Gap.SMALL);
            actionsLayout.addClassName("actions");

            add(/*person, */actionsLayout);
        }


        @Override
        public Predicate toPredicate(Root<QueuedMovie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!person.isEmpty()) {
                String databaseColumn = "picker";
                List<Predicate> pickerPredicates = new ArrayList<>();
                for (String picker : person.getValue()) {
                    pickerPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(picker), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(pickerPredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    //Reserved Filters
    public static class RFilters extends Div implements Specification<ReservedMovie> {

        private final CheckboxGroup<String> person = new CheckboxGroup<>("Chosen by: ");

        public RFilters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            person.setItems("Ben", "Dylan", "Eli", "Kevin", "Phill", "Robby");
            person.addClassName("double-width");
            person.addValueChangeListener(e -> onSearch.run());

            // Grid Selector
            //selectGrid.setLabel("Select Grid");
            //selectGrid.setItems("Queued List", "Reserved List");
            //selectGrid.setValue("Queued List");

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                person.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            actionsLayout.add(person, resetBtn/*, searchBtn*/, selectGrid);
            actionsLayout.setWidthFull();
            actionsLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
            selectGrid.getStyle().set("margin-left", "auto");
            actionsLayout.addClassName(LumoUtility.Gap.SMALL);
            actionsLayout.addClassName("actions");

            add(/*person, */actionsLayout);
        }


        @Override
        public Predicate toPredicate(Root<ReservedMovie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!person.isEmpty()) {
                String databaseColumn = "picker";
                List<Predicate> pickerPredicates = new ArrayList<>();
                for (String picker : person.getValue()) {
                    pickerPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(picker), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(pickerPredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }


    }

    private Component createQueuedGrid() {
        qGrid = new Grid<>(QueuedMovie.class, false);
        qGrid.addColumn("title").setAutoWidth(true);
        qGrid.addColumn("rating").setAutoWidth(true);
        qGrid.addColumn("director").setAutoWidth(true);
        qGrid.addColumn("picker").setAutoWidth(true);

        qGrid.setItems(query -> queuedMovieService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                qFilters).stream());
        qGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        qGrid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        qGrid.addThemeVariants(GridVariant.LUMO_COMPACT);

        return qGrid;

    }

    private Component createResGrid() {
        rGrid = new Grid<>(ReservedMovie.class, false);
        rGrid.addColumn("title").setAutoWidth(true);
        rGrid.addColumn("rating").setAutoWidth(true);
        rGrid.addColumn("director").setAutoWidth(true);
        rGrid.addColumn("picker").setAutoWidth(true);

        rGrid.setItems(query -> reservedMovieService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                rFilters).stream());
        rGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        rGrid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        rGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        rGrid.setMaxHeight(60, Unit.PERCENTAGE);

        return rGrid;

    }

    private void refreshQueuedGrid() {
        qGrid.getDataProvider().refreshAll();
    }

    private void refreshResGrid() {
        rGrid.getDataProvider().refreshAll();
    }

    private void SendToQueued() {
        if (rGrid.asSingleSelect().isEmpty()) {
            notification.show("Please select a movie from the list.");
            return;
        }
        ReservedMovie movie = rGrid.asSingleSelect().getValue();
        if (queuedMovieRepository.countByPicker(movie.getPicker()) >= 5) {
            notification.show("Queued movie list full for " + movie.getPicker() + ".");
        } else {
            queuedMovieRepository.save(ConvertToQueued(movie));
            reservedMovieService.delete(movie.getId());
            refreshResGrid();
        }
    }

    private QueuedMovie ConvertToQueued(ReservedMovie movie) {
        QueuedMovie newMovie = new QueuedMovie();
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
        return newMovie;
    }

    private void SendToReserved() {
        if (qGrid.asSingleSelect().isEmpty()) {
            notification.show("Please select a movie from the list.");
            return;
        }

        QueuedMovie movie = qGrid.asSingleSelect().getValue();

        reservedMovieRepository.save(ConvertToReserved(movie));
        queuedMovieService.delete(movie.getId());
        refreshQueuedGrid();
    }

    private ReservedMovie ConvertToReserved(QueuedMovie movie) {
        ReservedMovie newMovie = new ReservedMovie();
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
        return newMovie;
    }

    private void DeleteQueued() {
        if (qGrid.asSingleSelect().isEmpty()) {
            notification.show("Please select a movie from the list.");
        } else {
            deleteDialog.setHeader("Delete Movie");
            deleteDialog.setText("Are you sure you want to delete " + qGrid.asSingleSelect().getValue().getTitle() + "?");
            deleteDialog.setCancelable(true);
            deleteDialog.addCancelListener(e -> deleteDialog.close());
            deleteDialog.setConfirmText("Delete");
            deleteDialog.addConfirmListener(e -> {
                queuedMovieService.delete(qGrid.asSingleSelect().getValue().getId());
                    refreshQueuedGrid();});

            deleteDialog.open();
        }
    }

    private void DeleteReserved() {
        if (rGrid.asSingleSelect().isEmpty()) {
            notification.show("Please select a movie from the list.");
        } else {
            deleteDialog.setHeader("Delete Movie");
            deleteDialog.setText("Are you sure you want to delete " + rGrid.asSingleSelect().getValue().getTitle() + "?");
            deleteDialog.setCancelable(true);
            deleteDialog.addCancelListener(e -> deleteDialog.close());
            deleteDialog.setConfirmText("Delete");
            deleteDialog.addConfirmListener(e -> {
                reservedMovieService.delete(rGrid.asSingleSelect().getValue().getId());
                refreshResGrid();});

            deleteDialog.open();
        }
    }
}
