package ru.ogma.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ogma.entities.Person;

public class PersonValidator {

    private static final Logger logger = LoggerFactory.getLogger(PersonValidator.class);

    public static boolean isPersonValidate(Person person) throws NullPointerException {
        if (person == null) {
            logger.error("Person is null");
            throw new NullPointerException("Person is null");
        }
        return isNameCorrect(person) && isEmailCorrect(person);
    }

    private static boolean isNameCorrect(Person person) throws NullPointerException {
        if (person.getUsername() == null) {
            logger.error("Person.username is null");
            throw new NullPointerException("Person.Username is null");
        }
        if (person.getUsername().length() > 50 || person.getUsername().isEmpty()) {
            logger.debug("Person.username инвалидный");
            return false;
        }
        return true;
    }

    private static boolean isEmailCorrect(Person person) throws NullPointerException {
        if (person.getEmail() == null) {
            logger.error("Person.email is null");
            throw new NullPointerException("Person.Email is null");
        }
        if (EmailValidator.getInstance().isValid(person.getEmail())){
            return true;
        }
        logger.debug("Person.email инвалидный");
        return false;
    }
}
