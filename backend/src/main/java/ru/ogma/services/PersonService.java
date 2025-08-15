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


/**
 * Сервис для регистрации/сохранения сущности {@link ru.ogma.entities.Person}.
 *
 * Отвечает за оркестрацию этапов:
 * - парсинг JSON в {@link ru.ogma.entities.Person}
 * - валидацию данных
 * - сохранение/обновление через {@link PersonRepository}
 *
 * Возвращает HTTP-коды из {@link HTTPStatus} для упрощения маппинга на ответ контроллера:
 * - 204 (NO_CONTENT) — успешное сохранение/обновление
 * - 400 (BAD_REQUEST) — некорректный JSON или отсутствующие обязательные поля
 * - 422 (UNPROCESSABLE_CONTENT) — данные валидного формата, но не проходят бизнес-валидацию
 * - 500 (INTERNAL_SERVER_ERROR) — ошибки уровня БД или прочие критические сбои
 */
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Сохраняет {@link Person}. Если email уже существует — выполняет обновление записи.
     */
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

    /**
     * Принимает тело запроса в формате JSON, конвертирует в {@link Person}, валидирует
     * и сохраняет/обновляет через репозиторий.
     */
    public int savePerson(String bodyJson) {
        try {
            Person person = PersonParse.parseJsonToPerson(bodyJson);
            if (!PersonValidator.isPersonValidate(person)) {
                logger.warn("Данные, присланные клиентом, не являются валидными");
                return HTTPStatus.UNPROCESSABLE_CONTENT.code(); // 422
            }
            return savePerson(person);
        } catch (PersonJsonDecodingException | NullPointerException e) {
            logger.warn("Плохой запрос от клиента: {}", e.getMessage());
            return HTTPStatus.BAD_REQUEST.code(); // 400
        }
    }
}
