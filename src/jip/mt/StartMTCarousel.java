package jip.mt;

import org.mt4j.MTApplication;


public class StartMTCarousel extends MTApplication {
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		initialize();
	}
	
	@Override
	public void startUp(){
		this.addScene(new CarouselScene(this, "Multi-Touch Carrusel Scene"));
	}
	
}
