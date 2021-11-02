
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Scanner;

import javax.swing.JTextArea;

public class UDPCliente {

	private static final String endereco = "localhost";
	private DatagramSocket clientSocket;
	private int porta;
	private int contador = 0;

	public UDPCliente(JTextArea textArea) throws UnknownHostException, IOException{
		byte [] enviarDados;
		InetAddress ipServidor = InetAddress.getByName(endereco);
		clientSocket = new DatagramSocket();
		enviarDados = (""+ clientSocket.getPort()).getBytes();
		DatagramPacket enviarPacote = new DatagramPacket(enviarDados, enviarDados.length, ipServidor,28902);
		clientSocket.send(enviarPacote);

		byte [] receberDados =  new byte [1024];
		DatagramPacket receberPacote = new DatagramPacket(receberDados, receberDados.length);
		clientSocket.receive(receberPacote);
		String mensagem = new String (receberDados, "UTF-8").trim();
		System.out.println("Porta " + mensagem + " recebida");
		this.porta = Integer.valueOf(mensagem);
		ReceberDados receberThread = new ReceberDados(clientSocket,this.porta, textArea);
		receberThread.start();
	}

	public String enviarDados(String mensagem) throws IOException{
		byte [] enviarDados;
		InetAddress ipServidor = InetAddress.getByName(endereco);
		Timestamp hora = new Timestamp (System.currentTimeMillis());
		String tempo = hora.toString();
		mensagem = (tempo + ": " + mensagem);
		enviarDados = mensagem.getBytes();
		DatagramPacket enviarPacote = new DatagramPacket(enviarDados, enviarDados.length, ipServidor,this.porta);
		this.clientSocket.send(enviarPacote);
		this.contador++;
		return "["+ contador +"] You: " + mensagem;
	}
}

class EnviarDados extends Thread {
	DatagramSocket clientSocket;
	int porta;
	public EnviarDados(DatagramSocket clientSocket, int porta) {
		this.clientSocket = clientSocket;
		this.porta = porta;
	}
	@Override
	public void run() {
		Scanner in = new Scanner(System.in);
		byte [] enviarDados;
		String mensagem;
		String endereco = "localhost";
		try {
			InetAddress ipServidor = InetAddress.getByName(endereco);

			while (true) {
				enviarDados = null;
				mensagem = in.nextLine();
				Timestamp hora = new Timestamp (System.currentTimeMillis());
				String tempo = hora.toString();
				enviarDados = (tempo + " " + mensagem).getBytes();
				DatagramPacket enviarPacote = new DatagramPacket(enviarDados, enviarDados.length, ipServidor,this.porta);
				this.clientSocket.send(enviarPacote);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ReceberDados extends Thread {
	DatagramSocket clientSocket;
	int porta;
	JTextArea textArea;

	public ReceberDados(DatagramSocket clientSocket, int porta, JTextArea textArea) {
		this.clientSocket = clientSocket;
		this.porta = porta;
		this.textArea = textArea;
	}

	@Override
	public void run() {
		try {
			String endereco = "localhost";
			InetAddress ipServidor = InetAddress.getByName(endereco);
			int contador = 1;
			byte [] receberDados;
			byte [] confirmar = "Mensagem enviada com sucesso!".getBytes();
			while(true) {

				receberDados =  new byte [1024];
				DatagramPacket receberPacote = new DatagramPacket(receberDados, receberDados.length);
				this.clientSocket.receive(receberPacote);
				String mensagem = new String (receberDados, "UTF-8").trim();
				if(!mensagem.contentEquals("Mensagem enviada com sucesso!")) {
					mensagem = "[" + contador + "] Friend: " + mensagem;
					this.textArea.append(mensagem + "\n");
					DatagramPacket confirmacao = new DatagramPacket(confirmar, confirmar.length, ipServidor, this.porta);
					clientSocket.send(confirmacao);
					contador++;
				}
				else {
					System.out.println(mensagem);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}