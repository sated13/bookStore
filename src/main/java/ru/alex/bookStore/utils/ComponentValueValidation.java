package ru.alex.bookStore.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;

public class ComponentValueValidation {

    public static void validate(AbstractField field, Validator validator, ConversionService conversionService, Class convertToClass) {
        field.addValueChangeListener(event -> {
            try {
                Object fieldValue = conversionService.convert(field.getValue(), convertToClass);

                ValidationResult result = validator.apply(fieldValue, new ValueContext(field));

                if (result.isError()) {
                    UserError error = new UserError(result.getErrorMessage());
                    field.setComponentError(error);
                } else {
                    field.setComponentError(null);
                }
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
                UserError error = new UserError("Wrong value");
                field.setComponentError(error);
            }
        });
    }

}
