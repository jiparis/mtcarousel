package jip.mt.gesture;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;
import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;

public class RadialMenuGestureListener implements IGestureEventListener {

	private MTRadialMenu rm;
	private AbstractMTApplication app;
	
	public RadialMenuGestureListener(AbstractMTApplication app){
		this.app = app;
	}
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		int id = ge.getId();
		TapAndHoldEvent ev = (TapAndHoldEvent) ge;
		
		switch(id){
		case TapAndHoldEvent.GESTURE_ENDED:
			if(ev.isHoldComplete()){
				if(rm != null && !rm.isVisible()){
					rm.destroy(); rm = null;
				}
				if (rm == null){
					IFont font = FontManager.getInstance().createFont(app, "arial.ttf",
			                16, // Font size
			                new MTColor(255, 255, 255, 255), // Font fill color
			                true); // Anti-alias
					List<MTMenuItem> menuItems = new ArrayList<MTMenuItem>();
					final MTMenuItem menu1 = new MTMenuItem("File", null);
		            menu1.addSubMenuItem(new MTMenuItem("New Session", null));
		            menu1.addSubMenuItem(new MTMenuItem("Open Session", null));
		            menu1.addSubMenuItem(new MTMenuItem("Save Session", null));
		            menu1.addSubMenuItem(new MTMenuItem("Properties", null));
		            menuItems.add(menu1);
		            
		            final MTMenuItem menu2 = new MTMenuItem("Edit", null);
		            menu2.addSubMenuItem(new MTMenuItem("Copy", null));
		            menu2.addSubMenuItem(new MTMenuItem("Paste", null));
		            menu2.addSubMenuItem(new MTMenuItem("Cut", null));
		            menu2.addSubMenuItem(new MTMenuItem("Select", null));
		            menuItems.add(menu2);
		            
					rm = new MTRadialMenu(app, ev.getLocationOnScreen(),
							font, 0.1f, menuItems);
					app.getCurrentScene().getCanvas().addChild(rm);
					rm.scaleGlobal(0.5f, 0.5f, 0.5f, rm.getCenterPointGlobal());
				}
			}
		}
		return false;
	}

}
