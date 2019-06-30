package haulmont.karp.ui.form;


import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;
import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.models.Order;
import haulmont.karp.backend.service.MechanicService;
import com.vaadin.ui.*;
import haulmont.karp.backend.service.OrderService;
import haulmont.karp.ui.converter.TextFieldToIntegerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;



@Component
@UIScope
public class MechanicForm extends FormLayout {

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private Grid<Mechanic> mechanicGrid;

    @Autowired
    private Grid<Order> orderGrid;

    @Autowired
    private OrderForm orderForm;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TextFieldToIntegerConverter textFieldToIntegerConverter;

    private Button helpInfoBtn = new Button();
    private TextField lastName = new TextField("Фамилия");
    private TextField firstName = new TextField("Имя");
    private TextField secondName = new TextField("Отчество");
    private TextField paymentPerHour = new TextField("Ставка в час");
    private final Button save = new Button("Сохранить");
    private final Button add = new Button("Добавить");
    private final Button delete = new Button("Удалить");
    private final Button cancel = new Button("Отменить");
    private final HorizontalLayout buttons = new HorizontalLayout();
    private Binder<Mechanic> mechanicBinder = new Binder<>(Mechanic.class);
    private Mechanic mechanic;
    private Mechanic mechanicDuplicate;


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
        addComponents(helpInfoBtn, lastName, firstName, secondName, paymentPerHour, buttons);

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
                Notification.show("Невозможно удалить механика задействованного в заказе").setDelayMsec(2500);
            }
        });
        cancel.addClickListener(e -> this.cancel());
        add.addClickListener(e -> this.add());
    }

    private void formatBinder() {

        mechanicBinder.forField(paymentPerHour)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода", 1, 9))
                .withValidator(
                        name -> name.matches("[0-9]+"), "Недопустимое значение для ввода")
                .withConverter (textFieldToIntegerConverter)
                .asRequired()
                .bind(Mechanic::getPaymentPerHour, Mechanic::setPaymentPerHour);

        mechanicBinder.forField(lastName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired()
                .bind(Mechanic::getLastName, Mechanic::setLastName);

        mechanicBinder.forField(firstName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired()
                .bind(Mechanic::getFirstName, Mechanic::setFirstName);

        mechanicBinder.forField(secondName)
                .withValidator(new StringLengthValidator("Недопустимое значение для ввода",1, 25))
                .withValidator(
                        name -> name.matches("^[а-яА-Я]+$"), "Формат ввода (A-я), без пробелов, символов и цифр")
                .asRequired("")
                .bind(Mechanic::getSecondName, Mechanic::setSecondName);

        mechanicBinder.bindInstanceFields(this);
    }


    public void setMechanic(Mechanic mechanic){
        this.mechanic = mechanic;
        mechanicBinder.setBean(mechanic);
        if(mechanic.getId()!=0) initMechanicDuplicate(mechanic);
        setVisibleFieldAndButton(mechanic);
    }

    private void setVisibleFieldAndButton(Mechanic mechanic) {
        add.setVisible(mechanic.getId() == 0);
        delete.setVisible(mechanicService.existMechanic(mechanic.getId()));
        save.setVisible(mechanicService.existMechanic(mechanic.getId()));
        setVisible(true);
        lastName.selectAll();
    }

    private void delete() throws DataIntegrityViolationException {
        mechanicService.deleteMechanic(mechanic);
        mechanicGrid.setItems(mechanicService.getAllMechanic());
        orderForm.initComboBoxMechanics();
        setVisible(false);
        Notification.show("Механик " + this.mechanic.getLastName() +  " " + this.mechanic.getFirstName()+ " " + this.mechanic.getSecondName() +
                " удален из списка действущих сотрудников").setDelayMsec(2500);
    }

    private void save() {
        if (!mechanicBinder.isValid()) {
            Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
        }
        else {
            if (mechanicDuplicate.equalsBeforeSaving(mechanic)) Notification.show("Вы не внесли никаких изменений").setDelayMsec(2500);
              else {
                mechanicService.updateMechanic(mechanic);
                mechanicGrid.setItems(mechanicService.getAllMechanic());
                orderForm.initComboBoxMechanics();
                orderGrid.setItems(orderService.getAllOrder());
                setVisible(false);
                Notification.show("Данные сотрудника : " + this.mechanic.getLastName() + " " + this.mechanic.getFirstName() + " " +
                        this.mechanic.getSecondName() + " измененны").setDelayMsec(2500);
            }
        }
    }

    private void cancel() {
        setVisible(false);
    }

    private void add() {
        if(mechanicBinder.isValid()) {
            mechanicService.saveMechanic(mechanic);
            mechanicGrid.setItems(mechanicService.getAllMechanic());
            orderForm.initComboBoxMechanics();
            setVisible(false);
            Notification.show("Механик " + this.mechanic.getLastName() + " " + this.mechanic.getFirstName() + " " + this.mechanic.getSecondName() +
                    " добавлен в список действущих сотрудников").setDelayMsec(2500);
        }
        else {
            Notification.show("Форма заполненна некорректно").setDelayMsec(2500);
        }
    }

    private void initMechanicDuplicate(Mechanic mechanic) {
        mechanicDuplicate = new Mechanic();
        mechanicDuplicate.setFirstName(mechanic.getFirstName());
        mechanicDuplicate.setLastName(mechanic.getLastName());
        mechanicDuplicate.setSecondName(mechanic.getSecondName());
        mechanicDuplicate.setPaymentPerHour(mechanic.getPaymentPerHour());
    }
    private void createNotification(){
        helpInfoBtn.setIcon(VaadinIcons.COMMENT_ELLIPSIS);
        helpInfoBtn.addClickListener(event -> Notification.show(("* Поля обязательные для заполнения. \n" +
                "Для ввода данных о сотруднике нажмите ДОБАВИТЬ/СОХРАНИТЬ или Enter. \n" +
                "Для удаления сотрудника нажмите УДАЛИТЬ. \n" +
                "Для отмены нажмите ОТМЕНА или ESC.")).setDelayMsec(2500));
    }
}
