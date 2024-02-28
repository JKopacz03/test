import java.io.*;
import java.net.*;

public class Consumer {
    public static void main(String[] args) throws IOException {
        try {
            Socket socket = new Socket("localhost", 9998);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("CONSUMER_CONNECTION");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = in.readLine();
                if (message != null && message.equals("PING")) {
                    out.println("PONG");
                } else {
                    System.out.println(message);
                }
            }
        }
        catch (IOException e) {
                System.err.println("Server is off");
        }
    }
}