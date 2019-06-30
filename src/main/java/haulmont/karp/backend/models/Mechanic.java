package haulmont.karp.backend.models;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Mechanics")
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToMany (mappedBy = "mechanic", cascade = CascadeType.REFRESH, orphanRemoval = false)
    private Set<Order> orders;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "payment")
    private int paymentPerHour;

    @Column(name = "order_statistics")
    private int amountOrders;


    public Mechanic() {}

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPaymentPerHour() {
        return paymentPerHour;
    }

    public void setPaymentPerHour(int paymentPerHour) {
        this.paymentPerHour = paymentPerHour;
    }

    public int getAmountOrders() {
        return amountOrders;
    }

    public void setAmountOrders(int amountOrders) {
        this.amountOrders = amountOrders;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return id == mechanic.id &&
                paymentPerHour == mechanic.paymentPerHour &&
                amountOrders == mechanic.amountOrders &&
                firstName.equals(mechanic.firstName) &&
                secondName.equals(mechanic.secondName) &&
                lastName.equals(mechanic.lastName);
    }

    public boolean equalsBeforeSaving(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return
                paymentPerHour == mechanic.paymentPerHour &&
                firstName.equals(mechanic.firstName) &&
                secondName.equals(mechanic.secondName) &&
                lastName.equals(mechanic.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, lastName, paymentPerHour, amountOrders);
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", paymentPerHour=" + paymentPerHour +
                ", amountOrders=" + amountOrders +
                '}';
    }
}



