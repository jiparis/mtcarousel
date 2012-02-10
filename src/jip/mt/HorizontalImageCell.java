package jip.mt;

import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

public class HorizontalImageCell extends MTListCell {
	
	PApplet app;
	public HorizontalImageCell(PApplet applet, float width, float height, PImage pim, float cellborder) {
		super(applet, width, height);
		app = applet;
		
		// textured polygon
//		Vertex[] vertices = new Vertex[]{
//				new Vertex(0, 0, 0, 		0, 0),
//				new Vertex(0, height,	0, 		0, 1),
//				new Vertex(width, height, 0,		1, 1),
//				new Vertex(width, 0, 0, 		1, 0),
//				new Vertex(0, 0, 0, 		0, 0)
//		};
	
		// images rotated -90º
		
		Vertex[] vertices = new Vertex[]{
				new Vertex(0, 0, 0, 		0, 1),
				new Vertex(0, height,	0, 		1, 1),
				new Vertex(width, height, 0,		1, 0),
				new Vertex(width, 0, 0, 		0, 0),
				new Vertex(0, 0, 0, 		0, 1)
		};
		
		MTPolygon p = new MTPolygon(app, vertices);
		p.setName("Polygon");
		p.setTexture(pim);	
		//p.setNoFill(true);
		p.setNoStroke(true);
		
		//añade el polígono
		addChild(p);
		
		//eventos
		registerInputProcessor(new TapProcessor(app, 15));		
	}		
}
