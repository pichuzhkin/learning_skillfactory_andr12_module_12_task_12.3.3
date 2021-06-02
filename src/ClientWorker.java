import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class ClientWorker implements Runnable {
    Socket socket;
    MyChatServer serverRef;
    String clientName;

    String getClientName() {
        return this.clientName;
    }

    public ClientWorker(Socket socket, MyChatServer serverRef) {

        this.socket = socket;
        this.serverRef = serverRef;
        new Thread(this).start();
    }


    Scanner in;
    PrintStream out;


    public void run() {
        try {

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            in = new Scanner(is);
            out = new PrintStream(os);


            int port = socket.getPort();

            // читаем из сети и пишем в сеть
            out.println("You are now connected to the chat");
            out.print("Your name, please: ");
            String input = in.nextLine();

            if (input == null || input.isEmpty()) {
                this.clientName = "Mysterious" + port + "Anonymous";
            } else {
                this.clientName = input;

            }
            serverRef.broadcast(" I am from port " + port + " and now I am with you", this);
            while (input != null && !input.equals("bye")) {
                out.print("Your message: ");
                input = in.nextLine();
                serverRef.broadcast(input, this);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void receiveMessage(String content, ClientWorker sender) {
        out.println("[" + sender.getClientName() + "]: " + content);
    }
}