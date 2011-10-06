package jrplot.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import jrplot.core.Pair;
import jrplot.core.PlotEngine;
import jrplot.core.geometry.CoordinatesConverter;




public class PlottingArea extends JComponent {

	private static final long serialVersionUID = 8064542678184519324L;
	
	private static final double PLOTTING_AREA_PADDING = 6.0;
	
	private PlotEngine engine;
	private JPlotUI controller;
	private CoordinatesConverter converter;
	
	public PlottingArea(PlotEngine engine, JPlotUI controller) {
		this.engine = engine;
		this.controller = controller;
		this.converter = new CoordinatesConverter();
		PlottingAreaMouseListener mouseListener = new PlottingAreaMouseListener();
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		super.paintComponent(g);
		paintBackground(g2d);
		renderButton(g2d);
		drawAxis(g2d);
		drawFunction(g2d);
	}
	
	/**
	 * Draws the configuration function button.
	 * @param g2d
	 */
	private void renderButton(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawRect(2, 2, 20, 17);
		
		g2d.drawString("f(x)", 5, 15);
		
		g2d.setColor(Color.GRAY);
		g2d.fillRect(1, 3, 1, 18);
		g2d.fillRect(1, 20, 20, 1);
	}

	private void paintBackground(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	private void drawAxis(Graphics2D g2d) {
		g2d.setColor(Color.GRAY);
		
		converter.logicalBounds(engine.currentMinX(), engine.currentMaxX(), 
								engine.currentMinY(), engine.currentMaxY());
		converter.screenSize(this.getWidth(), this.getHeight(), PLOTTING_AREA_PADDING);
		
		Pair xAxisBegin = converter.toScreenCoordinate(
				new Pair(engine.currentMinX(), 0.0));
		Pair xAxisEnd = converter.toScreenCoordinate(
				new Pair(engine.currentMaxX(), 0.0));
		
		Pair yAxisBegin = converter.toScreenCoordinate(
				new Pair(0.0, engine.currentMinY()));
		Pair yAxisEnd = converter.toScreenCoordinate(
				new Pair(0.0, engine.currentMaxY()));

		g2d.drawLine((int)xAxisBegin.x, (int)xAxisBegin.y, (int)xAxisEnd.x, (int)xAxisEnd.y);
		g2d.drawLine((int)yAxisBegin.x, (int)yAxisBegin.y, (int)yAxisEnd.x, (int)yAxisEnd.y);
	}

	private void drawFunction(Graphics2D g2d) {
		g2d.setColor(Color.BLUE);
		
		if (engine.getCurrentFunctionPairs() == null || engine.getCurrentFunctionPairs().isEmpty()) {
			g2d.drawString("Use the button to insert a function", 56, 40);
			g2d.drawOval(-7, -7, 34, 34);
			g2d.drawLine(10, 27, 10, 37);
			g2d.drawLine(10, 37, 50, 37);
			return;
		}
		
		converter.logicalBounds(engine.currentMinX(), engine.currentMaxX(), 
								engine.currentMinY(), engine.currentMaxY());
		converter.screenSize(this.getWidth(), this.getHeight(), PLOTTING_AREA_PADDING);
		
		for (Pair p : engine.getCurrentFunctionPairs()) {
			Pair point = converter.toScreenCoordinate(p);
			// A point is represented by a "zero-sized line"
			g2d.drawLine((int)point.x, (int)point.y, (int)point.x, (int)point.y);
		}

	}
	
	private class PlottingAreaMouseListener implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Click is processed only inside the "button" area.
			if (e.getX() < 22 && e.getY() < 19) {
				PlottingArea.this.controller.notifyNewFunctionDialogRequested();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (e.getX() < 22 && e.getY() < 19) {
				PlottingArea.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				PlottingArea.this.setCursor(Cursor.getDefaultCursor());
			}
		}
		
	}
}
