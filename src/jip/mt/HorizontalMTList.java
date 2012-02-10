package jip.mt;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;


public class HorizontalMTList extends MTRectangle {
	MTList list;

	public HorizontalMTList(PApplet app, float x, float y, float width,
			float height, float listHeight, float cellPadding) {
		super(app, x, y, width, height);

		// default colors
		setFillColor(new MTColor(100, 100, 100, 100));
		setStrokeColor(new MTColor(100,100,100));
		
		// no input
		unregisterAllInputProcessors();
		removeAllGestureEventListeners();		
		
		/* The list. */		
		list = new MTList(app, 0, 0, listHeight, app.getWidth(), cellPadding);
		list.setNoFill(true);
		list.setNoStroke(true);
		
		addChild(list);
		
		// rotate and put mtlist at bottom
		list.rotateZ(list.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		list.setPositionRelativeToParent(getCenterPointRelativeToParent());
		// move a little to avoid flickering when rotate
		list.translate(new Vector3D(0, 0, 1));		
	}
	
	public MTList getList(){
		return list;
	}

}
