package ru.ogma.console;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AdminConsole extends Thread {

    private HttpServer server;
    private static final Logger logger = LoggerFactory.getLogger(AdminConsole.class);
    private boolean running = true;

    public AdminConsole(HttpServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            logger.info("Запущена админ консоль");

            while (running) {
                String cmd = sc.nextLine();
                handleCommand(cmd);
            }
        }
    }

    public void handleCommand(String cmd) {
        switch (cmd) {
            case "exit": {
            }
            case "quit": {
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
