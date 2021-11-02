

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServidor {
	public static void main(String[] args) throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(28902);
		byte [] receberDados;
		byte [] enviarDados;
		InetAddress ipCliente1, ipCliente2;
		
		while(true) {
			int firstPort;
			int secondPort;
			receberDados = new byte [1024];
			enviarDados = null;
			
			DatagramPacket receberPacote1 = new DatagramPacket(receberDados, receberDados.length);
			serverSocket.receive(receberPacote1);
			System.out.println("Porta recebida");
			ipCliente1 = receberPacote1.getAddress();
			firstPort = receberPacote1.getPort();
			
			DatagramPacket receberPacote2 = new DatagramPacket(receberDados, receberDados.length);
			serverSocket.receive(receberPacote2);
			System.out.println("Porta recebida");
			ipCliente2 = receberPacote2.getAddress();
			secondPort = receberPacote2.getPort();
			
			enviarDados = ("" + firstPort).getBytes();
			DatagramPacket enviarPacotes = new DatagramPacket(enviarDados, enviarDados.length, ipCliente2, secondPort);
			serverSocket.send(enviarPacotes);
			
			enviarDados = null;
			enviarDados = ("" + secondPort).getBytes();
			enviarPacotes = new DatagramPacket(enviarDados, enviarDados.length, ipCliente1, firstPort);
			serverSocket.send(enviarPacotes);
		}
	}
}
