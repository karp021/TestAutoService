package haulmont.karp.backend.repos;

import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findBySpecificationStartsWithIgnoreCase(String specification);
    List<Order> findByStatusStartsWithIgnoreCase(String statusOrder);
    List<Order> findByCustomer(Customer customer);
}
