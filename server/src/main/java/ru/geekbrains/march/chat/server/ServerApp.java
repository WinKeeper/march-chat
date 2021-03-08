package ru.geekbrains.march.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Server start at 8189 port. Awaiting for client...");
            //Слушает порт 8189
            Socket socket = serverSocket.accept();
            //Завернули потоки в Data
            //Оборачиваем поток в Data - расширяет функционал (обработчики потоков)
            //in - входящий поток
            DataInputStream in = new DataInputStream(socket.getInputStream());
            //out - исходящий поток
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Client now connected to server");
            int countMsg = 0;
            //Ожидаем ввода информации (биты) в InputStream
            while (true) {
                //Считываем вход от КЛИЕНТА
                String msg = in.readUTF();
                switch (msg) {
                    case "/stat":
                        out.writeUTF("Количество сообщений от клиента - " + countMsg);
                        break;
                    case "/exit":
                        //Отправляем сообщение обратно клиенту
                        out.writeUTF("Соединение прервано со стороны клиента.");
                        out.writeUTF("/exit");
                        socket.close();
                        break;
                    default:
                        System.out.println(msg);
                        out.writeUTF("ECHO: " + msg);
                        countMsg++;
                }


//                if (msg.equalsIgnoreCase("/stat")) {
//                    out.writeUTF("Количество сообщений от клиента - " + countMsg);
//                } else if(msg.equalsIgnoreCase("/exit")) {
//                    socket.close();
//                } else {
//                    System.out.println(msg);
//                    out.writeUTF("ECHO: " + msg);
//                    countMsg++;
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

//Черновик
//int x;
////Ожидаем ввода информации (биты) в InputStream
//            while ((x = socket.getInputStream().read()) != -1) {
//                    //Преобразуем в символы
//                    System.out.print((char) x);
//                    //отправили обратно байт - положили в трубу Оутпут
//                    socket.getOutputStream().write(x);
//                    }
//                    } catch (IOException e) {
//                    e.printStackTrace();
//                    }
