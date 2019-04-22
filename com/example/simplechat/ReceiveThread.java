package com.example.simplechat;

import java.io.*;
import java.net.Socket;

import com.example.simplechat.models.ChatMessage;

public class ReceiveThread extends Thread{

	private Socket mSocket;
	public String userId;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	
	@Override
	public void run() {
		super.run();

		try {
			
			while(ois != null) {
				ChatMessage msg = (ChatMessage)ois.readObject();

				if(ois != null){
					
					String groupType = msg.getGroupType();
					String senderId = msg.getSenderId();
					String senderName = msg.getSenderName();
					String receiverId = msg.getReceiverId();
					String receiverName = msg.getReceiverName();
					String message = msg.getMessage();

					if(groupType.equals("p2p")){ //client sends Message

						if(MainServer.threadMap.containsKey(receiverId)){ //check if the receiver's socket is currently connected
							ReceiveThread thread = MainServer.threadMap.get(receiverId);

							Socket oSocket = thread.mSocket;
							Boolean isClosed = oSocket.isClosed();//if a socket is closed it returns TRUE
							System.out.println("friend closed?: " + isClosed);

							System.out.println(userId + " => " + receiverId + ": " + message);


							if(!isClosed){ //if the socket is still open

								if(!oSocket.isClosed()){
									thread.oos.writeObject(msg); //write to friend's client
									thread.oos.flush();
								}else{
									oos.writeObject(new ChatMessage("noti", "", "", "", "", "not online"));
									oos.flush();
									System.out.println(userId + " => " + receiverId + "(not online): " + message);
								}
								

							
							}else if(isClosed == true || thread == null){ //if the socket is closed
								oos.writeObject(new ChatMessage("noti", "", "", "", "", "not registered"));
								oos.flush();
								System.out.println(userId + " => " + receiverId + "(not online): " + message);
							}

						
						}else{//if the user(toName) is never logged in before
							oos.writeObject(new ChatMessage("noti", "", "", "", "", "unknown user"));
							oos.flush();
							System.out.println(userId + " => " + receiverId + "(not online): " + message);
						}

					
					}else if(groupType.equals("groupMsg")){ //group message
						String[] memberIdArr = receiverId.split("[,]");
						String[] memberNameArr = receiverName.split("[,]");

						System.out.println(userId + " => " + " Group : " +  message);

						for(int i = 0; i < memberIdArr.length; i ++){
							String memberId = memberIdArr[i];
							String memberName = memberNameArr[i];

							if(MainServer.threadMap.containsKey(memberId)){

								ReceiveThread thread = MainServer.threadMap.get(memberId);
								Socket oSocket = thread.mSocket;
								Boolean isClosed = oSocket.isClosed();//if a socket is closed it returns TRUE
								
								if(!isClosed){ //write to friend's client if socket is still open 
									if(!oSocket.isClosed()){
										
										thread.oos.writeObject(new ChatMessage("groupMsg", senderId, senderName, memberId, memberName, message));
										thread.oos.flush();

									} else {
										System.out.println("fail: " + memberId + " is not online.. ");
									}
								} else {
									System.out.println("fail: " + memberId + " is not online.. ");
								}
							}else {
								System.out.println("fail: " + memberId + " is not online.. ");
							}
						}//for..
					}else if(groupType.equals("sign out")){ //client wants to logout
						oos.writeObject(new ChatMessage("sign out", "", "", "", "", "success"));
						oos.flush();
						mSocket.close();
						break;
						//escape the loop

					
				} 

				}else {
					System.out.println("input mesesage was null");
					break;
				}

			}//while..

			//Print text below if the loop is over
			System.out.println(userId + " is signed out (ReceiveThread is terminated.)");

			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("WHERE: " + userId + "'s thread");
		}
		
	}//run...





	public void setSocket(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
		this.mSocket = socket;
		this.oos = oos;
		this.ois = ois;
				
	}
}
