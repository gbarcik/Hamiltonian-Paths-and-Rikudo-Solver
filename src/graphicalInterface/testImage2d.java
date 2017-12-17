package graphicalInterface;

import java.awt.Color;

public class testImage2d {
	public static void main(String[] args) {
		Image2d img = new Image2d(256);
		img.addPolygon(new int[] {50,100,150,200}, new int[] {50,150,200,100}, Color.BLUE, Color.RED);
		img.addSegment(50, 200, 150, 100, 10, Color.GREEN);
		new Image2dViewer(img);		
	}
}
