package ru.ogma.services;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.EmailAlreadyExistsException;
import ru.ogma.http.HTTPStatus;
import ru.ogma.repositories.PersonRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    public void emailAlreadyExists() {
        String jsonString = "  {\"username\" : \"email\", \n  \"email\" : \"email@gmail.com\"}  ";
        Person person = new Gson().fromJson(jsonString, Person.class);
        Mockito.doThrow(EmailAlreadyExistsException.class).when(personRepository).save(person);
        assertEquals(HTTPStatus.NO_CONTENT.code(), personService.savePerson(jsonString));
    }


    @Test
    public void personIsNull() {
        assertEquals(HTTPStatus.BAD_REQUEST.code(), personService.savePerson(null));
    }

    @Test
    public void personIsEmpty() {
        assertEquals(HTTPStatus.BAD_REQUEST.code(), personService.savePerson(""));
    }

    @Test
    public void personIsValid() {
        String jsonString = "  {\"username\" : \"email\", \n  \"email\" : \"email2@gmail.com\"}  ";
        assertEquals(HTTPStatus.NO_CONTENT.code(), personService.savePerson(jsonString));
    }

    @Test
    public void personIsInvalidEmail() {
        String jsonString = "  {\"username\" : \"email\", \n  \"email\" : \"email2@@gmail.com\"}  ";
        assertEquals(HTTPStatus.UNPROCESSABLE_CONTENT.code(), personService.savePerson(jsonString));
    }

    @Test
    public void personIsInvalidUsername() {
        String jsonString = "  {\"username\" : \"\", \n  \"email\" : \"email2@gmail.com\"}  ";
        assertEquals(HTTPStatus.UNPROCESSABLE_CONTENT.code(), personService.savePerson(jsonString));
    }

    @Test
    public void personIsEmptyEmail() {
        String jsonString = "  {\"username\" : \"email\"}  ";
        assertEquals(HTTPStatus.BAD_REQUEST.code(), personService.savePerson(jsonString));
    }

    @Test
    public void personIsEmptyUsername() {
        String jsonString = "  {\"email\" : \"email2@@gmail.com\"}  ";
        assertEquals(HTTPStatus.BAD_REQUEST.code(), personService.savePerson(jsonString));
    }
}
