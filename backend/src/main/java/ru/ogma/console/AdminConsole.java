package ru.ogma.console;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Админ-консоль, работающая в отдельном потоке, читает команды из стандартного ввода.
 *
 * Поддерживаемые команды:
 * - "exit" / "quit" — плановая остановка HTTP-сервера и завершение JVM.
 *
 * Назначение класса — предоставить простой способ управлять сервером из консоли
 * без внешних инструментов и REST-эндпоинтов.
 */
public class AdminConsole extends Thread {

    private HttpServer server;
    private static final Logger logger = LoggerFactory.getLogger(AdminConsole.class);
    private boolean running = true;

    public AdminConsole(HttpServer server) {
        this.server = server;
    }

    @Override
    /*
     * Главный цикл чтения команд.
     */
    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            logger.info("Запущена админ консоль");

            while (running) {
                String cmd = sc.nextLine();
                handleCommand(cmd);
            }
        }
    }

    /**
     * Обрабатывает одну команду консоли администратора.
     * Вызов {@code System.exit(0)} завершит всю JVM — учитывайте это при интеграции.
     */
    public void handleCommand(String cmd) {
        switch (cmd) {
            case "exit": {
                // fall-through: "exit" обрабатывается так же, как и "quit"
            }
            case "quit": {
                // Мгновенно останавливаем HTTP-сервер (0 — без ожидания активных запросов)
                server.stop(0);
                logger.info("Плановая остановка сервера");
                running = false;
                System.exit(0);
                break;
            }
            default: {
                logger.info("Неверно введенная команда: {}", cmd);
                System.out.println("Неверно введенная команда: " + cmd);
            }
        }
    }

}
