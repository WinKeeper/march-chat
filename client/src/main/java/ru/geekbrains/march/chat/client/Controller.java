package ru.geekbrains.march.chat.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextField msgField;

    @FXML
    TextArea msgArea;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Подключение к порту
            socket = new Socket("localhost", 8189);
            //Создаём обёртку для потоков для функционала
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            //Два цикла не могут работать одновременно - используем потоки
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        switch (msg) {
                            case "/exit":
                                msgArea.appendText("Соединение прервано со стороны сервера." + "\n");
                                socket.close();
                                break;
                            default:
                                msgArea.appendText(msg + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();

        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to [ localhost:8189 ]");
        }

    }


    public void sendMsg() {
        try {
            if (!msgField.getText().isEmpty()) {
                out.writeUTF(msgField.getText());
                msgField.clear();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to send message", ButtonType.OK);
            alert.showAndWait();
        }

    }


}
