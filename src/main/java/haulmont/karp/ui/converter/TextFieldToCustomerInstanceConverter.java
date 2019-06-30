package haulmont.karp.ui.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextFieldToCustomerInstanceConverter implements Converter<String, Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public Result<Customer> convertToModel(String lastName, ValueContext context) {
        if (customerService.getByLastName(lastName) == null) return Result.error("Клиента с такой фамилией не существует в базе Автосервиса");
        else return Result.ok(customerService.getByLastName(lastName));
    }

    @Override
    public String convertToPresentation(Customer customer, ValueContext context) {
        if (customer == null) return "";
        else return customer.getLastName();
    }
}
