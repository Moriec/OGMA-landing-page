package ru.ogma.repositories;

import ru.ogma.entities.Person;
import ru.ogma.exceptions.DatabaseOperationException;
import ru.ogma.exceptions.EmailAlreadyExistsException;

public interface PersonRepository {

    // Сохраняет Person в бд, возвращает код состояния.
    void save(Person person) throws DatabaseOperationException, EmailAlreadyExistsException;

    void update(Person person) throws DatabaseOperationException;
}
