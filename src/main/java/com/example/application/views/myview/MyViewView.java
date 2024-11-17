package com.example.application.views.myview;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("My View")
@Route("my-view")
@Menu(order = 2, icon = "line-awesome/svg/pencil-ruler-solid.svg")
@AnonymousAllowed
public class MyViewView extends Composite<VerticalLayout> {

    public MyViewView() {
        FormLayout formLayout2Col = new FormLayout();
        FormLayout formLayout2Col2 = new FormLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        formLayout2Col.setWidth("100%");
        formLayout2Col2.setWidth("100%");
        getContent().add(formLayout2Col);
        getContent().add(formLayout2Col2);
    }
}
