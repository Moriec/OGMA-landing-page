package ru.ogma.repositories;

import ru.ogma.entities.Person;
import ru.ogma.exceptions.DatabaseOperationException;
import ru.ogma.exceptions.EmailAlreadyExistsException;

public interface PersonRepository {

    // Сохраняет Person в бд.
    void save(Person person) throws DatabaseOperationException, EmailAlreadyExistsException;

    //Обновляет Person в бд, если существует тот же емайл.
    void update(Person person) throws DatabaseOperationException;
}
