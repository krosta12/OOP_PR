package org.example.ooppr.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Подключился к серверу!");

            // Можно запустить отдельный поток для прослушивания сообщений от сервера
            new Thread(this::listenToServer).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        try {
            Object obj;
            while ((obj = in.readObject()) != null) {
                if (obj instanceof Data data) {
                    System.out.println("Получены данные от сервера: x=" + data.getX() + ", y=" + data.getY());
                    // Тут можно что-то делать с этими данными
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Отключен от сервера.");
        }
    }

    public void sendData(Data data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            System.out.println("Не удалось отправить данные серверу.");
        }
    }
}
