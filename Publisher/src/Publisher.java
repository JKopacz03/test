import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Publisher {
    private static BlockingQueue<Socket> consumers = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        try {
            Thread messageThread = new Thread(() -> {
                OutputStream outputStream;
                Scanner scanner = new Scanner(System.in);

                System.out.println("Waiting for customers");
                while (true) {
                    if(!consumers.isEmpty()) {
                        System.out.print("Enter message to publish: ");
                        String message = scanner.nextLine();

                        for (Socket consumerSocket : consumers) {
                            try {
                                outputStream = consumerSocket.getOutputStream();
                                outputStream.write(message.getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                System.err.println("Consumer disconnected");
                                consumers.remove(consumerSocket);
                            }
                        }
                    }
                }

            });
            messageThread.start();

            List<Integer> customersPorts = List.of(9996, 9997, 9998, 9999);

            while (true) {
                for (Integer port : customersPorts) {
                    boolean existPort = consumers.stream().anyMatch(e -> e.getPort() == port);
                    try {
                        if(!existPort){
                            Socket socket = new Socket("localhost", port);
                            System.out.println("Connected to consumer on port " + port);
                            consumers.offer(socket);
                        }
                    } catch (IOException e) {
                        Thread.sleep(1000);
                    }
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}