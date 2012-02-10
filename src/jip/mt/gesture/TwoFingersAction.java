package jip.mt.gesture;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanEvent;
import org.mt4j.util.camera.MTCamera;

public class TwoFingersAction implements IGestureEventListener {

	@Override
	public boolean processGestureEvent(MTGestureEvent g) {
		if (g instanceof PanEvent){
			PanEvent pe = (PanEvent)g;
			MTCamera c = (MTCamera) pe.getCamera();
			int id = pe.getId();
			if (id == PanEvent.GESTURE_UPDATED){
				c.moveViewCenter(-pe.getTranslationVector().getX()/2, -pe.getTranslationVector().getY()/2, 0);
			}
			else if(id == PanEvent.GESTURE_ENDED){
				c.resetToDefault();
			}
		}
		return false;
	}

}
