package haulmont.karp.ui.form;

import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;
import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.models.Order;
import haulmont.karp.backend.models.OrderStatus;
import haulmont.karp.backend.service.CustomerService;
import haulmont.karp.backend.service.MechanicService;
import haulmont.karp.backend.service.OrderService;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import haulmont.karp.ui.converter.TextFieldToIntegerConverter;
import haulmont.karp.ui.converter.TextFieldToStatusConverter;
import haulmont.karp.ui.converter.TextFieldToCustomerInstanceConverter;
import haulmont.karp.ui.converter.TextFieldToMechanicInstanceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
@UIScope
public class OrderForm extends FormLayout {

    @Autowired
    private OrderService orderService;

    @Autowired
    private Grid<Order> orderGrid;

    @Autowired
    private Grid<Mechanic> mechanicGrid;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Grid<Customer> customerGrid;

    @Autowired
    private ComboBox<String> filterCustomerLastName;

    @Autowired
    private TextFieldToMechanicInstanceConverter textFieldToMechanicInstanceConverter;

    @Autowired
    private TextFieldToCustomerInstanceConverter textFieldToCustomerInstanceConverter;

    @Autowired
    private TextFieldToStatusConverter textFieldToStatusConverter;

    @Autowired
    private TextFieldToIntegerConverter textFieldToIntegerConverter;

    private Button helpInfoBtn = new Button();
    private TextField specification = new TextField("Описание заказа");
    private DateField dateCreation = new DateField("Дата оформления заказа");
    private DateField expirationDate = new DateField("Дата окончания работ");
    private ComboBox<String> customersComboBox = new ComboBox<>("Клиент");
    private ComboBox<String> mechanicsComboBox = new ComboBox<>("Механик");
    private NativeSelect<OrderStatus> status = new NativeSelect<>("Статус заказа");
    private TextField price = new TextField("Стоимость");
    private final Button save = new Button("Сохранить");
    private final Button add = new Button("Добавить");
    private final Button delete = new Button("Удалить");
    private final Button cancel = new Button("Отменить");
    private final HorizontalLayout buttons = new HorizontalLayout();
    private Binder<Order> orderBinder = new Binder<>(Order.class);
    private Order order;
    private Order orderDuplicate;


    @PostConstruct
    private void init() {
        addComponents();
        formatBinder();
        addClickBtnListener();
    }

