package jrplot.ui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import jrplot.core.PlotEngine;



/**
 * Main UI controller.
 * 
 * @author rodrigo
 *
 */
public class JPlotUI extends JPanel {

	private static final long serialVersionUID = -1087720068051295101L;

	private static final Dimension PREFERRED_SIZE = new Dimension(500, 400);
	
	private static final String PLOTAREA_LAYOUT_KEY = "plottingArea";
	private static final String FUNCTIONINPUT_LAYOUT_KEY = "functionInput";

	private PlottingArea plottingArea;
	private FunctionInput functionInputDialog;
	
	private PlotEngine engine;

	
	public JPlotUI() {
		engine = new PlotEngine();

		this.setLayout(new CardLayout());

		plottingArea = new PlottingArea(engine, this);
		plottingArea.setPreferredSize(PREFERRED_SIZE);
		this.add(plottingArea, PLOTAREA_LAYOUT_KEY);
		
		functionInputDialog = new FunctionInput(engine, this);
		functionInputDialog.setPreferredSize(PREFERRED_SIZE);
		this.add(functionInputDialog, FUNCTIONINPUT_LAYOUT_KEY);
	}
	
	void notifyNewFunctionDialogRequested() {
		CardLayout layout = (CardLayout) this.getLayout();
		layout.show(this, FUNCTIONINPUT_LAYOUT_KEY);
	}

	void notifyFunctionInputClosed() {
		CardLayout layout = (CardLayout) this.getLayout();
		layout.show(this, PLOTAREA_LAYOUT_KEY);
	}
}
