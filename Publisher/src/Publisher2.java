import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Publisher2 {
    private static BlockingQueue<Socket> consumers = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9998);
            System.out.println("Publisher 2 started. Waiting for consumers");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to consumer");
                consumers.offer(socket);

                Thread messageThread = new Thread(() -> {
                    try {
                        OutputStream outputStream;
                        Scanner scanner = new Scanner(System.in);

                        System.out.print("Enter message to publish: ");
                        String message = scanner.nextLine();

                        for (Socket consumerSocket : consumers) {
                            outputStream = consumerSocket.getOutputStream();
                            outputStream.write(message.getBytes());
                            outputStream.flush();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                messageThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}