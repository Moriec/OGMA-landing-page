package ru.ogma.app;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ogma.controllers.RegisterPersonController;
import ru.ogma.repositories.PersonRepository;
import ru.ogma.repositories.datasource.ConnectionDataSource;
import ru.ogma.repositories.impl.PersonRepositoryJDBCImpl;
import ru.ogma.services.PersonService;
import ru.ogma.utils.PropertiesSingleton;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Начало запуска сервера");

            // Конфигурация
            PropertiesSingleton.addLocationConfigFile("backend/config.properties");
            logger.info("Получен файл конфигурации");

            // Настройка репозитория
            DataSource dataSource = new ConnectionDataSource(PropertiesSingleton.get("database.url"),
                    PropertiesSingleton.get("database.username"),
                    PropertiesSingleton.get("database.password"));
            PersonRepository personRepository = new PersonRepositoryJDBCImpl(dataSource);
            logger.info("Настройка репозитория завершена: {}", PropertiesSingleton.get("database.url"));

            // Настройка сервисов
            PersonService personService = new PersonService(personRepository);
            logger.info("Настройка сервисов завершена");

            // Настройка контроллеров
            RegisterPersonController controller = new RegisterPersonController(personService);
            logger.info("Настройка контроллеров завершена");

            //Настройка сервера
            logger.info("Запуск сервера:");
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(PropertiesSingleton.get("server.port"))), 0);
            server.createContext("/register", controller);
            server.setExecutor(null); // Используем дефолтный executor
            server.start();
            logger.info("Сервер запущен на порту {}", Integer.parseInt(PropertiesSingleton.get("server.port")));

            while(true) {
                String consoleRequest = new Scanner(System.in).nextLine();
                if (consoleRequest.equals("exit")) {
                    server.stop(0);
                    logger.info("Плановая остановка сервера\n");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
