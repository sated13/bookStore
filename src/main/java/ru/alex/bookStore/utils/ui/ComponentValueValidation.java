package ru.alex.bookStore.utils.ui;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;

@Slf4j
public class ComponentValueValidation {

    @SuppressWarnings("unchecked")
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
            } catch (Exception e) {
                log.debug("Error during validating: {}", e);
                UserError error = new UserError("Wrong value");
                field.setComponentError(error);
            }
        });
    }

    public static boolean addErrorOnComponent(AbstractComponent componentForAddingError, boolean resultOfValidation,
                                              String errorMessage) {
        boolean returningResult = false;
        if (resultOfValidation) {
            UserError error = new UserError(errorMessage);
            componentForAddingError.setComponentError(error);
            returningResult = true;
        } else {
            componentForAddingError.setComponentError(null);
            returningResult = false;
        }
        return returningResult;
    }

}
