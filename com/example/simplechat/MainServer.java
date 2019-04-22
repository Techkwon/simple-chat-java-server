package com.example.simplechat;

import java.io.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.simplechat.models.ChatMessage;

public class MainServer {

	ArrayList<ReceiveThread> connectList;
	public static HashMap<String, ReceiveThread> threadMap;
    public static String userName=""; 
    Socket testSocket;
	
	public MainServer() {
		connectList = new ArrayList<>();
		threadMap = new HashMap<>();				

		
		try {
			ServerSocket serverSocket = new ServerSocket(8080);
			System.out.println("\nWaiting for connection..!");

			while(true) {

				//waiting for socket connection
				Socket socket = serverSocket.accept();
				//create a new OpenThread object when there is a new connection
				OpenThread openThread = new OpenThread(socket);
				//start the thread object
				openThread.start();
				
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public class OpenThread extends Thread{
		private Socket mSocket;
	    private ObjectOutputStream oos;
	    private ObjectInputStream ois;

	    
		
		public OpenThread(Socket mSocket) {
			this.mSocket = mSocket;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			try {
				// in = new DataInputStream(mSocket.getInputStream());
				// reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
				// writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
				oos = new ObjectOutputStream(mSocket.getOutputStream());
				ois = new ObjectInputStream(mSocket.getInputStream());
				
				while(ois != null) {
					
					ChatMessage msg = (ChatMessage)ois.readObject();
					ReceiveThread rThread;
					
					String groupType = msg.getGroupType();

			        if(groupType.equals("sign in")){
//			        	String name = msgArr[1];
			        	String userId = msg.getSenderId();

			        	if(!userId.equals("null")){
			        		rThread = new ReceiveThread();
				        	threadMap.put(userId, rThread);
							
					        System.out.println("\n" + userId + " logged on"); //show which user logged in
					        System.out.println(threadMap.size() + " users are currently logged on");
					        	
				        	//give unique user Name to the thread
				        	rThread.userId = userId;
				        	//connect Socket
				        	rThread.setSocket(mSocket, oos, ois);
							//start the Thread
							rThread.start();

//							writer.write("login| | |success\n");
//							writer.flush();
							oos.writeObject(new ChatMessage("sign in", "", "", "", "", "success"));
							oos.flush();
							break;

			        	}else{
				        	System.out.println("There was inappropriate login trial..");
			        		break;
				        }
			        }else{
			        	System.out.println("interruption: " + msg);
			        	break;
			        }
				}//while..
				
			}catch(Exception e) {
				// e.printStackTrace();
				System.out.println("비정상 접속: " + e.getMessage());
				e.printStackTrace();
			}
		}//run..
		
	} //openThread..
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainServer();
	}
}
