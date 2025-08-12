package ru.ogma.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.PersonJsonDecodingException;

public class PersonParse {
    public static Person parseJsonToPerson(String jsonString) throws PersonJsonDecodingException {
        Gson gson = new Gson();
        try {
            Person person = (Person) gson.fromJson(jsonString, Person.class);
            return person;
        } catch (JsonSyntaxException e) {
            throw new PersonJsonDecodingException(e.getMessage());
        }
    }


}
