import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] Args) {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        //can also use Scanner instead of BufferedReader
        String hostname, portNum;
        int port;
        Socket clientSocket;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String request, response;

        try {
            System.out.println("Enter the server's hostname: ");
            hostname = inputReader.readLine();
            System.out.println("Enter the server's port number: ");
            portNum = inputReader.readLine();
            port = Integer.parseInt(portNum);

            clientSocket = new Socket(hostname, port);
            System.out.println("\nClient-side socket has been initialized!");

            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Enter your message to the server!");
            request = inputReader.readLine();
            while (request != null) {
                try {
                    bw.write(request + "\n");
                    bw.flush();
                    response = br.readLine();
                    System.out.println("Server says " + response);
                    request = inputReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
