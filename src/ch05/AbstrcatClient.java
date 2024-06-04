package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstrcatClient {

	private ServerSocket serverSocket;
	private Socket socket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
	private BufferedReader keyboardReader;

	protected void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	protected ServerSocket getServerSocket() {
		return serverSocket;
	}

	public final void run() {

		try {
			setupClient();
			setupStream();
			startService();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void setupClient() throws IOException;


	private void setupStream() throws IOException {
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));

	}

	private void startService() {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();

		readThread.start();
		writeThread.start();

		try {
			readThread.join();
			writeThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("clean up 호출 확인");
			cleanup();
		}
	}

	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = socketReader.readLine()) != null) {
					System.out.println("서버에서온 메시지 : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					socketWriter.println(msg);
					socketWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
