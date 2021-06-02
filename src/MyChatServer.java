import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyChatServer {


    private ArrayList<ClientWorker> connectedClients = new ArrayList<>();

    private ServerSocket serverSocket;

    MyChatServer() throws IOException {
        serverSocket = new ServerSocket(8888);

    }

    public void run() {
        System.out.println("Now waiting for incoming clients...");
        while (true) {


            try {
                // принимаем подключение
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                // подключаем
                connectedClients.add(new ClientWorker(socket, this));


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    void broadcast(String content, ClientWorker sender)
    {
        for (ClientWorker client : connectedClients)
        {
            if (client != sender) {
                client.receiveMessage(content,sender);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new MyChatServer().run();

    }
}
