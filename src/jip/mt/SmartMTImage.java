package jip.mt;

import jip.mt.gesture.ImageDoubleTapAndFadeGestureListener;
import jip.mt.gesture.RadialMenuGestureListener;
import jip.mt.gesture.ZTranslateOnDragGestureListener;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

/**
 * MTImage with some gestures and effects: translate on drag and destroy on double tap
 * @author jip
 *
 */
public class SmartMTImage extends MTImage {

	public SmartMTImage(AbstractMTApplication app, PImage image, float dimensiony) {
		super(app, image);		
		
		setNoStroke(true);			
		
		float scalefactor = dimensiony / image.height;
		
		// scale and position
		scale(scalefactor, scalefactor, 1, new Vector3D(0, 0, 0));
		setPositionGlobal(new Vector3D(app.width /2, app.height / 2 - 100));
		
		// gestures and effects on drag
		addGestureListener(DragProcessor.class, new ZTranslateOnDragGestureListener(10.0f)); 
		addGestureListener(DragProcessor.class, new InertiaDragAction());
		
//		// gestures and effects on double tap
		registerInputProcessor(new TapProcessor(app, 15, true));
		addGestureListener(TapProcessor.class, new ImageDoubleTapAndFadeGestureListener());

		registerInputProcessor(new TapAndHoldProcessor(app, 500));
		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, this));
		addGestureListener(TapAndHoldProcessor.class, new RadialMenuGestureListener(app));
		
	}

}
