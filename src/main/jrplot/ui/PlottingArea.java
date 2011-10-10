package jrplot.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;

import javax.swing.JComponent;

import jrplot.core.Pair;
import jrplot.core.PlotEngine;
import jrplot.core.geometry.CoordinatesConverter;




public class PlottingArea extends JComponent {

	private static final long serialVersionUID = 8064542678184519324L;
	
	private static final double PLOTTING_AREA_PADDING = 10.0;

	/**
	 * How frequent the scale marks will have labels.
	 */
	private static final int LABELED_SCALE_INTERVAL = 5;
	
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
	 * Draws the control button.
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
		
		Pair origin = converter.toScreenCoordinate(
				new Pair(0.0, 0.0));

		g2d.drawLine(0, (int)origin.y, this.getWidth(), (int)origin.y);
		g2d.drawLine((int)origin.x, 0, (int)origin.x, this.getHeight());
		
		/*
		 * Scale marks and labels
		 */
		if (engine.getCurrentFunctionPairs() == null || engine.getCurrentFunctionPairs().isEmpty()) {
			return;
		}
		
		g2d.setFont(new Font("Arial", Font.PLAIN, 9));
		
		/*
		 * In this method we will work with BigDecimal numbers; they look better on screen
		 * because of the precision issues found on Double numbers.
		 * That shouldn't harm performance, as this iteraction is pretty short.
		 *  
		 */
		BigDecimal startingX = BigDecimal.valueOf(engine.scaleIntervalX());
		// Adding a small offset so that the double imprecision does not "drop" the end of scale
		BigDecimal xBound = BigDecimal.valueOf(engine.currentMaxX() + 0.001);
		BigDecimal xStep = BigDecimal.valueOf(engine.scaleIntervalX());
		drawScaleMarksX(g2d, startingX, xBound, xStep, false);
		
		startingX = BigDecimal.valueOf(engine.scaleIntervalX()).negate();
		xBound = BigDecimal.valueOf(engine.currentMinX() - 0.001);
		drawScaleMarksX(g2d, startingX, xBound, xStep, true);
		
		BigDecimal startingY = BigDecimal.valueOf(engine.scaleIntervalY());
		BigDecimal yBound = BigDecimal.valueOf(engine.currentMaxY() + 0.001);
		BigDecimal yStep = BigDecimal.valueOf(engine.scaleIntervalY());
		drawScaleMarksY(g2d, startingY, yBound, yStep, false);
		
		startingY = BigDecimal.valueOf(engine.scaleIntervalY()).negate();
		yBound = BigDecimal.valueOf(engine.currentMinY() - 0.001);
		drawScaleMarksY(g2d, startingY, yBound, yStep, true);
	}

	/**
	 * 
	 * @param g2d
	 * @param startingX
	 * @param xBound
	 * @param xStep
	 * @param descending 
	 */
	private void drawScaleMarksX(Graphics2D g2d, BigDecimal startingX,
			BigDecimal xBound, BigDecimal xStep, boolean descending) {
		Pair scalePoint = new Pair(0.0, 0.0);
		BigDecimal x = startingX;
		int labelCounter = 1;
		while ((!descending && x.compareTo(xBound) <= 0)
				|| (descending && x.compareTo(xBound) >= 0)) {
			scalePoint.x = x.doubleValue();
			Pair converted = converter.toScreenCoordinate(scalePoint);
			
			if (labelCounter == LABELED_SCALE_INTERVAL) {
				g2d.drawLine((int)converted.x, (int)converted.y - 4, (int)converted.x, (int)converted.y + 4);
				g2d.drawString(String.valueOf(x), (int)converted.x - 5, (int)converted.y + 15);
				labelCounter = 1;
			} else {
				g2d.drawLine((int)converted.x, (int)converted.y - 2, (int)converted.x, (int)converted.y + 2);
				labelCounter++;
			}
			
			if (descending) {
				x = x.subtract(xStep);
			} else {
				x = x.add(xStep);
			}
		}
	}
	
	private void drawScaleMarksY(Graphics2D g2d, BigDecimal startingY,
			BigDecimal yBound, BigDecimal yStep, boolean descending) {
		Pair scalePoint = new Pair(0.0, 0.0);
		BigDecimal y = startingY;
		int labelCounter = 1;
		while ((!descending && y.compareTo(yBound) <= 0)
				|| (descending && y.compareTo(yBound) >= 0)) {
			scalePoint.y = y.doubleValue();
			Pair converted = converter.toScreenCoordinate(scalePoint);
			
			if (labelCounter == LABELED_SCALE_INTERVAL) {
				g2d.drawLine((int)converted.x - 4, (int)converted.y, (int)converted.x + 4, (int)converted.y);
				g2d.drawString(String.valueOf(y), (int)converted.x - 23, (int)converted.y + 2);
				labelCounter = 1;
			} else {
				g2d.drawLine((int)converted.x - 2, (int)converted.y, (int)converted.x + 3, (int)converted.y);
				labelCounter++;
			}
			
			if (descending) {
				y = y.subtract(yStep);
			} else {
				y = y.add(yStep);
			}
		}
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
