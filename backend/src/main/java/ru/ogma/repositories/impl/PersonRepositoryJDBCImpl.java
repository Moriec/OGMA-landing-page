package ru.ogma.repositories.impl;

import ru.ogma.entities.Person;
import ru.ogma.exceptions.DatabaseOperationException;
import ru.ogma.exceptions.EmailAlreadyExistsException;
import ru.ogma.exceptions.PersonNotFoundException;
import ru.ogma.repositories.PersonRepository;

import javax.sql.DataSource;
import java.sql.*;

public class PersonRepositoryJDBCImpl implements PersonRepository {

    private DataSource dataSource;

    private final String SQL_INSERT = "insert into persons (username, email) values (?, ?)";
    private final String SQL_EXISTS_BY_EMAIL = "select 1 from persons where email = ? limit 1";
    private final String SQL_UPDATE = "update persons set username = ? where email = ?";

    public PersonRepositoryJDBCImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Person person) throws EmailAlreadyExistsException {
        if(existsByEmail(person.getEmail())){
            throw new EmailAlreadyExistsException("Емайл уже существует");
        }

        executeRequest(person, SQL_INSERT);
    }

    public void update(Person person) throws DatabaseOperationException, PersonNotFoundException {
        if(!existsByEmail(person.getEmail())){
            throw new PersonNotFoundException("База данных не содержит email " + person.getEmail());
        }
        executeRequest(person, SQL_UPDATE);
    }

    private void executeRequest(Person person, String sqlUpdate) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate,  Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, person.getUsername());
            preparedStatement.setString(2, person.getEmail());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    person.setId(id);
                }
                else{
                    throw new SQLException("Не удалось получить id записи");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public boolean existsByEmail(String email){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }
}
