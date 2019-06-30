package haulmont.karp.ui.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import org.springframework.stereotype.Component;

@Component
public class TextFieldToNumberPhoneConverter implements Converter<String, String>  {
    @Override
    public Result<String> convertToModel(String numberPhone, ValueContext context) {
        if (Integer.parseInt(numberPhone.substring(0,1)) == 0) return Result.error("Телефонный номер не может начинаться с нуля после кода страны");
        return Result.ok("+7" + numberPhone);
    }

    @Override
    public String convertToPresentation(String numberPhone, ValueContext context) {
        if (numberPhone == null) return "";
        return numberPhone.substring(2);
    }
}
