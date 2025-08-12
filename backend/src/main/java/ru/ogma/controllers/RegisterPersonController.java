package ru.ogma.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ogma.http.HTTPStatus;
import ru.ogma.services.PersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class RegisterPersonController implements HttpHandler {

    private final PersonService personService;

    public RegisterPersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void handle(HttpExchange exchange){
        try(exchange) {
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
            e.printStackTrace();
            safeSendError(exchange);
        }
    }

    private void doPost(HttpExchange exchange) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String body = br.lines().collect(Collectors.joining("\n"));

            int code = personService.savePerson(body);
            addCors(exchange);
            exchange.sendResponseHeaders(code, -1);
        }
        catch (Exception e) {
            addCors(exchange);
            exchange.sendResponseHeaders(HTTPStatus.INTERNAL_SERVER_ERROR.code(), -1);
        }
    }

    private void doOptions(HttpExchange exchange) throws IOException {
        addCors(exchange);
        exchange.sendResponseHeaders(HTTPStatus.NO_CONTENT.code(), -1);
    }

    private void invalidRequestType(HttpExchange exchange) throws IOException {
        addCors(exchange);
        System.out.print("Запрос пришел");
        exchange.sendResponseHeaders(HTTPStatus.METHOD_NOT_ALLOWED.code(), -1);
    }

    private void addCors(HttpExchange exchange) {
        var h = exchange.getResponseHeaders();
        h.add("Access-Control-Allow-Origin", "*");
        h.add("Access-Control-Allow-Headers", "Content-Type");
        h.add("Access-Control-Allow-Methods", "POST, OPTIONS");
    }

    private void safeSendError(HttpExchange exchange){
        try {
            addCors(exchange);
            exchange.sendResponseHeaders(HTTPStatus.INTERNAL_SERVER_ERROR.code(), -1);
        } catch (IOException e) {
            // возможно, заголовки уже отправлены или соединение закрыто
        }
    }
}
