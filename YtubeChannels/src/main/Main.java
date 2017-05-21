package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import views.View;
import controller.Controller;

public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				View view = createView();
				createController(view);
			}			
		});		
	}
	
	public static View createView(){
		View view = new View();
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.pack();
		view.setVisible(true);
		return view;
	}
	
	private static void createController(View view) {
		new Controller(view);		
	}
}
