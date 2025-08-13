package ru.ogma.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.DatabaseOperationException;
import ru.ogma.exceptions.EmailAlreadyExistsException;
import ru.ogma.exceptions.PersonJsonDecodingException;
import ru.ogma.exceptions.PersonNotFoundException;
import ru.ogma.http.HTTPStatus;
import ru.ogma.repositories.PersonRepository;

import ru.ogma.utils.PersonParse;
import ru.ogma.utils.PersonValidator;


public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private int savePerson(Person person) {
        try {
            try {
                personRepository.save(person);
            } catch (EmailAlreadyExistsException e) {
                logger.warn(e.getMessage());
                personRepository.update(person);
            }
            logger.info("Отправить ответ с кодом {}", HTTPStatus.NO_CONTENT.code());
            return HTTPStatus.NO_CONTENT.code(); // 204
        } catch (DatabaseOperationException | PersonNotFoundException e) {
            logger.warn("Критическая ошибка при попытке сохранения Person: {}", e.getMessage());
            return HTTPStatus.INTERNAL_SERVER_ERROR.code(); // 500
        }
    }

    public int savePerson(String bodyJson) {
        try {
            Person person = PersonParse.parseJsonToPerson(bodyJson);
            if (!PersonValidator.isPersonValidate(person)) {
                logger.warn("Данные, присланные клиентом, не являются валидными");
                return HTTPStatus.UNPROCESSABLE_CONTENT.code(); // 422
            }
            return savePerson(person);
        } catch (PersonJsonDecodingException e) {
            logger.warn("Плохой запрос от клиента: ", e.getMessage());
            return HTTPStatus.BAD_REQUEST.code(); // 400
        } catch (NullPointerException e) {
            logger.error("Одна из переменных не инициализирована: {}", e.getMessage());
            return HTTPStatus.INTERNAL_SERVER_ERROR.code(); // 500
        }
    }
}
