package com.rubenmadsen.TjackServer;


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
		/*List<String> flags = Arrays.asList(args);
		if (flags.size() > 1)
			port = Integer.parseInt(flags.get(1));*/

		Thread thread = new Thread(() -> {
			ChessServer chessServer = new ChessServer(port);
			while (true);
		});
		thread.start();

		// Make sure GUI is created on the event dispatching thread
		/*SwingUtilities.invokeLater(new Runnable() {
			public void run() {

			}
		});*/
	}
}