package haulmont.karp.ui.view;


import com.vaadin.spring.annotation.UIScope;
import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.service.MechanicService;
import haulmont.karp.ui.form.MechanicForm;
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
@SpringView(name = MechanicsView.VIEW_NAME)
public class MechanicsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "mechanics";
    private VerticalLayout layout = new VerticalLayout();
    private TextField filterLastName = new TextField();
    private CssLayout filteringLastName = new CssLayout();
    private Button clearFilterTextBtn = new Button();
    private HorizontalLayout main = new HorizontalLayout();
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addMechanicBtn = new Button();
    private Button changeFieldBtn = new Button();

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private MechanicForm mechanicForm;

    @Autowired
    private Grid<Mechanic> mechanicGrid;

    @PostConstruct
    void init() {
        gridInit();
        creationLastNameFilter();
        selectField();
        creationAddMechanicBtn();
        createNotification();
        initLayout();
    }

    private void gridInit() { mechanicGrid.setItems(mechanicService.getAllMechanic()); }

    private void creationLastNameFilter() {

        filterLastName.setPlaceholder("поиск по фамилии...");
        filterLastName.addValueChangeListener(e -> updateListMechanicsForFilter());
        filterLastName.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterTextBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Очистить текущий фильр");
        clearFilterTextBtn.addClickListener(e -> filterLastName.clear());

        filteringLastName.addComponents(filterLastName, clearFilterTextBtn);
        filteringLastName.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    }

    private void updateListMechanicsForFilter() {
        List<Mechanic> mechanicsList = mechanicService.findByLastName(filterLastName.getValue());
        mechanicGrid.setItems(mechanicsList);
    }


    private void initLayout(){
        toolbar.addComponents(filteringLastName, addMechanicBtn, changeFieldBtn);
        main.addComponents(mechanicGrid, mechanicForm);
        main.setSizeFull();
        mechanicGrid.setSizeFull();
        main.setExpandRatio(mechanicGrid, 1);

        mechanicForm.setVisible(false);
        layout.addComponents(toolbar, main);
        addComponent(layout);
    }
    private void createNotification(){
        changeFieldBtn.setIcon(VaadinIcons.FILE_REFRESH);
        changeFieldBtn.setCaption("Изменить данные");
        changeFieldBtn.addClickListener(
                event -> Notification.show("Для изменения данных или удаления из списка кликните по полю сотрудника").setDelayMsec(2500));
    }

    private void selectField() {
        mechanicGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                mechanicForm.setVisible(false);
            }
            else {
                clearFilterTextBtn.click();
                mechanicForm.setMechanic(event.getValue());
            }
        });
    }

    private void creationAddMechanicBtn() {
        addMechanicBtn.setIcon(VaadinIcons.FILE_ADD);
        addMechanicBtn.setCaption("Добавить нового механика");
        addMechanicBtn.addClickListener(e ->{
            clearFilterTextBtn.click();
            mechanicGrid.asSingleSelect().clear();
            mechanicForm.setMechanic(new Mechanic());
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
