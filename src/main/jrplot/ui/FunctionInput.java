package jrplot.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jrplot.core.PlotEngine;



public class FunctionInput extends JPanel {

	private static final long serialVersionUID = -1037545325435699582L;
	
	private PlotEngine engine;
	private JPlotUI controller;

	private JTextField txtFunction;
	private JLabel lbErros;
	private JButton btOk;
	private JButton btCancel;
	
	
	public FunctionInput(PlotEngine engine, JPlotUI controller) {
		this.engine = engine;
		this.controller = controller;
		
		this.setLayout(new GridLayout(5, 1));
		
		this.add(new JLabel("Digite o texto da função"));
		
		txtFunction = new JTextField();
		this.add(txtFunction);
		
		lbErros = new JLabel("");
		this.add(lbErros);
		
		btOk = new JButton("OK");
		btOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String functionText = FunctionInput.this.txtFunction.getText();
				FunctionInput.this.engine.updateFunction(functionText);
				
				FunctionInput.this.controller.notifyFunctionInputClosed();
			}
		});
		this.add(btOk);
		
		btCancel = new JButton("Cancel");
		btCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FunctionInput.this.controller.notifyFunctionInputClosed();
			}
		});
		this.add(btCancel);
	}

	
}