    private void addComponents() {
        setSizeUndefined();
        createNotification();
        buttons.addComponents(save, delete, add, cancel);
        addComponents(helpInfoBtn, specification, dateCreation, expirationDate, customersComboBox, mechanicsComboBox, price, status, buttons);

        status.setItems(OrderStatus.values());
        status.setEmptySelectionAllowed(false);
        mechanicsComboBox.setEmptySelectionAllowed(false);
        customersComboBox.setEmptySelectionAllowed(false);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        add.setStyleName(ValoTheme.BUTTON_PRIMARY);
        add.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    private void addClickBtnListener() {
        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
        cancel.addClickListener(e -> this.cancel());
        add.addClickListener(e -> this.add());
    }

    private void formatBinder() {

        orderBinder.forField(price)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода", 1, 9))
                .withValidator(
                        name -> name.matches("[0-9]+"), "Недопустимое значение для ввода")
                .withConverter (textFieldToIntegerConverter)
                .asRequired()
                .bind(Order::getPrice, Order::setPrice);

        orderBinder.forField(specification)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода", 1, 250))
                .asRequired()
                .bind(Order::getSpecification, Order::setSpecification);

        orderBinder.forField(expirationDate)
                .withValidator(new DateRangeValidator("Некоректная дата сдачи работ", LocalDate.now(), null))
                .asRequired()
                .bind(Order::getExpirationDate, Order::setExpirationDate);

        orderBinder.forField(customersComboBox)
                .withConverter(textFieldToCustomerInstanceConverter)
                .asRequired()
                .bind(Order::getCustomer, Order::setCustomer);

         orderBinder.forField(mechanicsComboBox)
                .withConverter(textFieldToMechanicInstanceConverter)
                .asRequired()
                .bind(Order::getMechanic, Order::setMechanic);

        orderBinder.forField(status)
                .withConverter(textFieldToStatusConverter)
                .asRequired()
                .bind(Order::getStatus, Order::setStatus);


        orderBinder.bindInstanceFields(this);
    }

    public void setOrder(Order order) {
        this.order = order;
        orderBinder.setBean(order);
        if(order.getId()!=0) initOrderDuplicate(order);
        setVisibleFieldAndButton(order);
        initComboBoxMechanics();
        initComboBoxCustomers();
    }

    private void setVisibleFieldAndButton(Order order) {
        add.setVisible(order.getId() == 0);
        delete.setVisible(orderService.existOrder(order.getId()));
        save.setVisible(orderService.existOrder(order.getId()));
        customersComboBox.setReadOnly(order.getId() != 0);
        dateCreation.setReadOnly(order.getId() != 0);
        dateCreation.setVisible(order.getId() != 0);
        status.setVisible(order.getId() != 0);
        setVisible(true);
        specification.selectAll();
    }

    private void delete()  {
            mechanicDecrementAmountOrders(order);
            mechanicGrid.setItems(mechanicService.getAllMechanic());
            orderService.deleteOrder(order);
            initCustomerFilterBox();
            orderGrid.setItems(orderService.getAllOrder());
            setVisible(false);
            Notification.show("Заказ удален").setDelayMsec(2500);
    }

    private void save() {
        if (!orderBinder.isValid()) Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
        else {
            if (orderDuplicate.equalsBeforeSaving(order)) Notification.show("Вы не внесли никаких изменений").setDelayMsec(2500);
             else {
                if (order.getMechanic().getLastName().equals(orderDuplicate.getMechanic().getLastName())) {
                    orderService.updateOrder(order);
                    orderGrid.setItems(orderService.getAllOrder());
                    setVisible(false);
                    Notification.show("Заказ изменен").setDelayMsec(2500);
                }
                else {
                    mechanicDecrementAmountOrders(orderDuplicate);
                    mechanicIncrementAmountOrders(order);
                    mechanicGrid.setItems(mechanicService.getAllMechanic());

                    orderService.updateOrder(order);
                    orderGrid.setItems(orderService.getAllOrder());

                    setVisible(false);
                    Notification.show("Заказ изменен. \n У механика " + orderDuplicate.getMechanic().getLastName() +
                            " и " + order.getMechanic().getLastName() + " изменена статистика по заказам").setDelayMsec(2500);
                }
            }
        }
    }

    private void mechanicDecrementAmountOrders(Order order) {
        Mechanic mechanic = mechanicService.getMechanicOne(order.getMechanic().getId());
        mechanic.setAmountOrders(mechanic.getAmountOrders() - 1);
        mechanicService.updateMechanic(mechanic);
    }

    private void mechanicIncrementAmountOrders(Order order) {
        Mechanic mechanic = mechanicService.getMechanicOne(order.getMechanic().getId());
        mechanic.setAmountOrders(mechanic.getAmountOrders() + 1);
        mechanicService.updateMechanic(mechanic);
    }

    private void cancel() {
        setVisible(false);
    }

    private void add() {
        if (orderBinder.isValid()) {
            order.setStatus(OrderStatus.SCHEDULED.getStatus());
            order.setDateCreation(LocalDate.now());
            mechanicIncrementAmountOrders(order);
            orderService.saveOrder(order);
            mechanicGrid.setItems(mechanicService.getAllMechanic());
            customerGrid.setItems(customerService.getAllCustomer());
            initCustomerFilterBox();
            orderGrid.setItems(orderService.getAllOrder());
            setVisible(false);
            Notification.show("Заказ создан с статусом: Запланирован").setDelayMsec(2500);
        } else Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
    }

    private void initOrderDuplicate (Order order) {
        orderDuplicate = new Order();
        orderDuplicate.setStatus(order.getStatus());
        orderDuplicate.setMechanic(order.getMechanic());
        orderDuplicate.setExpirationDate(order.getExpirationDate());
        orderDuplicate.setPrice(order.getPrice());
        orderDuplicate.setSpecification(order.getSpecification());
    }

    private void createNotification(){
        helpInfoBtn.setIcon(VaadinIcons.COMMENT_ELLIPSIS);
        helpInfoBtn.addClickListener(event -> Notification.show(("* Поля обязательные для заполнения. \n" +
                "Для ввода данных о заказе нажмите ДОБАВИТЬ/СОХРАНИТЬ или Enter. \n" +
                "Для удаления заказа нажмите УДАЛИТЬ. \n" +
                "Для отмены нажмите ОТМЕНА или ESC.")).setDelayMsec(2500));
    }

    void initComboBoxMechanics(){
        List<Mechanic> mechanics = mechanicService.getAllMechanic();
        List<String> mechanicLastName = new ArrayList<>();
        for (Mechanic mechanic: mechanics) {
            mechanicLastName.add(mechanic.getLastName());
        }
        mechanicsComboBox.setItems(mechanicLastName);
    }

    public void initComboBoxCustomers() {
         List<Customer> customers = customerService.getAllCustomer();
         List<String> customersLastName = new ArrayList<>();
         for (Customer customer: customers) {
            customersLastName.add(customer.getLastName());
         }
         customersComboBox.setItems(customersLastName);
     }

    private void initCustomerFilterBox() {
        List<String> customerLastName = new ArrayList<>();
        for (Order order : orderService.getAllOrder()) {
            customerLastName.add(order.getCustomer().getLastName());
        }
        filterCustomerLastName.setItems(customerLastName);
    }
}

