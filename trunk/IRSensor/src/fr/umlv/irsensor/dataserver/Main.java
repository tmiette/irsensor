
package fr.umlv.irsensor.dataserver;



public class Main {
	public static void main(String[] args) {
		
		DataServerServer server = new DataServerServer(31002);
		try  {
			server.listen();
		}
		finally {
			server.close();
		}
	}
}
