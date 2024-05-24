package ch05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiThreadClient {

	public static void main(String[] args) {

		System.out.println(" !!! 클라이언트 시작함 !!!");

		try (Socket socket = new Socket("localhost", 5000)) {
			System.out.println(" 서버와 연결되었습니다! ");

			// 서버와 통신을 위한 스트림 초기화
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			startReadThread(bufferedReader);
			startWriteThread(printWriter, keyboardReader);
			// 메인 스레드 기다림 , 가독성이 떨어짐
			// startWriteThread() <-- 내부에 있음
			
//			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
//			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// 1. 클라이언트로부터 데이터를 읽는 스레드 시작 메서드 생성

	private static void startReadThread(BufferedReader bufferedReader) {
		Thread readThread = new Thread(() -> {
			try {
				String serverMesage;
				while ((serverMesage = bufferedReader.readLine()) != null) {
					System.out.println(" 서버에서 온 메시지 : " + serverMesage);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start();

	}

	// 2. 키보드에서 입력을 받아 클라이언트 측으로 데이터를 전송하는 스레드
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String clientMesage;
				while ((clientMesage = keyboardReader.readLine()) != null) {
					printWriter.println(clientMesage);
					printWriter.flush();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		writeThread.start();

		try {
			// 메인 스레드 기다림
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

} // end of class
