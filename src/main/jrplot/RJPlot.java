package jrplot;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jrplot.ui.JPlotUI;



public class RJPlot {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new RJPlot().startGUI();
			}
		});
	}

	public void startGUI() {
		JFrame frame = new JFrame("JPlot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		
		JPlotUI ui = new JPlotUI();
		frame.add(ui);
		
		frame.setLocation(200, 200);
		frame.pack();
		frame.setVisible(true);
	}
}
