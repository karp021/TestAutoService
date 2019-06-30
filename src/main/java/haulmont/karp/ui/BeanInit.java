package haulmont.karp.ui;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.models.Order;
import com.vaadin.ui.Grid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class BeanInit {

    @Bean
    @UIScope
    public Grid<Order> orderGrid() {
        Grid<Order> orderGrid = new Grid<>();
        orderGrid.addColumn(Order::getSpecification).setCaption("Описание работ");
        orderGrid.addColumn(Order::getDateCreation).setCaption("Дата оформления заказа");
        orderGrid.addColumn(Order::getExpirationDate).setCaption("Дата окончания работ");
        orderGrid.addColumn(order -> order.getCustomer().getLastName()).setCaption("Клиент");
        orderGrid.addColumn(order -> order.getMechanic().getLastName()).setCaption("Механик");
        orderGrid.addColumn(Order::getPrice).setCaption("Стоимость");
        orderGrid.addColumn(Order::getStatus).setCaption("Статус заказа");
        return orderGrid;
    }

    @Bean
    @UIScope
    public Grid<Customer> customerGrid() {
        Grid<Customer> customerGrid = new Grid<>();
        customerGrid.addColumn(Customer::getLastName).setCaption("Фамилия");
        customerGrid.addColumn(Customer::getFirstName).setCaption("Имя");
        customerGrid.addColumn(Customer::getSecondName).setCaption("Отчество");
        customerGrid.addColumn(Customer::getPhoneNumber).setCaption("Контактный номер");

        return customerGrid;
    }

    @Bean
    @UIScope
    public Grid<Mechanic> mechanicGrid() {
        Grid<Mechanic> mechanicGrid = new Grid<>();
        mechanicGrid.addColumn(Mechanic::getLastName).setCaption("Фамилия");
        mechanicGrid.addColumn(Mechanic::getFirstName).setCaption("Имя");
        mechanicGrid.addColumn(Mechanic::getSecondName).setCaption("Отчество");
        mechanicGrid.addColumn(Mechanic::getPaymentPerHour).setCaption("Ставка в час");
        mechanicGrid.addColumn(Mechanic::getAmountOrders).setCaption("Кол-во заказов");

        return mechanicGrid;
    }
    @Bean
    @UIScope
    public ComboBox<String> filterCustomerLastName() {
        ComboBox<String> filterCustomerLastName = new ComboBox<>();
        return filterCustomerLastName;
    }

}
