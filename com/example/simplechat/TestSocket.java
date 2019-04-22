package com.example.simplechat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.simplechat.models.ChatMessage;

public class TestSocket {

	static Socket socket;
	static ObjectInputStream ois;
    static ObjectOutputStream oos;
	
	public static void main(String[] args) {

		new Thread(() -> {
			try {
				socket = new Socket("13.209.19.16", 8080);
				System.out.println("socket connected!!");
				oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(new ChatMessage("sign in", "BLUE", "øÌ¿œ", "RED", "πŒ¡§", "æ»≥Á"));
                System.out.println("message sent");
				
                ChatMessage msg = (ChatMessage)ois.readObject();
                System.out.println("message received:" + msg.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
	}

}
