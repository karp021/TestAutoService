package haulmont.karp.backend.service.impl;

import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Order;
import haulmont.karp.backend.repos.OrderRepository;
import haulmont.karp.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderOne(Long id) {
        return orderRepository.getOne(id);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.delete(id);
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }


    @Override
    public boolean existOrder(Long id) {
        return orderRepository.exists(id);
    }

    @Override
    public List<Order> findBySpecification(String specification) {
        return orderRepository.findBySpecificationStartsWithIgnoreCase(specification);
    }

    @Override
    public List<Order> findByStatus(String statusOrder) {
        return orderRepository.findByStatusStartsWithIgnoreCase(statusOrder);
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

}
