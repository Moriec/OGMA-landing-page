package ru.ogma.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.PersonJsonDecodingException;

import static org.junit.jupiter.api.Assertions.*;


public class PersonParseTest {
    @Test
    public void personIsNull() {
        assertThrows(PersonJsonDecodingException.class, () -> PersonParse.parseJsonToPerson(null));
    }

    @Test
    public void personIsEmpty() {
        assertThrows(PersonJsonDecodingException.class, () -> PersonParse.parseJsonToPerson(""));
    }

    @Test
    public void personIsInvalid() {
        assertThrows(PersonJsonDecodingException.class, () -> PersonParse.parseJsonToPerson("}"));
    }

    @Test
    public void personIsValid() {
        Person person = new Person("dima", "email1@gmail.com");
        String jsonString = "  {\"username\" : \"dima\", \n  \"email\" : \"email1@gmail.com\"}  ";
        assertEquals(person, PersonParse.parseJsonToPerson(jsonString));
    }

    @Test
    public void personIsValidWithNullEmail() {
        String jsonString = "  {\"username\" : \"dima\"}  ";
        assertThrows(PersonJsonDecodingException.class, () -> PersonParse.parseJsonToPerson(jsonString));
    }

    @Test
    public void personIsValidWithNullUsername() {
        String jsonString = "  {  \"email\" : \"email1@gmail.com\"}  ";
        assertThrows(PersonJsonDecodingException.class, () -> PersonParse.parseJsonToPerson(jsonString));
    }

}
