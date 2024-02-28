import exceptions.IllegalConnectionException;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

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
                            while (true) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                String receivedMess;
                                StringBuilder accumulatedMessages = new StringBuilder();
                                while ((receivedMess = reader.readLine()) != null) {
                                    if (consumers.isEmpty()) {
                                        Socket consumerSocket = consumers.take();
                                        consumers.offer(consumerSocket);
                                        accumulatedMessages.append(receivedMess + "\n");
                                        while (reader.ready() && (receivedMess = reader.readLine()) != null) {
                                            accumulatedMessages.append(receivedMess).append("\n");
                                        }
                                        PrintWriter out = outputMap.get(consumerSocket);
                                        out.println(accumulatedMessages);
                                        accumulatedMessages.setLength(0);
                                        break;
                                    } else {
                                        for (Socket consumerSocket : consumers) {
                                            PrintWriter out = outputMap.get(consumerSocket);
                                            out.println("PING");
                                            consumerSocket.setSoTimeout(1000);
                                            try {
                                                BufferedReader responseReader = new BufferedReader(new InputStreamReader(consumerSocket.getInputStream()));
                                                String response = responseReader.readLine();
                                                if (response != null && response.equals("PONG")) {
                                                    out.println(receivedMess);
                                                } else {
                                                    consumers.remove(consumerSocket);
                                                    outputMap.remove(consumerSocket);
                                                    accumulatedMessages.append(receivedMess + "\n");
                                                }
                                            }
                                            catch (IOException e) {
                                                consumers.remove(consumerSocket);
                                                outputMap.remove(consumerSocket);
                                                accumulatedMessages.append(receivedMess + "\n");
                                            }
                                        }
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
