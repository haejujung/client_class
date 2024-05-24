package ch05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// 2- 1 상속을 활용한 구현 클래스 설계 하기
public class MyClient extends AbstrcatClient {

	@Override
	protected void setupClient() throws IOException {

		Socket socket = new Socket("192.168.0.46", 5000);

	}

	public static void main(String[] args) {

		MyClient myClient = new MyClient();
		myClient.run();

	}

}