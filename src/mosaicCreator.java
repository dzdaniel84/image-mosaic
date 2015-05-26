import processing.core.*;

public class mosaicCreator extends PApplet{
	
	static final int R = 0, G = 1, B = 2;
	static final int GRID_SIZE= 10;
	static final int APP_SIZE = 700;
	static final int FILE_TOTAL = 409;
	
	PImage mainImage = new PImage();
	float[][][] grid;
	boolean normalMode = true;
	float[][] avgImgColor = new float[409][3];
	
	public void setup() {
		imageReferencer();
		mainImage = loadImage("images/408.JPG");
		
		if (mainImage.width > mainImage.height)
			size(APP_SIZE, (APP_SIZE/mainImage.width)*mainImage.height);
		else
			size((APP_SIZE/mainImage.height)*mainImage.width, APP_SIZE);
		
		grid = new float[width/GRID_SIZE][height/GRID_SIZE][3];
		
		loadPixels();
		
		System.out.println("Accumulating Pixels... ");
		for (int i = 0; i < width - width%GRID_SIZE; i++){
			for (int j = 0; j < height - height%GRID_SIZE; j++){
				int loc = i + j*width;
				
				grid[i/GRID_SIZE][j/GRID_SIZE][R] += red(mainImage.pixels[loc]);
				grid[i/GRID_SIZE][j/GRID_SIZE][G] += green(mainImage.pixels[loc]);
				grid[i/GRID_SIZE][j/GRID_SIZE][B] += blue(mainImage.pixels[loc]);
			}
		}
		
		System.out.println("Averaging Pixels... ");
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				grid[i][j][R] /= GRID_SIZE * GRID_SIZE;
				grid[i][j][G] /= GRID_SIZE * GRID_SIZE;
				grid[i][j][B] /= GRID_SIZE * GRID_SIZE;
			}
		}
		
	}
	
	public void draw() {
		
		if (normalMode){
			image(mainImage, 0, 0);
		} else {

			System.out.println("Matching Pixels... ");
			for (int i = 0; i < grid.length; i++){
				for (int j = 0; j < grid[0].length; j++){
					float[] colors = {grid[i][j][R], grid[i][j][G], grid[i][j][B]};
					image(getCorrectImage(colors), i*GRID_SIZE, j*GRID_SIZE, GRID_SIZE, GRID_SIZE);
				}
			}
		}
	}
	
	public void mouseClicked() {
		normalMode = !normalMode;
	}
	
	public void imageReferencer(){
		for (int i = 0; i < FILE_TOTAL; i++){
			System.out.println("Loading Pixels... " + (double)(i*100)/FILE_TOTAL + "% Done");
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
			
			avgImgColor[i][R] = r / img.pixels.length;
			avgImgColor[i][G] = g / img.pixels.length;
			avgImgColor[i][B] = b / img.pixels.length;
		}
	}
	
	public PImage getCorrectImage(float[] colors){
		float minDist = Integer.MAX_VALUE;
		int minDistLoc = 0;
		for (int i = 0; i < avgImgColor.length; i++ ) {
			float curDist = getColorDistance(colors, avgImgColor[i]);
			
			if (curDist < minDist) {
				minDist = curDist;
				minDistLoc = i;
			}
		}
		return loadImage("images/" + ((minDistLoc < 10) ? "00"+minDistLoc : (minDistLoc < 100) ? "0"+minDistLoc : minDistLoc ) + ".JPG");
	}
	
	public float getColorDistance(float[] a, float[] b){	
		return (float) Math.sqrt(Math.pow(a[R]-b[R], 2) + Math.pow(a[G]-b[G], 2) + Math.pow(a[B]-a[B], 2));				
	}
	
	public static void main(String[] args){
		PApplet.main(new String[] {"mosaicCreator"});
	}
	
	
}