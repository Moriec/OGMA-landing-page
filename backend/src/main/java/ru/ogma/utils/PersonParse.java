package ru.ogma.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.PersonJsonDecodingException;

/**
 * Утилита для преобразования JSON-строки в {@link ru.ogma.entities.Person} с помощью Gson.
 * Выполняет базовые проверки входных данных и гарантирует ненулевые поля username и email.
 */
public class PersonParse {
    /**
     * Парсит JSON-строку в {@link Person}. Бросает исключения при null/empty входе,
     * синтаксических ошибках JSON или отсутствии обязательных полей.
     */
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
