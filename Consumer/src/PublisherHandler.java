import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class PublisherHandler implements Runnable {
    private Socket socket;
    public PublisherHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while (true) {
                try {
                    bytesRead = inputStream.read(buffer);
                    if (bytesRead == -1) {
                        System.out.println("Publisher disconnected");
                        break;
                    }
                    String message = new String(buffer, 0, bytesRead);
                    System.out.println("Received from publisher: " + message);
                } catch (IOException e) {
                    System.err.println("Publisher disconnected");
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
