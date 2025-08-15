package ru.ogma.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ogma.entities.Person;
import ru.ogma.exceptions.DatabaseOperationException;
import ru.ogma.exceptions.EmailAlreadyExistsException;
import ru.ogma.exceptions.PersonNotFoundException;
import ru.ogma.repositories.PersonRepository;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC-реализация {@link ru.ogma.repositories.PersonRepository} на основе {@link DataSource}.
 *
 * Поддерживает операции сохранения и обновления по полю email с проверкой уникальности.
 * В случае ошибок уровня JDBC оборачивает их в {@link DatabaseOperationException}.
 */
public class PersonRepositoryJDBCImpl implements PersonRepository {

    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryJDBCImpl.class);

    private final String SQL_INSERT = "insert into persons (username, email) values (?, ?)";
    private final String SQL_EXISTS_BY_EMAIL = "select 1 from persons where email = ? limit 1";
    private final String SQL_UPDATE = "update persons set username = ? where email = ?";

    public PersonRepositoryJDBCImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Сохраняет нового пользователя. Бросает {@link EmailAlreadyExistsException},
     * если запись с таким email уже существует.
     */
    @Override
    public void save(Person person) throws EmailAlreadyExistsException {

        if(existsByEmail(person.getEmail())){
            logger.warn("Этот емайл уже существует в бд");
            throw new EmailAlreadyExistsException("Емайл уже существует");
        }

        executeRequest(person, SQL_INSERT);
    }

    /**
     * Обновляет имя пользователя по email. Бросает {@link PersonNotFoundException},
     * если запись с таким email отсутствует.
     */
    @Override
    public void update(Person person) throws DatabaseOperationException, PersonNotFoundException {
        if(!existsByEmail(person.getEmail())){
            logger.error("Попытка обновить Person в бд когда бд не содержит такой же емайл");
            throw new PersonNotFoundException("База данных не содержит email " + person.getEmail());
        }
        executeRequest(person, SQL_UPDATE);
    }

    /**
     * Выполняет SQL-запрос и считывает сгенерированный ключ.
     * В случае ошибок JDBC выбрасывает {@link DatabaseOperationException}.
     */
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
                    logger.info("Успешно добавлен Person в бд с id {}", id);
                }
                else{
                    logger.error("Не удалось получить id добавленной записи");
                    throw new SQLException("Не удалось получить id записи");
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка в работе бд: {}", e.getMessage());
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    /**
     * Проверяет существование записи по email.
     */
    private boolean existsByEmail(String email){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Ошибка в работе бд при поиске одинаковых емайл: {}", e.getMessage());
            throw new DatabaseOperationException(e.getMessage());
        }
    }
}
