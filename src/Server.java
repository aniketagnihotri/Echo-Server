import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server {

	private ServerSocket serverSocket;

	public Server() throws IOException {
		this.serverSocket = new ServerSocket(0);
	}

	public void serveClients() {
		Socket clientSocket;
		CountdownRequestHandler handler;
		Thread handlerThread;
		int clientCounter = 0;

		System.out.println("The server has started and is serving clients on port " + this.serverSocket.getLocalPort() + ".");

		while (true) {
			try {
				clientSocket = this.serverSocket.accept();
				clientCounter++;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			handler = new CountdownRequestHandler(clientSocket, clientCounter);
			handlerThread = new Thread(handler);
			handlerThread.start();
			System.out.println("Client #" + clientCounter + " has connected.");

		}
	}

	public static void main(String[] Args) {
		Server newServer;
		try {
			newServer = new Server();
			newServer.serveClients();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server failed to start!");
			return;
		}

	}
}

class CountdownRequestHandler implements Runnable {

	private Socket clientSocket;
	private int connectionNumber;

	public CountdownRequestHandler(Socket clientSocket, int connectionNumber) {
		Objects.requireNonNull(clientSocket, "The provided client socket is NULL.");

		this.clientSocket = clientSocket;
		this.connectionNumber = connectionNumber;

	}

	public void run() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		 } catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				String inputFeed = br.readLine();
				try {
					if (inputFeed.equalsIgnoreCase("close")) {
						bw.write("goodbye!\n");
						bw.flush();
						break;
					}
					bw.write(inputFeed + "\n");
					bw.flush();
				} catch (IOException e) {
					bw.write("an error occurred!");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client #" + connectionNumber + " has disconnected.");
	}

}