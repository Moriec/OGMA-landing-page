package ru.ogma.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ogma.http.HTTPStatus;
import ru.ogma.services.PersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class RegisterPersonController implements HttpHandler {

    private final PersonService personService;
    private static final Logger logger = LoggerFactory.getLogger(RegisterPersonController.class);

    public RegisterPersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        logger.info("Перехвачен запрос с методом {}", exchange.getRequestMethod());
        try (exchange) {
            switch (exchange.getRequestMethod()) {
                case "POST": {
                    doPost(exchange);
                    break;
                }
                case "OPTIONS": {
                    doOptions(exchange);
                    break;
                }
                default: {
                    invalidRequestType(exchange);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            safeSendError(exchange);
        }
    }

    private void doPost(HttpExchange exchange) throws IOException {
        logger.debug("Обработка метода Post");
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String body = br.lines().collect(Collectors.joining("\n"));

            int code = personService.savePerson(body);
            addCors(exchange);
            exchange.sendResponseHeaders(code, -1);
            logger.info("Для Post метода отправлен ответ с кодом {}", code);
        } catch (Exception e) {
            addCors(exchange);
            exchange.sendResponseHeaders(HTTPStatus.INTERNAL_SERVER_ERROR.code(), -1);
            logger.info("Критическая ошибка при обработке Post метода: {}", e.getMessage());
        }
    }

    private void doOptions(HttpExchange exchange) throws IOException {
        addCors(exchange);
        exchange.sendResponseHeaders(HTTPStatus.NO_CONTENT.code(), -1);
        logger.info("Для OPTIONS метода отправлен код: {}", HTTPStatus.NO_CONTENT.code());
    }

    private void invalidRequestType(HttpExchange exchange) throws IOException {
        addCors(exchange);
        exchange.sendResponseHeaders(HTTPStatus.METHOD_NOT_ALLOWED.code(), -1);
        logger.debug("Для {} метода отправлен код: {}", exchange.getRequestMethod(), HTTPStatus.METHOD_NOT_ALLOWED.code());
    }

    private void addCors(HttpExchange exchange) {
        var h = exchange.getResponseHeaders();
        h.add("Access-Control-Allow-Origin", "*");
        h.add("Access-Control-Allow-Headers", "Content-Type");
        h.add("Access-Control-Allow-Methods", "POST, OPTIONS");
    }

    private void safeSendError(HttpExchange exchange) {
        try {
            addCors(exchange);
            exchange.sendResponseHeaders(HTTPStatus.INTERNAL_SERVER_ERROR.code(), -1);
            logger.error("Успешно отправлен код: {}", HTTPStatus.INTERNAL_SERVER_ERROR.code());
        } catch (IOException e) {
            logger.error("Не получилось отправить код критической ошибки: {}", e.getMessage());
        }
    }
}
