package de.deadlocker8.budgetmasterserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server
{
	public static void main(String[] args) throws Exception
	{
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/", new MyHandler());
		server.setExecutor(null);
		server.start();
	}

	static class MyHandler implements HttpHandler
	{
		public void handle(HttpExchange t) throws IOException
		{
			switch(t.getRequestMethod())
			{
				case "GET":
					System.out.println("GET");
					break;

				case "PUT":
					System.out.println("PUT");
					InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
					BufferedReader br = new BufferedReader(isr);

					int b;
					StringBuilder buf = new StringBuilder(512);
					while((b = br.read()) != -1)
					{
						buf.append((char)b);
					}

					br.close();
					isr.close();
					System.out.println(buf);
					break;

				default:
					System.out.println("unrecognized: " + t.getRequestMethod());
					break;
			}

			String response = "Test Page";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}