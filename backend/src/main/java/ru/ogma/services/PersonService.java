package ru.ogma.services;

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

    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private int savePerson(Person person){
        try{
            try{
                personRepository.save(person);
            }catch (EmailAlreadyExistsException e){
                personRepository.update(person);
            }
            return HTTPStatus.NO_CONTENT.code(); // 204
        } catch (DatabaseOperationException | PersonNotFoundException e) {
            return HTTPStatus.INTERNAL_SERVER_ERROR.code(); // 500
        }
    }

    public int savePerson(String bodyJson){
        try {
            Person person = PersonParse.parseJsonToPerson(bodyJson);
            if (!PersonValidator.isPersonValidate(person)) {
                return HTTPStatus.UNPROCESSABLE_CONTENT.code(); // 422
            }

            return savePerson(person);
        } catch (PersonJsonDecodingException e) {
            return HTTPStatus.BAD_REQUEST.code(); // 400
        } catch (NullPointerException e){
            return HTTPStatus.INTERNAL_SERVER_ERROR.code(); // 500
        }
    }
}
