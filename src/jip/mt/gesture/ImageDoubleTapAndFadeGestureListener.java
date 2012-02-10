package jip.mt.gesture;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;

public class ImageDoubleTapAndFadeGestureListener implements IGestureEventListener{
	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {
		TapEvent te = (TapEvent) ge;
		final MTImage im = (MTImage) ge.getTarget();
		if (te.getId() == TapEvent.GESTURE_ENDED &&
				te.isDoubleTap()){
			final float imw = im.getWidthXY(TransformSpace.GLOBAL);
			new AniAnimation(0, 50, 300, AniAnimation.CUBIC_OUT, this).addAnimationListener(new IAnimationListener() {
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					
					switch(ae.getId()){
						case(AnimationEvent.ANIMATION_STARTED):
							im.getImage().setNoStroke(true);
							break;
						case(AnimationEvent.ANIMATION_UPDATED):
							float v = ae.getValue();
							im.setWidthXYGlobal(imw + v);
							//alpha
							MTColor fc = new MTColor(255,255,255,250 - v*5);
							im.setFillColor(fc);
							im.getImage().setFillColor(fc);
							break;
						case(AnimationEvent.ANIMATION_ENDED):
							im.destroy();
					}								
				}
			}).start();						
		}
		return false;
	}

}
