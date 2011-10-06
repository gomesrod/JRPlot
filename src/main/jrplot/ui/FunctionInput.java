package jrplot.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jrplot.core.PlotEngine;
import jrplot.core.expression.ExpressionException;

/**
 * The function input form.
 * @author rodrigo
 *
 */
public class FunctionInput extends JPanel {

	private static final long serialVersionUID = -1037545325435699582L;
	
	private static final String DEFAULT_X_MIN = "-10.0";
	private static final String DEFAULT_X_MAX = "10.0";
	
	private PlotEngine engine;
	private JPlotUI controller;

	private JTextField txtFunction;
	private JTextField txtMinX;
	private JTextField txtMaxX;

	private JLabel lbErros;
	private JButton btOk;
	private JButton btCancel;
	
	public FunctionInput(PlotEngine engine, JPlotUI controller) {
		this.engine = engine;
		this.controller = controller;
		
//		this.setLayout(new BorderLayout());
		JPanel container = createControlsContainer();
		this.add(container);
	}

	private JPanel createControlsContainer() {
		JPanel container = new JPanel(new GridBagLayout());
		
		container.setPreferredSize(new Dimension(300, 200));
		
		GridBagConstraints constr = new GridBagConstraints();
		constr.anchor = GridBagConstraints.CENTER;
		constr.gridx = 0;
		constr.gridy = 0;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		
		container.add(new JLabel("New Function"), constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 1;
		container.add(new JLabel(" f(x) = "), constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 1;
		constr.gridy = 1;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.fill = GridBagConstraints.HORIZONTAL;
		txtFunction = new JTextField();
		container.add(txtFunction, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 2;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		lbErros = new JLabel("");
		lbErros.setForeground(Color.RED);
		container.add(lbErros, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 1;
		constr.gridy = 3;
		container.add(new JLabel("x min: "), constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 2;
		constr.gridy = 3;
		constr.fill = GridBagConstraints.HORIZONTAL;
		txtMinX = new JTextField(DEFAULT_X_MIN);
		container.add(txtMinX, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 3;
		constr.gridy = 3;
		container.add(new JLabel(" x max: "), constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 4;
		constr.gridy = 3;
		constr.fill = GridBagConstraints.HORIZONTAL;
		txtMaxX = new JTextField(DEFAULT_X_MAX);
		container.add(txtMaxX, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 1;
		constr.gridy = 4;
		constr.gridwidth = 2;
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.weightx = 0.5;
		constr.insets = new Insets(10, 0, 0, 0);
		btOk = new JButton("OK");
		btOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lbErros.setText("");
				String functionText = FunctionInput.this.txtFunction.getText();
				String minXtext = FunctionInput.this.txtMinX.getText();
				String maxXtext = FunctionInput.this.txtMaxX.getText();
				try {
					//TODO Validate the consistency of minX e maxX
					FunctionInput.this.engine.updateFunction(functionText, 
							Double.valueOf(minXtext), Double.parseDouble(maxXtext));
				} catch (ExpressionException e1) {
					lbErros.setText(e1.getMessage());
					return;
				}
				
				FunctionInput.this.controller.notifyFunctionInputClosed();
			}
		});
		container.add(btOk, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 3;
		constr.gridy = 4;
		constr.gridwidth = 2;
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.weightx = 0.5;
		constr.insets = new Insets(10, 0, 0, 0);
		btCancel = new JButton("Cancel");
		btCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FunctionInput.this.controller.notifyFunctionInputClosed();
			}
		});
		container.add(btCancel, constr);
		
		constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 5;
		constr.fill = GridBagConstraints.BOTH;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weighty = 1.0;
		constr.insets = new Insets(10, 0, 0, 0);
		JTextArea helpArea = createHelpArea();
		JScrollPane helpScroll = new JScrollPane(helpArea);
		container.add(helpScroll, constr);
		
		return container;
	}

	private JTextArea createHelpArea() {
		JTextArea textArea = new JTextArea();
		
		InputStream helpContents = getClass().getClassLoader().getResourceAsStream(
				"function-syntax-help.txt");
		
		if (helpContents == null) {
			textArea.setText("** Help file no found **");
		
		} else {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(helpContents));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					textArea.append(line);
					textArea.append("\n");
				}
			} catch (IOException e) {
				// This is not supposed to happen! If the file didn't exist we would not have
				// arrived at this point.
				throw new RuntimeException("Unexpected exception reading help file");
			}
		}
		
		textArea.setCaretPosition(0);
		textArea.setEditable(false);
		return textArea;
	}

	
}
