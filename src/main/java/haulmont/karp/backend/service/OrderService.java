package haulmont.karp.backend.service;

import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Order;

import java.util.List;

public interface OrderService {

    Order saveOrder(Order order);

    Order getOrderOne(Long id);

    List<Order> getAllOrder();

    void deleteOrder(Long id);

    void deleteOrder(Order order);

    Order updateOrder(Order order);

    boolean existOrder(Long id);

    List <Order> findBySpecification(String specification);

    List<Order> findByStatus(String statusOrder);

    List<Order> findByCustomer(Customer customer);
}

