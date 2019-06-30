package haulmont.karp.ui.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import haulmont.karp.backend.models.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TextFieldToStatusConverter implements Converter<OrderStatus, String> {
    @Override
    public Result<String> convertToModel(OrderStatus status, ValueContext context) {
        if (status == null) return Result.ok(OrderStatus.SCHEDULED.getStatus());
        else return Result.ok(status.getStatus());

    }

    @Override
    public OrderStatus convertToPresentation(String statusStr, ValueContext context) {
        return OrderStatus.fromString(statusStr);
    }
}
