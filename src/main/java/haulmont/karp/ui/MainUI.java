package haulmont.karp.ui;

import com.vaadin.spring.annotation.UIScope;
import haulmont.karp.ui.view.CustomersView;
import haulmont.karp.ui.view.MechanicsView;
import haulmont.karp.ui.view.OrdersView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
@Title("AutoService-KARP021")
@SpringUI
@UIScope
@SpringViewDisplay
 public class MainUI extends UI implements ViewDisplay {

    private Panel springViewDisplay;

    @Override
    protected void init(VaadinRequest request) {
        initLayout();
    }

    private void initLayout() {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        navigationBar.addComponent(createNavigationButton("Клиенты автосервиса",
                CustomersView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Механики автосервиса",
                MechanicsView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Таблица заказов",
                OrdersView.VIEW_NAME));

        root.addComponent(navigationBar);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);

    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void showView(View view) {

        springViewDisplay.setContent((Component) view);
    }
}
