package haulmont.karp.ui.view;

import com.vaadin.spring.annotation.UIScope;
import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.service.CustomerService;
import haulmont.karp.ui.form.CustomerForm;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView(name = CustomersView.VIEW_NAME)
public class CustomersView extends VerticalLayout implements View  {

    public static final String VIEW_NAME = "";
    private VerticalLayout layout = new VerticalLayout();
    private TextField filterLastName = new TextField();
    private CssLayout filteringLastName = new CssLayout();
    private Button clearFilterTextBtn = new Button();
    private HorizontalLayout main = new HorizontalLayout();
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addCustomerBtn = new Button();
    private Button changeFieldBtn = new Button();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerForm customerForm;

    @Autowired
    private Grid<Customer> customerGrid;

    @PostConstruct
    void init() {
        gridInit();
        creationLastNameFilter();
        selectField();
        creationAddCustomerBtn();
        createNotification();
        initLayout();
    }

    private void gridInit() { customerGrid.setItems(customerService.getAllCustomer()); }

    private void creationLastNameFilter() {

        filterLastName.setPlaceholder("поиск по фамилии...");
        filterLastName.addValueChangeListener(e -> updateListCustomersForFilter());
        filterLastName.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterTextBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Очистить текущий фильр");
        clearFilterTextBtn.addClickListener(e -> filterLastName.clear());

        filteringLastName.addComponents(filterLastName, clearFilterTextBtn);
        filteringLastName.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    }

    private void updateListCustomersForFilter() {
        List<Customer> customersList = customerService.findByLastName(filterLastName.getValue());
        customerGrid.setItems(customersList);
    }


    private void initLayout(){
        toolbar.addComponents(filteringLastName, addCustomerBtn, changeFieldBtn);
        main.addComponents(customerGrid, customerForm);
        main.setSizeFull();
        customerGrid.setSizeFull();
        main.setExpandRatio(customerGrid, 1);

        customerForm.setVisible(false);
        layout.addComponents(toolbar, main);
        addComponent(layout);
    }

    private void createNotification(){
        changeFieldBtn.setIcon(VaadinIcons.FILE_REFRESH);
        changeFieldBtn.setCaption("Изменить данные");
        changeFieldBtn.addClickListener(
                event -> Notification.show("Для изменения данных или удаления из списка кликните по полю клиента").setDelayMsec(2500));
    }

    private void selectField() {
        customerGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                customerForm.setVisible(false);
            }
            else {
                clearFilterTextBtn.click();
                customerForm.setCustomer(event.getValue());
            }
        });
    }

    private void creationAddCustomerBtn() {
            addCustomerBtn.setIcon(VaadinIcons.FILE_ADD);
            addCustomerBtn.setCaption("Добавить нового клиента");
            addCustomerBtn.addClickListener(e ->{
                clearFilterTextBtn.click();
                customerGrid.asSingleSelect().clear();
                customerForm.setCustomer(new Customer());
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
