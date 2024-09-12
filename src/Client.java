import java.io.*;
import java.net.*;

public class Client {
    public static final String HOST = "localhost";
    public static final int PORT = 1234;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new ClientTask(i)).start();
        }
    }
}

class ClientTask implements Runnable {
    private final int clientId;

    public ClientTask(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(Client.HOST, Client.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            String message = "Hej fra klient " + clientId;
            out.println(message);
            System.out.println("Modtaget fra server: " + in.readLine());

            // Afslutter klienten
            out.println("bye");
        } catch (IOException e) {
            System.out.println("Fejl for klient " + clientId + ": " + e.getMessage());
        }
    }
}
