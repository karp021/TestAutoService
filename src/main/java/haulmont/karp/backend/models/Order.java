package haulmont.karp.backend.models;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;



@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_mechanic", nullable = false)
    private Mechanic mechanic;

    private String specification;

    @Column(name = "date_of_creation")
    private LocalDate dateCreation;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "price_of_services")
    private int price;

    private String status;

    public Order() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getId() { return id; }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                price == order.price &&
                customer.equals(order.customer) &&
                mechanic.equals(order.mechanic) &&
                specification.equals(order.specification) &&
                dateCreation.equals(order.dateCreation) &&
                expirationDate.equals(order.expirationDate) &&
                status.equals(order.status);
    }

    public boolean equalsBeforeSaving(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return
                price == order.price &&
                mechanic.equals(order.mechanic) &&
                specification.equals(order.specification) &&
                expirationDate.equals(order.expirationDate) &&
                status.equals(order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, mechanic, specification, dateCreation, expirationDate, price, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", specification='" + specification + '\'' +
                ", customer=" + customer +
                ", mechanic=" + mechanic +
                ", dateCreation=" + dateCreation +
                ", expirationDate=" + expirationDate +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
