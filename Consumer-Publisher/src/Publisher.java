import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Publisher {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9998);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("PUBLISHER_CONNECTION");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                    System.out.print("Enter message to publish: ");
                    String message = scanner.nextLine();
                    out.println(message);
            }
        }
        catch (IOException e) {
            System.err.println("Server is off");
        }

    }
}