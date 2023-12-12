package com.rubenmadsen.TjackServer;

import java.util.Scanner;

/**
* <h1>AppStart</h1>
*
* @author  Ruben Madsen
* @version 1.0
* @since   2024-12-06
*/
public class AppStart {

	public static void main(String[] args) throws InterruptedException {
		final int port = 4231;
		ChessServer chessServer = new ChessServer(port);
		Scanner scanner = new Scanner(System.in);
		String read = "";
		while (!read.equals("exit")){
			read = scanner.nextLine();
			String cmd = read.split(" ")[0];
			String arg = read.substring(cmd.length() );
			switch (cmd){
				case "tell" -> {
					chessServer.tellAll(arg);
				}
				case "game" -> {
					chessServer.printAllGames();
				}
			}
		}
	}
}