import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Consumer2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9998);
            System.out.println("Connected to publisher.");

            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            String message = new String(buffer, 0, bytesRead);
            System.out.println("Received: " + message);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}