package ch01;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DoitHttpServer {

	public static void main(String[] args) {
		try {
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

			httpServer.createContext("/now", new nowHandler());

			httpServer.start();
			System.out.println(" >> HTTP now Server Start");

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end of main

	static class nowHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			System.out.println("method : " + method);

			if ("GET".equalsIgnoreCase(method)) {
				System.out.println("get 확인");
				handleGetRequest(exchange);

			} else if ("POST".equalsIgnoreCase(method)) {
				System.out.println("post 확인");
				handlePostRequest(exchange);
			}
		}

		private void handleGetRequest(HttpExchange exchange) throws IOException {
			String response = """
						<!DOCTYPE html>
						<html lang = ko>
							<head>
								<meta charset="UTF-8">
								<meta name="viewport" content="width=device-width, initial-scale=1.0">
							</head>
							<body>
								<h1 style = "background-color:blue">허니 오 허니 너무 보고싶어어억!</h1>
							</body>
						</html>
					""";
			exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
			exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody(),"UTF-8"));
		
			bw.write(response);
			bw.flush();
			bw.close();
		}

		private void handlePostRequest(HttpExchange exchange) throws IOException {
			String response = """
						<!DOCTYPE html>
						<html lang = ko>
							<head>
								<meta charset="UTF-8">
								<meta name="viewport" content="width=device-width, initial-scale=1.0">
							</head>
							<body>
								<h1 style = "background-color:blue">이것은 post라는 것이다</h1>
							</body>
						</html>
					""";

			exchange.setAttribute("Content-Type", "text/html; charset=UTF-8");
			exchange.sendResponseHeaders(200, response.length());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody()));
			bw.write(response);
			bw.flush();
			bw.close();

		}

	} // end of nowHandler

}
