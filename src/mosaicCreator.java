import java.util.HashMap;

import processing.core.*;

public class mosaicCreator extends PApplet{
	
	static final int R = 0, G = 1, B = 2;
	static final int gridBoxSize = 10;
	
	PImage mainImage = new PImage();
	float[][][] grid;
	boolean normalMode = true;
	//static HashMap<Integer, Integer> ReferenceMap = new HashMap<Integer, Integer>();
	float[][] averageColorForEachImage = new float[409][3];
	
	//cdd
	public void setup() {
		imageReferencer();
		mainImage = loadImage("images/408.JPG");
		
		if (mainImage.width > mainImage.height)
			size(700, (700/mainImage.width)*mainImage.height);
		else
			size((700/mainImage.height)*mainImage.width, 700);
		
		grid = new float[width/gridBoxSize][height/gridBoxSize][3];
		
		loadPixels();
		
		for (int i = 0; i < width - width%gridBoxSize; i++){
			for (int j = 0; j < height - height%gridBoxSize; j++){
				int loc = i + j*width;
				
				grid[i/gridBoxSize][j/gridBoxSize][R] += red(mainImage.pixels[loc]);
				grid[i/gridBoxSize][j/gridBoxSize][G] += green(mainImage.pixels[loc]);
				grid[i/gridBoxSize][j/gridBoxSize][B] += blue(mainImage.pixels[loc]);
			}
		}
		
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				grid[i][j][R] /= gridBoxSize * gridBoxSize;
				grid[i][j][G] /= gridBoxSize * gridBoxSize;
				grid[i][j][B] /= gridBoxSize * gridBoxSize;
			}
		}
		
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				PImage img = getCorrectImage(grid[i][j]);
				image(img, i*gridBoxSize, j*gridBoxSize, gridBoxSize, gridBoxSize);
			}
		}
	}
	
	public void draw() {
		
		if (normalMode){
			image(mainImage, 0, 0);
			for (int i = 0; i < height; i += gridBoxSize)
				line(0, i, width, i);
			for (int i = 0; i < width; i += gridBoxSize)
				line(i, 0, i, height);
		} else {
			for (int i = 0; i < grid.length; i++){
				for (int j = 0; j < grid[0].length; j++){
					float r = grid[i][j][R];
					float g = grid[i][j][G];
					float b = grid[i][j][B];
					
					fill(r, g, b);
					rect(i*gridBoxSize, j*gridBoxSize, i*gridBoxSize+gridBoxSize, j*gridBoxSize+gridBoxSize);
				}
			}
		}
	}
	
	public void mouseClicked() {
		
		normalMode = !normalMode;
		float r = grid[mouseX/gridBoxSize][mouseY/gridBoxSize][R];
		float g = grid[mouseX/gridBoxSize][mouseY/gridBoxSize][G];
		float b = grid[mouseX/gridBoxSize][mouseY/gridBoxSize][B];
		
		//System.out.println(ReferenceMap.get(color(r, g, b)));
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
			
			averageColorForEachImage[i][R] = r;
			averageColorForEachImage[i][G] = g;
			averageColorForEachImage[i][B] = b;
			//ReferenceMap.put(color(r, g, b), i);
		}
	}
	
	//Precondition: i is an array with 3 values
	public PImage getCorrectImage(float[] colors){
		float minDistance = Integer.MAX_VALUE;
		int minDistanceIndex = 0;
		for (int i = 0; i < averageColorForEachImage.length; i++ ) {
			float distance = getColorDistance(colors,averageColorForEachImage[i]); 
			if (distance < minDistance) {
				minDistance = distance;
				minDistanceIndex = i;
			}
		}
		PImage img = loadImage("images/" + ((minDistanceIndex < 10) ? "00"+minDistanceIndex : (minDistanceIndex < 100) ? "0"+minDistanceIndex : minDistanceIndex ) + ".JPG");
		return img;
	}
	
	public float getColorDistance(float[] colorA, float[] colorB){
		double redA = colorA[0];
		double redB = colorB[0];
		double greenA = colorA[1];
		double greenB = colorB[1];
		double blueA = colorA[2];
		double blueB = colorB[2];
		double output = Math.sqrt(Math.pow(redA-redB, 2)
					  + Math.pow(greenA-greenB, 2)
					  + Math.pow(blueA-blueB, 2));				
		return (float)output;				
	}
	public static void main(String[] args){
		PApplet.main(new String[] {"mosaicCreator"});
	}
	
	
}
