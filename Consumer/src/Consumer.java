import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Consumer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9998);
            System.out.println("Consumer started. Waiting for publishers");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to publisher");

                Thread publisherThread = new Thread(new PublisherHandler(socket));
                publisherThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}