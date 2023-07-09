import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(engine.search("бизнес"));


        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате


        try (ServerSocket serverSocket = new ServerSocket(8989);) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept(); //принимает входящее соединение от клиента и создает новый сокет (socket), который будет использоваться для обмена данными с этим клиентом
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //BufferedReader будет использовать входной поток данных из сокета для чтения информации, переданной от клиента
                        PrintWriter out = new PrintWriter(socket.getOutputStream()); //PrintWriter будет использоваться для отправки данных обратно клиенту через сокет
                ) {
                    BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
                    String word = in.readLine();// на входной поток подается слово word
                    List<PageEntry> response = engine.search(word); // ответ response - результат вызова метода search класса BooleanSearchEngine
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response);
                    out.println(jsonResponse);
                    out.flush();

                    socket.close();
                } catch (IOException e){
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