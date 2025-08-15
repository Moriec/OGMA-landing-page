package ru.ogma.utils;

import org.junit.jupiter.api.Test;
import ru.ogma.entities.Person;

import static org.junit.jupiter.api.Assertions.*;

public class PersonValidatorTest {
    @Test
    public void personIsNull() {
        assertThrows(NullPointerException.class, () -> PersonValidator.isPersonValidate(null));
    }

    @Test
    public void personUsernameIsNull() {
        Person person = new Person(null, "email1@gmail.com");
        assertThrows(NullPointerException.class, () -> PersonValidator.isPersonValidate(person));
    }

    @Test
    public void personEmailIsNull() {
        Person person = new Person("email1", null);
        assertThrows(NullPointerException.class, () -> PersonValidator.isPersonValidate(person));
    }

    @Test
    public void personIsInvalidEmail() {
        Person person = new Person("email1", "email1@@gmail.com");
        assertFalse(PersonValidator.isPersonValidate(person));
    }

    @Test
    public void personIsInvalidEmail2() {
        Person person = new Person("email1", "");
        assertFalse(PersonValidator.isPersonValidate(person));
    }

    @Test
    public void personIsInvalidUsername() {
        Person person = new Person("", "email1");
        assertFalse(PersonValidator.isPersonValidate(person));
    }

    @Test
    public void personIsValidEmail() {
        Person person = new Person("email1", "email1@gmail.com");
        assertTrue(PersonValidator.isPersonValidate(person));
    }
}
