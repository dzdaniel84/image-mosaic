import processing.core.*;

public class mosaicCreator extends PApplet{
	
	PImage mainImage = new PImage();
	int[][] grid;
	boolean normalMode = true;
	
	public void setup() {
		mainImage = loadImage("images/DSC_4170.JPG");
		
		if (mainImage.width > mainImage.height)
			size(1000, (1000/mainImage.width)*mainImage.height);
		else
			size((1000/mainImage.height)*mainImage.width, 1000);
		
		grid = new int[width/10][height/10];
		
		loadPixels();
		
		for (int i = 0; i < width - width%10; i++){
			for (int j = 0; j < height - height%10; j++){
				int loc = i + j*width;
				
				float r = red(mainImage.pixels[loc]);
			    float g = green(mainImage.pixels[loc]);
			    float b = blue(mainImage.pixels[loc]);
			    
				grid[i/10][j/10] += color(r, g, b);
			}
		}
		
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j] /= 100;
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
					float r = red(grid[i][j]);
					float g = green(grid[i][j]);
					float b = blue(grid[i][j]);
					
					fill(r, g, b);
					rect(i*10, j*10, i*10+10, j*10+10);
				}
			}
		}
	}
	
	public void mouseClicked() {
		
		normalMode = !normalMode;
		float r = red(grid[mouseX/10][mouseY/10]);
		float g = green(grid[mouseX/10][mouseY/10]);
		float b = blue(grid[mouseX/10][mouseY/10]);
		
		System.out.println(r + "," + g + "," + b);
	}
	
	public static void main(String[] args){
		PApplet.main(new String[] {"mosaicCreator"});
	}
	
	
}
