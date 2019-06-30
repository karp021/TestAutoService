package haulmont.karp.ui.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextFieldToMechanicInstanceConverter implements Converter<String, Mechanic> {

    @Autowired
    private MechanicService mechanicService;

    @Override
    public Result<Mechanic> convertToModel(String lastName, ValueContext context) {
        if (mechanicService.getByLastName(lastName) == null) return Result.error("Мeханика с такой фамилией не существует в базе Автосервиса");
        else return Result.ok(mechanicService.getByLastName(lastName));
    }

    @Override
    public String convertToPresentation(Mechanic mechanic, ValueContext context) {
        if (mechanic == null) return "";
        else return mechanic.getLastName();
    }
}
