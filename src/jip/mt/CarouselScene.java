package jip.mt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jip.mt.gesture.TwoFingersAction;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PImage;

public class CarouselScene extends AbstractScene implements Iscene {

	private AbstractMTApplication app;

	// Dimensions
	final static float CONTAINERHEIGHT = 200.0f;
	final static float LISTHEIGHT = 140.0f;
	final static float CELLPADDING = 20.0f;
	final static float CELLBORDER = 5.0f;
	final static float CELLHEIGHT = LISTHEIGHT - CELLBORDER * 2;

	final static float DIMENSIONY = 500f;
		
	public CarouselScene(AbstractMTApplication app, String name) {
		super(app, name);
		this.app = app;
		
		// Create layout (image loading, etc)
		createLayout();

		// optimization
		getCanvas().setFrustumCulling(true);		
	}			
	
	
	/**
	 * main layout
	 */
	protected void createLayout(){		
		// Background
		Vertex[] vertices = new Vertex[]{
				new Vertex(0, 			0,	0, 	0,0,0,255),
				new Vertex(app.width, 	0,	0, 	0,100,0,255),
				new Vertex(app.width, 	app.height,0,	170,170,140,255),
				new Vertex(0,			app.height,0,	170,0,140,255),
				new Vertex(0, 			0,	0,	0,0,0,255),
		};
		MTPolygon p = new MTPolygon(getMTApplication(), vertices);
		p.setName("upper gradient");
		p.setNoStroke(true);
		p.generateAndUseDisplayLists();
		p.setPickable(false);
		this.getCanvas().addChild(p);		
	
		// hotizontal list
		final HorizontalMTList container = new HorizontalMTList(app,
				0, app.height - CONTAINERHEIGHT, 
				app.width, CONTAINERHEIGHT, LISTHEIGHT, CELLPADDING);
		
		// gets the list
		MTList list = container.getList();
		// 1. load images and create mtlistitems
		File f = new File("images");
		for (File imFile : f.listFiles()) {
			PImage pim = app.loadImage(imFile.getAbsolutePath());
			// calculamos la dimensión y (futura x), fijando el ancho (la altura del carrusel)
			HorizontalImageCell lc = new HorizontalImageCell(app, 
									CELLHEIGHT, CELLHEIGHT * pim.width / pim.height,
									pim, CELLBORDER);
			// evento de tap
			lc.addGestureListener(TapProcessor.class, new CarouselGestureListener());
			list.addListElement(lc);
		}

		// send to front in the z axis so that it stands on top of translated images
		container.translate(new Vector3D(0, 0, 15));
				
		getCanvas().addChild(container);
		
		// canvas panning
		getCanvas().registerInputProcessor(new PanProcessorTwoFingers(app));
		getCanvas().addGestureListener(PanProcessorTwoFingers.class, new TwoFingersAction());
		
		// carousel always on top to avoid z-fighting with mtimages
		registerPreDrawAction(new IPreDrawAction() {			
			@Override
			public void processAction() {
				container.sendToFront();
			}
			
			@Override
			public boolean isLoop() {				
				return true;
			}
		});		
		
		// show cursor
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
	}
	
	private List<MTImage> visibleImages = new ArrayList<MTImage>();
	
	/**
	 * adds or focus (bring to front) image
	 * @param image the processing's image
	 */
	private void addImageToCanvas(PImage image){
		MTImage im = imageInCanvas(image);
		if (im != null){
			im.sendToFront();
		}else{
			// create the mtimage
			final MTImage mtImage = new SmartMTImage(app, image, DIMENSIONY);
			
			getCanvas().addChild(mtImage);
			
			// adds the image to the list
			visibleImages.add(mtImage);
			// remove reference when it's destroyed
			mtImage.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {				
				@Override
				public void stateChanged(StateChangeEvent evt) {					
					visibleImages.remove(mtImage);
				}
			});		
		}		
	}
	
	/**
	 * lookup image
	 * @param t the texture
	 * @return
	 */
	private MTImage imageInCanvas(PImage t){
		// focus visible image	
		for (MTImage im: visibleImages){
			if (im.getImage().getTexture() == t){
				return im;
			}
		}
		return null;
	}
	
	/**
	 * tap event listener. Adds an animated frame and send the image to the canvas
	 * @author jip
	 *
	 */
	class CarouselGestureListener implements IGestureEventListener {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			final TapEvent te = (TapEvent)ge;
			if (te.isTapped()){
				//animación
				MTListCell theCell = (MTListCell)te.getTarget();
				
				new AniAnimation(1, 250, 1000, AniAnimation.QUAD_OUT, theCell)
					.addAnimationListener(new IAnimationListener() {									
						@Override
						public void processAnimationEvent(AnimationEvent ae) {
							MTListCell target = (MTListCell)ae.getTarget();
							switch(ae.getId()){
							case AnimationEvent.ANIMATION_UPDATED:									
								float v = ae.getValue();
								target.setStrokeWeight(v/10);
								target.setStrokeColor(new MTColor(18,208,233,256-v));	
								break;							
							default:
								target.setStrokeWeight(0);
							}
						}
					}).start();
				
				// adds image to canvas
				PImage image = ((MTPolygon) theCell.getChildByName("Polygon")).getTexture();
				addImageToCanvas(image);

			}						
			return false;	
		}
	}
	
}
	

