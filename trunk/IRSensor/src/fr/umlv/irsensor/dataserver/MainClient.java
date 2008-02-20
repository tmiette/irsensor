package fr.umlv.irsensor.dataserver;

public class MainClient {
	public static void main(String[] args) {
		DataServerClient client = new DataServerClient("localhost", 31002);
		client.connect();
	}
}
