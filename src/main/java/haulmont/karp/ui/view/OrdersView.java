package haulmont.karp.ui.view;

import com.vaadin.spring.annotation.UIScope;
import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Order;
import haulmont.karp.backend.service.CustomerService;
import haulmont.karp.backend.service.OrderService;
import haulmont.karp.ui.form.OrderForm;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@UIScope
@SpringView(name = OrdersView.VIEW_NAME)
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";

    private VerticalLayout layout = new VerticalLayout();
    private TextField filterSpecification = new TextField();
    private ComboBox<String> filterStatus = new ComboBox<>();
    private CssLayout filteringSpecification = new CssLayout();
    private CssLayout filteringStatus = new CssLayout();
    private CssLayout filteringCustomer = new CssLayout();
    private Button clearFilterTextSpecBtn = new Button();
    private Button clearFilterStatusBtn = new Button();
    private Button clearFilterCustomerBtn = new Button();
    private HorizontalLayout main = new HorizontalLayout();
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addOrderBtn = new Button();
    private Button changeFieldBtn = new Button();
    private List<Order> orders;


    @Autowired
    private Grid<Order> orderGrid;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderForm orderForm;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ComboBox<String> filterCustomerLastName;

    @PostConstruct
    private void init() {
        gridInit();

        creationSpecificationFilter();
        creationCustomerFilter();
        creationStatusFilter();

        selectField();
        creationAddOrderBtn();
        createNotification();
        initLayout();
    }

    private void gridInit() {
        orderGrid.setItems(orderService.getAllOrder());
    }

    private void creationSpecificationFilter() {

        filterSpecification.setPlaceholder("поиск по описанию..");
        filterSpecification.addValueChangeListener(event -> {
            filterCustomerLastName.setVisible(false);
            filterStatus.setVisible(false);
            clearFilterStatusBtn.setVisible(false);
            clearFilterCustomerBtn.setVisible(false);
            updateGridOnSpecificationField();
        });
        filterSpecification.setValueChangeMode(ValueChangeMode.EAGER);

        clearFilterTextSpecBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterTextSpecBtn.setDescription("Очистить текущий фильр");
        clearFilterTextSpecBtn.addClickListener(e -> filterSpecification.clear());

        filteringSpecification.addComponents(filterSpecification, clearFilterTextSpecBtn);
        filterSpecification.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    }
    private void creationStatusFilter() {
        filterStatus.setTextInputAllowed(false);
        filterStatus.setEmptySelectionAllowed(false);
        filterStatus.setEmptySelectionCaption("фильтр по статусу");
        filterStatus.setItems("Запланирован", "Выполнен", "Принят клиентом");
        filterStatus.addValueChangeListener(event -> {
            filterCustomerLastName.setVisible(false);
            filterSpecification.setVisible(false);
            clearFilterTextSpecBtn.setVisible(false);
            clearFilterCustomerBtn.setVisible(false);
            updateGridOnStatusField();
        });

        clearFilterStatusBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterStatusBtn.setDescription("Очистить текущий фильтр");
        clearFilterStatusBtn.addClickListener(e -> filterStatus.clear());

        filteringStatus.addComponents(filterStatus, clearFilterStatusBtn);
        filteringStatus.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    }


    private void creationCustomerFilter() {
        filterCustomerLastName.setEmptySelectionAllowed(false);
        filterCustomerLastName.setPlaceholder("по клиенту..");
        initCustomerFilterBox();

        filterCustomerLastName.addValueChangeListener(e -> {
            filterSpecification.setVisible(false);
            filterStatus.setVisible(false);
            clearFilterTextSpecBtn.setVisible(false);
            clearFilterStatusBtn.setVisible(false);
            updateGridOnCustomerField();
        });

        clearFilterCustomerBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterCustomerBtn.setDescription("Очистить текущий фильтр");
        clearFilterCustomerBtn.addClickListener(e ->
                filterCustomerLastName.clear());

        filteringCustomer.addComponents(filterCustomerLastName, clearFilterCustomerBtn);
        filteringCustomer.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    }

    private void updateGridOnStatusField() {
        if (filterStatus.getValue()== null) {
            orders = orderService.getAllOrder();
            filterCustomerLastName.setVisible(true);
            filterSpecification.setVisible(true);
            clearFilterTextSpecBtn.setVisible(true);
            clearFilterCustomerBtn.setVisible(true);
        }
        else {
            orders = orderService.findByStatus(filterStatus.getValue());
        }
        orderGrid.setItems(orders);
    }

    private void updateGridOnCustomerField() {
      if (filterCustomerLastName.getValue() == null ) {
          orders = orderService.getAllOrder();
          filterSpecification.setVisible(true);
          filterStatus.setVisible(true);
          clearFilterTextSpecBtn.setVisible(true);
          clearFilterStatusBtn.setVisible(true);
      }
      else {
          List<Customer> customers = customerService.findByLastName(filterCustomerLastName.getValue());
          for (Customer customer : customers) {
              orders = orderService.findByCustomer(customer);
          }
      }
        orderGrid.setItems(orders);
    }

    private void updateGridOnSpecificationField() {
        if (filterSpecification.getValue().equals("")) {
            orders = orderService.getAllOrder();
            filterCustomerLastName.setVisible(true);
            filterStatus.setVisible(true);
            clearFilterStatusBtn.setVisible(true);
            clearFilterCustomerBtn.setVisible(true);

        } else {
            orders = orderService.findBySpecification(filterSpecification.getValue());
        }
        orderGrid.setItems(orders);
    }

    private void initLayout(){
        toolbar.addComponents(filteringSpecification, filteringCustomer, filteringStatus, addOrderBtn, changeFieldBtn);
        main.addComponents(orderGrid, orderForm);
        main.setSizeFull();
        orderGrid.setSizeFull();
        main.setExpandRatio(orderGrid, 1);

        orderForm.setVisible(false);
        layout.addComponents(toolbar, main);
        addComponent(layout);

    }

    private void createNotification(){
        changeFieldBtn.setIcon(VaadinIcons.FILE_REFRESH);
        changeFieldBtn.setCaption("Изменить данные");
        changeFieldBtn.addClickListener(
                event -> Notification.show("Для изменения данных или удаления из списка кликните по полю заказа").setDelayMsec(2500));
    }

    private void selectField() {
        orderGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                orderForm.setVisible(false);
            }
            else {
                clearFilterTextSpecBtn.click();
                clearFilterStatusBtn.click();
                clearFilterCustomerBtn.click();
                orderForm.setOrder(event.getValue());
            }
        });
    }

    private void creationAddOrderBtn() {
        addOrderBtn.setIcon(VaadinIcons.FILE_ADD);
        addOrderBtn.setCaption("Добавить новый заказ");
        addOrderBtn.addClickListener(e ->{
            clearFilterTextSpecBtn.click();
            clearFilterStatusBtn.click();
            clearFilterCustomerBtn.click();
            orderGrid.asSingleSelect().clear();
            orderForm.setOrder(new Order());
        });
    }

    private void initCustomerFilterBox() {
        List<String> customerLastName = new ArrayList<>();
        for (Order order : orderService.getAllOrder()) {
            customerLastName.add(order.getCustomer().getLastName());
        }
        filterCustomerLastName.setItems(customerLastName);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {}
}
