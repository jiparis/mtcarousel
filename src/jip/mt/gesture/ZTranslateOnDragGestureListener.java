package jip.mt.gesture;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.util.math.Vector3D;

public class ZTranslateOnDragGestureListener implements IGestureEventListener{
	boolean translated = false;
	float z = 10;
	
	public ZTranslateOnDragGestureListener(float z){
		this.z = z;
	}
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		MTComponent c = (MTComponent) ge.getTarget();
		switch(ge.getId()){
		case(MTGestureEvent.GESTURE_UPDATED):
			if(!translated){
				c.translate(new Vector3D(0, 0, z));
				translated = true;
			}
			break;
		case(MTGestureEvent.GESTURE_ENDED):
			if(translated){
				c.translate(new Vector3D(0, 0, -z));
				translated = false;
			}
			break;
		}
		return false;
	}
}
