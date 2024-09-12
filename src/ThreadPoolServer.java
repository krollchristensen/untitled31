import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ThreadPoolServer {
    private static final int PORT = 1234;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException {
        // Opretter en thread pool med 10 tråde
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server lytter på port " + PORT);

            while (true) {
                // Accepterer klientforbindelse
                Socket clientSocket = serverSocket.accept();
                System.out.println("Ny klient forbundet: " + clientSocket.getInetAddress().getHostAddress());

                // Håndterer klienten i en separat tråd via thread poolen
                executor.submit(new ClientHandler(clientSocket));
            }
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {

        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Modtaget fra klient: " + inputLine);
                out.println("Server svarer: " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl i forbindelse med klient: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
