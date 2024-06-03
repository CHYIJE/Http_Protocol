package ch01;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {

	public static void main(String[] args) {
		// 8080 <- https,80 <-- http (포트번호 생략 가능하다)
		try {
			// 포트 번호 8080으로 HTTP 서버 생성
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

			// 서버에 대한 설정

			// 프로토콜 정의 (경로, 핸들러 처리)
			// 핸들러 처리를 내부 정적 클래스로 사용
			httpServer.createContext("/test", new MyTestHandler());
			httpServer.createContext("/hello", new HelloHandler());

			// 서버 시작
			httpServer.start();
			System.out.println(">> My Http Server started on port 8080");

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end of main

	// http://localhost:8080/test <- 주소 설계
	static class MyTestHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			// 사용자의 요청 방식(METHOD) , GET, POST 알아야 우리가 동작 시킬 수 있다.
			String method = exchange.getRequestMethod();
			System.out.println("method : " + method);

			if ("GET".equalsIgnoreCase(method)) {
				// GET 이라면 여기서 동작
//				System.out.println("여기는 GET이 만든 세상이야!");

				// GET -> path: /test 라고 들어오면 어떤 응답 처리를 내려주면 된다.
				handleGetRequest(exchange);

			} else if ("POST".equalsIgnoreCase(method)) {
				// POST 요청시 여기서 동작
//				System.out.println("여기는 POST 방식으로 만든 세상이야!");
				handlePostRequest(exchange);
			}
		}

		// GET 요청시 동작 만들기
		private void handleGetRequest(HttpExchange exchange) throws IOException {
			String response = """
					<!DOCTYPE html>
					<html lang = ko>
						<head></head>
						<body>
							<h1 style = "background-color:red">Hello path by /test</h1>
						</body>
					</html>
				""";
			//			String response = "hello Get"; // 응답 메세지
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		// POST 요청시 동작 만들기
		private void handlePostRequest(HttpExchange exchange) throws IOException {
			// POST 요청은 HTTP 메세지에 바디 영역이 존재한다.
			String response = """
						<!DOCTYPE html>
						<html lang = ko>
							<head></head>
							<body>
								<h1 style = "background-color:red">Hello path by /test</h1>
							</body>
						</html>
					""";
			//HTTP 응답 메세지 헤더 설정
			exchange.setAttribute("Content-Type", "text/html; charset=UTF-8" );
			exchange.sendResponseHeaders(200, response.length());
			
			// getResponseBody
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.flush();
			os.close();
		}

	} // end of MyTestHandler

	static class HelloHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			System.out.println("hello method : " + method);

		}

	} // end of HelloHandler
} // end of class