package ru.ogma.utils;

import org.apache.commons.validator.routines.EmailValidator;
import ru.ogma.entities.Person;

public class PersonValidator {
    public static boolean isPersonValidate(Person person) throws NullPointerException {
        if (person == null) {
            throw new NullPointerException("Person is null");
        }
        return isNameCorrect(person) && isEmailCorrect(person);
    }

    private static boolean isNameCorrect(Person person) throws NullPointerException {
        if (person.getUsername() == null) {
            throw new NullPointerException("Person.Username is null");
        }
        if (person.getUsername().length() > 50 || person.getUsername().isEmpty()) {
            return false;
        }
        return true;
    }

    private static boolean isEmailCorrect(Person person) throws NullPointerException {
        if (person.getEmail() == null) {
            throw new NullPointerException("Person.Email is null");
        }
        return EmailValidator.getInstance().isValid(person.getEmail());
    }
}
