package ru.ogma.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.PersonJsonDecodingException;

public class PersonParse {
    public static Person parseJsonToPerson(String jsonString) throws PersonJsonDecodingException, NullPointerException {
        if (jsonString == null) {
            throw new NullPointerException("Json string is null");
        }
        if (jsonString.isEmpty()) {
            throw new PersonJsonDecodingException("Json string is empty");
        }
        Gson gson = new Gson();
        try {
            Person person = (Person) gson.fromJson(jsonString, Person.class);
            if (person.getUsername() == null || person.getEmail() == null) {
                throw new PersonJsonDecodingException("username or email is null");
            }
            return person;
        } catch (JsonSyntaxException e) {
            throw new PersonJsonDecodingException(e.getMessage());
        }
    }
}
