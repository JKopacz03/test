import exceptions.IllegalConnectionException;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9998);
        BlockingQueue<Socket> consumers = new LinkedBlockingQueue<>();
        Map<Socket, PrintWriter> outputMap = new ConcurrentHashMap<>();

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
                                Socket newe = consumers.take();
                                consumers.offer(newe);
                                for (Socket consumerSocket : consumers) {
                                    if (consumerSocket.isConnected()) {
                                        PrintWriter out = outputMap.get(consumerSocket);
                                        out.println(receivedMess);
                                    } else {
                                        consumers.remove(consumerSocket);
                                        outputMap.remove(consumerSocket);
                                    }
                                }
                            }
                        } catch (IOException | InterruptedException e) {
                            System.err.println("Publisher disconnected");
                        }
                    }).start();
                } else if (message.equals("CONSUMER_CONNECTION")) {
                    System.out.println("Consumer connected from port: " + socket.getPort());
                    outputMap.put(socket, new PrintWriter(socket.getOutputStream(), true));
                    consumers.offer(socket);
                } else {
                    throw new IllegalConnectionException("Unknown client connected from port: " + socket.getPort());
                }
            }
        }
    }
}
