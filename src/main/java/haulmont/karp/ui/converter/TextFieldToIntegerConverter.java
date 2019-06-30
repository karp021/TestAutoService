package haulmont.karp.ui.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import org.springframework.stereotype.Component;

@Component
public class TextFieldToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Result<Integer> convertToModel(String textField, ValueContext context) {
        if (Integer.parseInt(textField.substring(0,1)) == 0) return Result.error("Недопустимое значение для ввода");
        return Result.ok(Integer.parseInt(textField));
    }

    @Override
    public String convertToPresentation(Integer value, ValueContext context) {
        if (value == null) return "";
        return value.toString();
    }
}
