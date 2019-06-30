package haulmont.karp.ui.form;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;
import haulmont.karp.backend.models.Customer;

import haulmont.karp.backend.models.Order;
import haulmont.karp.backend.service.CustomerService;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import haulmont.karp.backend.service.OrderService;
import haulmont.karp.ui.converter.TextFieldToNumberPhoneConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;


@Component
@UIScope
public class CustomerForm extends FormLayout {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Grid<Customer> customerGrid;

    @Autowired
    private Grid<Order> orderGrid;

    @Autowired
    private OrderForm orderForm;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TextFieldToNumberPhoneConverter textFieldToNumberPhoneConverter;

    private Button helpInfoBtn = new Button();
    private TextField lastName = new TextField("Фамилия");
    private TextField firstName = new TextField("Имя");
    private TextField secondName = new TextField("Отчество");
    private TextField phoneNumber = new TextField("Контактный номер");
    private final Button save = new Button("Сохранить");
    private final Button add = new Button("Добавить");
    private final Button delete = new Button("Удалить");
    private final Button cancel = new Button("Отменить");
    private final HorizontalLayout buttons = new HorizontalLayout();
    private Binder<Customer> customerBinder = new Binder<>(Customer.class);
    private Customer customer;
    private Customer customerDuplicate;

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
        addComponents(helpInfoBtn, lastName, firstName, secondName, phoneNumber, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        add.setStyleName(ValoTheme.BUTTON_PRIMARY);
        add.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    private void addClickBtnListener() {
        save.addClickListener(e -> this.save());
        delete.addClickListener(e ->
        {
            try {
                this.delete();
            } catch (DataIntegrityViolationException e1) {
                Notification.show("Невозможно удалить клиента имеющего заказ").setDelayMsec(2500);
            }
        });
        cancel.addClickListener(e -> this.cancel());
        add.addClickListener(e -> this.add());
    }

    private void formatBinder() {

        customerBinder.forField(phoneNumber)
                .withValidator(new StringLengthValidator("Формат ввода без кода страны +7 xxxxxxxxxx", 10, 10))
                .withValidator(
                        name -> name.matches("[0-9]+"), "Формат ввода без кода страны +7 xxxxxxxxxx")
                .withConverter (textFieldToNumberPhoneConverter)
                .asRequired()
                .bind(Customer::getPhoneNumber, Customer::setPhoneNumber);

        customerBinder.forField(lastName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired()
                .bind(Customer::getLastName, Customer::setLastName);

        customerBinder.forField(firstName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired("")
                .bind(Customer::getFirstName, Customer::setFirstName);

        customerBinder.forField(secondName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired()
                .bind(Customer::getSecondName, Customer::setSecondName);

        customerBinder.bindInstanceFields(this);
    }


    public void setCustomer(Customer customer){
        this.customer = customer;
        customerBinder.setBean(customer);
        if(customer.getId()!=0) initCustomerDuplicate(customer);
        setVisibleFieldAndButton(customer);
    }

    private void setVisibleFieldAndButton(Customer customer) {
        add.setVisible(customer.getId() == 0);
        delete.setVisible(customerService.existCustomer(customer.getId()));
        save.setVisible(customerService.existCustomer(customer.getId()));
        setVisible(true);
        lastName.selectAll();
    }

    private void delete() throws DataIntegrityViolationException {
        customerService.deleteCustomer(customer);
        customerGrid.setItems(customerService.getAllCustomer());
        orderForm.initComboBoxCustomers();
        setVisible(false);
        Notification.show("Клиент " + this.customer.getLastName() +  " " + this.customer.getFirstName()+ " " + this.customer.getSecondName() +
                " удален").setDelayMsec(2500);
    }

    private void save() {
        if (!customerBinder.isValid()) {
            Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
        }
        else {
            if (customerDuplicate.equalsBeforeSaving(customer)) Notification.show("Вы не внесли никаких изменений").setDelayMsec(2500);
               else {
                customerService.updateCustomer(customer);
                customerGrid.setItems(customerService.getAllCustomer());
                orderForm.initComboBoxCustomers();
                orderGrid.setItems(orderService.getAllOrder());
                setVisible(false);
                Notification.show("Данные клиента : " + this.customer.getLastName() + " " + this.customer.getFirstName() + " " +
                        this.customer.getSecondName() + " измененны").setDelayMsec(2500);
            }
        }
    }

    private void cancel() {
        setVisible(false);
    }

    private void add() {
        if (customerBinder.isValid()) {
            customerService.saveCustomer(customer);
            customerGrid.setItems(customerService.getAllCustomer());
            orderForm.initComboBoxCustomers();
            setVisible(false);
            Notification.show("Клиент " + this.customer.getLastName() + " " + this.customer.getFirstName() + " " + this.customer.getSecondName() +
                    " добавлен в список Автосервиса").setDelayMsec(2500);
        } else {
            Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
        }
    }
        private void initCustomerDuplicate(Customer customer) {
            customerDuplicate = new Customer();
            customerDuplicate.setFirstName(customer.getFirstName());
            customerDuplicate.setLastName(customer.getLastName());
            customerDuplicate.setSecondName(customer.getSecondName());
            customerDuplicate.setPhoneNumber(customer.getPhoneNumber());
        }

    private void createNotification(){
        helpInfoBtn.setIcon(VaadinIcons.COMMENT_ELLIPSIS);
        helpInfoBtn.addClickListener(event -> Notification.show(("* Поля обязательные для заполнения. \n" +
                "Для ввода данных о клиенте нажмите ДОБАВИТЬ/СОХРАНИТЬ или Enter. \n" +
                "Для удаления клиента нажмите УДАЛИТЬ. \n" +
                "Для отмены нажмите ОТМЕНА или ESC.")).setDelayMsec(2500));
    }
}
