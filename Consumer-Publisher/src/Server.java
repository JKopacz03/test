import exceptions.IllegalConnectionException;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9998);
        BlockingQueue<Socket> consumers = new LinkedBlockingQueue<>();

        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = in.readLine();
            if (message != null) {
                if (message.equals("PUBLISHER_CONNECTION")) {
                    System.out.println("Publisher connected from port: " + socket.getPort());
                    new Thread(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            String receivedMess;
                            while ((receivedMess = reader.readLine()) != null) {
                                while (consumers.isEmpty()) {
                                    Thread.sleep(1000);
                                }
                                for (Socket consumerSocket : consumers) {
                                        PrintWriter out = new PrintWriter(consumerSocket.getOutputStream(), true);
                                        out.println(receivedMess);
                                }
                            }
                        } catch (IOException | InterruptedException e) {
                            System.err.println("Publisher disconnected");
                        }
                    }).start();
                } else if (message.equals("CONSUMER_CONNECTION")) {
                    System.out.println("Consumer connected from port: " + socket.getPort());
                    consumers.offer(socket);
                } else {
                    throw new IllegalConnectionException("Unknown client connected from port: " + socket.getPort());
                }
            }
        }
    }
}
