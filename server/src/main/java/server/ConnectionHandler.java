package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConnectionHandler implements Runnable {
	private Socket connection;
	private File directory;
	private ServerSocket listener;

	// public ConnectionHandler(Socket connection, File directory) {
	public ConnectionHandler(ServerSocket listener, File directory) {

		// this.connection = connection;
		this.directory = directory;
		this.listener = listener;

		try {
			this.connection = this.listener.accept();
		} catch (IOException e) {
			System.out.println("Error is thrown from ConnectionHandler");
			e.printStackTrace();
		}

	}

	private void handleRequest() {

	}

	@Override
	public void run() {

		try {

			String request = null;

			while (!"quit".equals(request)) {

				if (connection.isClosed()) {
					this.connection = this.listener.accept();
				}
				request = getRequest(connection);

				System.out.println("Runinng command: " + request);

				if ("index".equals(request)) {

					List<String> filenames = list(directory);
					StringBuilder sb = new StringBuilder();

					for (String filename : filenames) {
						sb.append(filename).append("\n");
					}
					sendResponse(connection, sb.toString());

				} else if ("unknown".equals(request)) {
					sendResponse(connection, "unknown command");
				} else if (null == request || request.equals("")) {
					connection.close();
				} else {
					File file = new File(directory, request);
					if (!file.exists() || file.isDirectory()) {
						sendResponse(connection, "error: File does not exist on the server.");
						return;
					}
					sendResponse(connection, getFileContent(file));
				}
			}

		} catch (

		IOException e) {
			System.out.println("Error thrown from run()");
			e.printStackTrace();
		}

	}

	private String getFileContent(File file) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));

		StringBuilder sb = new StringBuilder();
		sb.append("ok").append("\n");

		List<String> lines = reader.lines().collect(Collectors.toList());

		for (String line : lines) {
			sb.append(line).append("\n");
		}

		return sb.toString();
	}

	private List<String> list(File directory) {

		return Arrays.stream(Objects.requireNonNull(directory.listFiles())).map(this::getName)
				.collect(Collectors.toList());
	}

	private String getName(File file) {
		String name = file.getName();
		return file.isDirectory() ? name + "/" : name;
	}

	private void sendResponse(Socket connection, String payload) throws IOException {

		PrintWriter responseWriter = new PrintWriter(connection.getOutputStream(), true);
		responseWriter.println(payload);
		connection.close();
	}

	private String getRequest(Socket connection) throws IOException {
		InputStream requestInputStream = connection.getInputStream();
		InputStreamReader requestInputStreamReader = new InputStreamReader(requestInputStream);
		BufferedReader requestReader = new BufferedReader(requestInputStreamReader);

		return requestReader.readLine();
	}
}
