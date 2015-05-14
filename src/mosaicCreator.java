import java.util.HashMap;

import processing.core.*;

public class mosaicCreator extends PApplet{
	
	static final int R = 0, G = 1, B = 2;
	
	PImage mainImage = new PImage();
	float[][][] grid;
	boolean normalMode = true;
	static HashMap<Integer, Integer> ReferenceMap = new HashMap<Integer, Integer>();
	
	public void setup() {
		imageReferencer();
		mainImage = loadImage("images/408.JPG");
		
		if (mainImage.width > mainImage.height)
			size(700, (700/mainImage.width)*mainImage.height);
		else
			size((700/mainImage.height)*mainImage.width, 700);
		
		grid = new float[width/10][height/10][3];
		
		loadPixels();
		
		for (int i = 0; i < width - width%10; i++){
			for (int j = 0; j < height - height%10; j++){
				int loc = i + j*width;
				
				grid[i/10][j/10][R] += red(mainImage.pixels[loc]);
				grid[i/10][j/10][G] += green(mainImage.pixels[loc]);
				grid[i/10][j/10][B] += blue(mainImage.pixels[loc]);
			}
		}
		
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++){
				grid[i][j][R] /= 100;
				grid[i][j][G] /= 100;
				grid[i][j][B] /= 100;
			}
		
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				PImage img = getCorrImage(grid[i][j]);
				image(img, i*10, j*10, 10, 10);
			}
		}
	}
	
	public void draw() {
		
		if (normalMode){
			image(mainImage, 0, 0);
			for (int i = 0; i < height; i += 10)
				line(0, i, width, i);
			for (int i = 0; i < width; i += 10)
				line(i, 0, i, height);
		} else {
			for (int i = 0; i < grid.length; i++){
				for (int j = 0; j < grid[0].length; j++){
					float r = grid[i][j][R];
					float g = grid[i][j][G];
					float b = grid[i][j][B];
					
					fill(r, g, b);
					rect(i*10, j*10, i*10+10, j*10+10);
				}
			}
		}
	}
	
	public void mouseClicked() {
		
		normalMode = !normalMode;
		float r = grid[mouseX/10][mouseY/10][R];
		float g = grid[mouseX/10][mouseY/10][G];
		float b = grid[mouseX/10][mouseY/10][B];
		
		System.out.println(ReferenceMap.get(color(r, g, b)));
	}
	
	public void imageReferencer(){
		for (int i = 0; i < 409; i++){
			System.out.println(i);
			PImage img = loadImage("images/" + ((i < 10) ? "00"+i : (i < 100) ? "0"+i : i ) + ".JPG");
			float r = 0, g = 0, b = 0;
			
			img.loadPixels();
			for (int w = 0; w < img.width; w++)
				for (int h = 0; h < img.height; h++){
					int loc = w + h*img.width;
					
					r += red(img.pixels[loc]);
					g += green(img.pixels[loc]);
					b += blue(img.pixels[loc]);
				}
			
			r /= img.pixels.length;
			g /= img.pixels.length;
			b /= img.pixels.length;
			
			ReferenceMap.put(color(r, g, b), i);
		}
	}
	
	//Precondition: i is an array with 3 values
	public PImage getCorrImage(float[] grid){
		int color = color(grid[R], grid[G], grid[B]);
		
		if (ReferenceMap.containsKey(color)){
			int j = ReferenceMap.get(color);
			PImage img = loadImage("images/" + ((j < 10) ? "00"+ j : (j < 100) ? "0"+ j : j ) + ".JPG"); 
		} else {
			//To-Do
		}
		
		return null;
	}
	
	public static void main(String[] args){
		PApplet.main(new String[] {"mosaicCreator"});
	}
	
	
}
