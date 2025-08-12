package ru.ogma.app;

import com.sun.net.httpserver.HttpServer;
import ru.ogma.controllers.RegisterPersonController;
import ru.ogma.repositories.PersonRepository;
import ru.ogma.repositories.datasource.ConnectionDataSource;
import ru.ogma.repositories.impl.PersonRepositoryJDBCImpl;
import ru.ogma.services.PersonService;
import ru.ogma.utils.PropertiesSingleton;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            // Конфигурация
            PropertiesSingleton.addLocationConfigFile("backend/config.properties");

            // Настройка репозитория
            DataSource dataSource = new ConnectionDataSource(PropertiesSingleton.get("database.url"),
                    PropertiesSingleton.get("database.username"),
                    PropertiesSingleton.get("database.password"));

            PersonRepository personRepository = new PersonRepositoryJDBCImpl(dataSource);

            // Настройка сервисов
            PersonService personService = new PersonService(personRepository);

            // Настройка контроллеров
            RegisterPersonController controller = new RegisterPersonController(personService);

            //Настройка сервера
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(PropertiesSingleton.get("server.port"))), 0);
            server.createContext("/register", controller);
            server.setExecutor(null); // Используем дефолтный executor
            server.start();
            System.out.println("Сервер запущен на порту 8080");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
