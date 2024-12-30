package com.example.application.views.addperson;

import com.example.application.data.ReservedMovie;
import com.example.application.data.Role;
import com.example.application.data.User;
import com.example.application.data.UserRepository;
import com.example.application.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

@PageTitle("Add Person")
@Route("add-person")
@Menu(order = 5, icon = "line-awesome/svg/pencil-ruler-solid.svg")
@RolesAllowed("ADMIN")
public class AddPersonView extends Composite<VerticalLayout> {

    private TextField firstName = new TextField();
    private TextField textField2 = new TextField();
    private DatePicker datePicker = new DatePicker();
    private ComboBox<Role> role = new ComboBox<>();
    private EmailField emailField = new EmailField();
    private TextField textField4 = new TextField();
    private HorizontalLayout layoutRow = new HorizontalLayout();
    private Button save = new Button();
    private Button buttonSecondary = new Button();
    private ConfirmDialog saveDialog = new ConfirmDialog();
    private HorizontalLayout gridLayout = new HorizontalLayout();
    private Grid<User> grid;

    public AddPersonView(PasswordEncoder passwordEncoder, UserRepository userRepository,
                         UserService userService) {
        //refreshGrid(userRepository);

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Personal Information");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        firstName.setLabel("First Name");
        firstName.isRequired();
        //textField2.setLabel("Last Name");
        //datePicker.setLabel("Birthday");
        role.setLabel("Role");
        role.setItems(EnumSet.allOf(Role.class));
        emailField.setLabel("Email");
        emailField.isRequired();
        //textField4.setLabel("Occupation");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        save.setText("Save");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> SavePerson(passwordEncoder, userRepository));
        //buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2, gridLayout);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(firstName);
        //formLayout2Col.add(textField2);
        //formLayout2Col.add(datePicker);
        formLayout2Col.add(role);
        formLayout2Col.add(emailField);
        //formLayout2Col.add(textField4);
        layoutColumn2.add(layoutRow);
        layoutRow.add(save);
        //layoutRow.add(buttonSecondary);
        //gridLayout.add(createUserGrid(userService));
        getContent().add(createUserGrid(userService));


    }

    //Saves new User
    private void SavePerson(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        User user = new User();
        user.setUsername(emailField.getValue());
        user.setName(firstName.getValue());
        user.setHashedPassword(passwordEncoder.encode("password"));
        user.setRoles(Collections.singleton(role.getValue()));

        userRepository.save(user);
    }

    //Creates and returns User grid
    private Component createUserGrid(UserService userService) {
        grid = new Grid<>(User.class, false);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("roles").setAutoWidth(true);

        grid.setItems(query -> userService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
        //grid.setItems(userList);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setMaxHeight(60, Unit.PERCENTAGE);

        return grid;

    }

    private void refreshGrid(UserRepository userRepository) {

    }



}