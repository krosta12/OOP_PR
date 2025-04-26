package org.example.ooppr.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    //vars for server work tracking
    private int port;
    private boolean isStarted = false;

    //UseState for clients
    private final Set<ObjectOutputStream> clientStreams = ConcurrentHashMap.newKeySet();

    /**
     *
     * @param port port for server
     *             func will setPort when init
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * runing server
     */
    public void start_host() {
        //TWR try to open port
//        ServerSocket serverSocket = new ServerSocket(this.port, 0, InetAddress.getByName("192.168.1.5")); if we have a server on this IP

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            //setFlag
            isStarted = true;
            //log
            System.out.println("server started at " + InetAddress.getLocalHost().getHostAddress() + ":" + port);

            //when it works will handle a new clients
            while (isStarted) {
                //get
                Socket socket = serverSocket.accept();
                //log
                System.out.println("new client connected: " + socket.getRemoteSocketAddress());

                //create a client and create for him a new Thread
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // кусок клиента надо отделить

    /**
     * class for working with server from client side
     */
    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                clientStreams.add(out);
                System.out.println("Новый клиент подключен: " + socket.getRemoteSocketAddress());

                Object obj;
                while ((obj = in.readObject()) != null) {
                    if (obj instanceof Data data) {
                        System.out.println("Получена Data от клиента: x=" + data.getX() + ", y=" + data.getY());
                        broadcast(data);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Клиент отключился: " + e.getMessage());
            } finally {
                if (out != null) {
                    clientStreams.remove(out);
                    try {
                        out.close();
                    } catch (IOException ignored) {}
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }


    /**
     *
     * @param data data for sharing between client and server
     *             function share date for all lients
     */
    private void broadcast(Data data) {
        for (ObjectOutputStream out : clientStreams) {
            try {
                out.writeObject(data);
                out.flush();
            } catch (IOException e) {
                System.out.println("Ошибка при отправке клиенту: " + e.getMessage());
            }
        }
    }


    public void connectToHost(String ip, int port){
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Подключен к серверу: " + ip + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
