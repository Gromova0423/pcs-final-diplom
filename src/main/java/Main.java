import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(8989);) { // стартуем сервер один(!) раз
            while (true) {// в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();//принимает входящее соединение от клиента и создает новый сокет (socket), который будет использоваться для обмена данными с этим клиентом
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//BufferedReader будет использовать входной поток данных из сокета для чтения информации, переданной от клиента
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());//PrintWriter будет использоваться для отправки данных обратно клиенту через сокет
                ) {
                    String word = reader.readLine();// на входной поток подается слово word
                    List<PageEntry> response = engine.search(word); // ответ response - результат вызова метода search класса BooleanSearchEngine
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response);
                    writer.println(jsonResponse);
                    writer.flush();


                } catch (IOException e) {
                    System.out.println("Ошибка при обработке запроса");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }


    }
}


