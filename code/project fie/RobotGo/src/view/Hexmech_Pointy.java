package view;


import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;



/*
 * this is the helper class for the DrawingPanel 
 * to construct the pointy-top hexagon
 * we found some tutorial online to help me understand to construct the flat-top hexagon.
 * Based on it, we do some calculation to get the pointy-top hexagon
 */
public class Hexmech_Pointy
{

	//Constants
	public final static boolean orFLAT= true;
	public final static boolean orPOINT= false;
	public static boolean ORIENT= orFLAT;  //this is not used. We're never going to do pointy orientation
	public static boolean XYVertex=true;	//true: x,y are the coords of the first vertex.

	
	private static int BORDERS=50;	//default number of pixels for the border.
	
	private static int s=0;	// length of one side
	private static int t=0;	// short side of 30o triangle outside of each hex
	private static int r=0;	// radius of inscribed circle (centre to middle of each side). r= h/2
	private static int h=0;	// height. Distance between centres of two adjacent hexes. Distance between two opposite sides in a hex.
	
	public static Image[] img = {Toolkit.getDefaultToolkit().getImage("img/scout-red.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-red.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-orange.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-yellow.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-green.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-blue.png"),
								Toolkit.getDefaultToolkit().getImage("img/scout-purple.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-red.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-orange.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-yellow.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-green.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-blue.png"),
								Toolkit.getDefaultToolkit().getImage("img/sniper-purple.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-red.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-orange.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-yellow.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-green.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-blue.png"),
								Toolkit.getDefaultToolkit().getImage("img/tank-purple.png")};

	
	public static void setXYasVertex(boolean b) {
		XYVertex=b;
	}
	public static void setBorders(int b){
		BORDERS=b;
	}

	/** This functions takes the Side length in pixels and uses that as the basic dimension of the hex.
            It calculates all other needed constants from this dimension.
	*/
	public static void setSide(int side) {
		s=side;
		t =  (int) (s/2);			
		r =  (int) (s * 0.8660254037844); 
		h=2*s;
	}
	public static void setHeight(int height) {
		h = height;			
		r = (int) (h * 0.433012702);			
		s = (int) (h / 2);	
		t = (int) (h / 4);	
	}

/*********************************************************
Name: hex()
Parameters: (x0,y0) This point is normally the top left corner 
    of the rectangle enclosing the hexagon. 
    However, if XYVertex is true then (x0,y0) is the vertex of the 
    top left corner of the hexagon. 
Returns: a polygon containing the six points.
Called from: drawHex(), fillhex()
Purpose: This function takes two points that describe a hexagon
and calculates all six of the points in the hexagon.
*********************************************************/
	
	public static Polygon hex (int x0, int y0) {

		int y = y0 + BORDERS;
		int x = x0 + BORDERS; // + (XYVertex ? t : 0); //Fix added for XYVertex = true. 
				      // NO! Done below in cx= section
		if (s == 0  || h == 0) {
			System.out.println("ERROR: size of hex has not been set");
			return new Polygon();
		}

		int[] cx,cy;

		if (XYVertex) 
			cx = new int[] {x,x+r,x+r,x,x-r,x-r}; 
		else
			cx = new int[] {x+r,x+r+r,x+r+r,x+r,x,x};	

		cy = new int[] {y,y+t,y+s+t,y+t+s+t,y+t+s,y+t};
		return new Polygon(cx,cy,6);

	}

/********************************************************************
Name: drawHex()
Parameters: (i,j) : the x,y coordinates of the initial point of the hexagon
	    g2: the Graphics2D object to draw on.
Returns: void
Calls: hex() 
Purpose: This function draws a hexagon based on the initial point (x,y).
The hexagon is drawn in the color specified in grid.COLOURELL.
*********************************************************************/
	public static void drawHex(int i, int j, Graphics2D g2) {
		int x = i*2*r + (j%2) * r;
		int y = j*(s+t);
		Polygon poly = hex(x,y);
		g2.setColor(DrawingPanel.COLOURCELL);
		g2.fillPolygon(poly);
		if((DrawingPanel.p_old[DrawingPanel.N].x == i) && ((DrawingPanel.p_old[DrawingPanel.N].y == j))){
			g2.setColor(DrawingPanel.COLOURRED);
		}
		else
			g2.setColor(DrawingPanel.COLOURGRID);
		g2.drawPolygon(poly);
	}

/***************************************************************************
* Name: fillHex()
* Parameters: (i,j) : the x,y coordinates of the initial point of the hexagon
		n   : an integer number to indicate the image to draw in the hex
		g2  : the graphics context to draw on
* Return: void
* Called from:
* Calls: hex()
*Purpose: This draws a filled in polygon based on the coordinates of the hexagon.
		  if the n is between the 1 and 18, then draw the robot picture on the corresponding cell
		  Otherwise, then just make the cell is filled with gray color.
	  
*****************************************************************************/
	public static void fillHex(int i, int j, int n, Graphics2D g2) {

		int x = i*2*r + (j%2) * r;
		int y = j*(s+t);
		// initialize the starting point and color them based on different color of team 
		if((i==3 && j==0) || (i==4 && j==0) || (i==2 && j==1)){
			g2.setColor(DrawingPanel.COLOURORANGE);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}
		
		else if((i==1 && j==3) || (i==1 && j==4) || (i==1 && j==5)){
			g2.setColor(DrawingPanel.COLOURRED);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}

		
		else if((i==2 && j==7) || (i==3 && j==8) || (i==4 && j==8)){
			g2.setColor(DrawingPanel.COLOURPURPLE);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}
		
		else if((i==6 && j==8) || (i==7 && j==8) || (i==7 && j==7)){
			g2.setColor(DrawingPanel.COLOURBLUE);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}
		
		else if((i==9 && j==4) || (i==8 && j==5) || (i==8 && j==3)){
			g2.setColor(DrawingPanel.COLOURGREEN);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}
		
		else if((i==6 && j==0) || (i==7 && j==0) || (i==7 && j==1)){
			g2.setColor(DrawingPanel.COLOURYELLOW);
			g2.fillPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
		}
		else if(DrawingPanel.N == 0){
			g2.setColor(DrawingPanel.COLOURGRID);
			g2.drawPolygon(hex(x,y));
			g2.setColor(DrawingPanel.COLOURCELL);
			g2.fillPolygon(hex(x,y));
		}
		else;
		
		// add the range of attacking shadow around the (i,j)
			
			// for the all scouts
			if ( DrawingPanel.N >= 1 && DrawingPanel.N <= 6){
				showScoutRange(i,j,DrawingPanel.N, n, g2);
				showTankRange(i,j, DrawingPanel.N + 12, n, g2);
				showSniperRange(i,j,DrawingPanel.N + 6, n, g2);

			}
			
			// for the all snipers
			else if ( DrawingPanel.N >= 7 && DrawingPanel.N <= 12){
				showSniperRange(i,j,DrawingPanel.N, n, g2);
				showTankRange(i,j, DrawingPanel.N + 6, n, g2);
				showScoutRange(i,j,DrawingPanel.N - 6, n, g2);
				
			}
			

			// for the all tanks
			else if ( DrawingPanel.N >= 13 && DrawingPanel.N <= 18){
				showTankRange(i,j, DrawingPanel.N, n, g2);
				showScoutRange(i,j,DrawingPanel.N - 12, n, g2);
				showSniperRange(i,j,DrawingPanel.N - 6, n, g2);	
				
			}
			else;
	
			
			// highlight the range of walk
			if ((i <= DrawingPanel.p_old[DrawingPanel.N].x + 1) && (i >= DrawingPanel.p_old[DrawingPanel.N].x - 1) && (j >= DrawingPanel.p_old[DrawingPanel.N].y - 1) && (j <= DrawingPanel.p_old[DrawingPanel.N].y + 1)  ){
				g2.setColor(DrawingPanel.COLOURGREEN);
				g2.drawPolygon(hex(x,y));
			}
			
			if ((DrawingPanel.p_old[DrawingPanel.N].y % 2) == 0){
				if((i == DrawingPanel.p_old[DrawingPanel.N].x + 1) && ((j == DrawingPanel.p_old[DrawingPanel.N].y - 1) || (j == DrawingPanel.p_old[DrawingPanel.N].y + 1)) ){
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
				}
			}
			else{
				if((i == DrawingPanel.p_old[DrawingPanel.N].x - 1) && ((j == DrawingPanel.p_old[DrawingPanel.N].y - 1) || (j == DrawingPanel.p_old[DrawingPanel.N].y + 1)) ){
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
				}
			}
			
			// highlight the current robot 
			if (n == DrawingPanel.N && (n != 0)) {
				g2.setColor(DrawingPanel.COLOURGRAY);
				g2.fillPolygon(hex(x,y));
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));
				//g2.drawImage(img[n], x+r, y+r, 40, 40, null);
			}
			
			

		// add the image of picture		
		if((n>=1) && (n<=6) &&  (DrawingPanel.N == n || DrawingPanel.N == n+6 || DrawingPanel.N == n+12 || DrawingPanel.N == 0)){
			if((GUI.robotList.getElementAt(n).alive())){
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));
				g2.drawImage(img[n], x+r, y+r, 40, 40, null);
			}
		}
		
		else if((n>=7) && (n<=12) &&  (DrawingPanel.N == n || DrawingPanel.N == n+6 || DrawingPanel.N == n-12 || DrawingPanel.N == 0)){
			if((GUI.robotList.getElementAt(n).alive())){
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));
				g2.drawImage(img[n], x+r, y+r, 40, 40, null);

			}
		}
		else if((n>=13) && (n<=18) &&  (DrawingPanel.N == n || DrawingPanel.N == n-6 || DrawingPanel.N == n-12 || DrawingPanel.N == 0)){
			if((GUI.robotList.getElementAt(n).alive())){
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));
				g2.drawImage(img[n], x+r, y+r, 40, 40, null);

			}
		}
		else;
			
	
	}

	//This function changes pixel location from a mouse click to a hex grid location
/*****************************************************************************
* Name: pxtoHex (pixel to hex)
* Parameters: mx, my. These are the coordinates of mouse click.
* Returns: point. A point containing the coordinates of the hex that is clicked in.
           If the point clicked is not a valid hex (ie. on the borders of the board, (-1,-1) is returned.
* Function: This works for hexes in the Pointy orientation. 
*****************************************************************************/
	public static Point pxtoHex(int mx, int my) {
		Point p = new Point(-1,-1);

		//correction for BORDERS and XYVertex
		mx -= BORDERS;
		my -= BORDERS;
		if (XYVertex) mx += r;

		// get array coordinate(i,j) for pointy
		int y = (int)(my/(s+t)); //j
		int x = (int)((mx-(y%2)*r)/(2*r)); //i

		/******FIX for clicking in the triangle spaces (on the left side only)*******/
		//dx,dy are the number of pixels from the hex rectangular boundary. (ie. relative to the hex clicked in)
		
		int dy = my - y*(s+t);
		int dx = mx - x*2*r;
		
		if (mx - (y%2)*r < 0) return p; // prevent clicking in the open halfhexes at the top of the screen
					
		// even rows 
		if(y%2 == 0){
			if (dx > r) {	//right half of hexes
				if (dy * r /t < dx - r) {
					y--;
				}
			}
			if (dx < r) {	//left half of hexes
				if ((r - dx)*t/r > dy ) {
					x--;
					y--;
				}
			}
		}
		
		// odd rows 
		else{
			if (dx > 2*r) {	//right half of hexes
				if (dy * r /t < dx - 2*r) {
					x++;
					y--;
				}
			}
			if (dx < 2*r) {	//left half of hexes
				if ((r - dx + r)*t/r > dy ) {
					y--;
				}
			}
		}		
		p.x=x;
		p.y=y;
		return p;
	}
	
	/*
	 * to make the cells in the range of current scout white
	 * We can see the all things in this range
	 */
	public static void showScoutRange( int i, int j, int rN,int n, Graphics2D g2){
		int x = i*2*r + (j%2) * r;
		int y = j*(s+t);
		if ((GUI.robotList.getElementAt(rN).alive())){
			if ((i <= DrawingPanel.p_old[rN].x + 2) && (i >= DrawingPanel.p_old[rN].x - 2) && (j >= DrawingPanel.p_old[rN].y - 2) && (j <= DrawingPanel.p_old[rN].y + 2)  ){
				if(DrawingPanel.N == rN){
					g2.setColor(DrawingPanel.COLOURSHOOT);
				}
				else 
					g2.setColor(DrawingPanel.COLOURSHADOW);
				g2.fillPolygon(hex(x,y));
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));
				
				if(n>0 && (GUI.robotList.getElementAt(n).alive()) ){
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
					g2.drawImage(img[n], x+r, y+r, 40, 40, null);
				}
				if(checkOutofScoutRange(i,j,rN) && checkOutofSniperRange(i,j,rN+6) && checkOutofTankRange(i,j,rN+12)){
					g2.setColor(DrawingPanel.COLOURCELL);
					g2.fillPolygon(hex(x,y));
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
				}
				
			}
			else;
			
		}
		
	}// end of showScoutRange
	
	
	
	
	/*
	 * to make the cells in the range of current sniper white
	 * We can see the all things in this range
	 */
	public static void showSniperRange( int i, int j, int rN,int n, Graphics2D g2){
			int x = i*2*r + (j%2) * r;
			int y = j*(s+t);
	
			if ((GUI.robotList.getElementAt(rN).alive())){
			if ((i <= DrawingPanel.p_old[rN].x + 3) && (i >= DrawingPanel.p_old[rN].x - 3) && (j >= DrawingPanel.p_old[rN].y - 3) && (j <= DrawingPanel.p_old[rN].y + 3)  ){
				if(DrawingPanel.N == rN){
					g2.setColor(DrawingPanel.COLOURSHOOT);
				}
				else 
					g2.setColor(DrawingPanel.COLOURSHADOW);
				g2.fillPolygon(hex(x,y));
				g2.setColor(DrawingPanel.COLOURGRID);
				g2.drawPolygon(hex(x,y));

				if(n > 0 && (GUI.robotList.getElementAt(n).alive())){
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
					g2.drawImage(img[n], x+r, y+r, 40, 40, null);
				}	
				
				if(checkOutofScoutRange(i,j,rN-6) &&checkOutofSniperRange(i,j,rN) && checkOutofTankRange(i,j,rN+6)){

					g2.setColor(DrawingPanel.COLOURCELL);
					g2.fillPolygon(hex(x,y));
					g2.setColor(DrawingPanel.COLOURGRID);
					g2.drawPolygon(hex(x,y));
				}
				
			}
			else;
			}

		
	} // end of showSniperRange
	
	
	
	/*
	 * to make the cells in the range of current tank white
	 * We can see the all things in this range
	 */
	public static void showTankRange( int i, int j, int rN, int n, Graphics2D g2){
				int x = i*2*r + (j%2) * r;
				int y = j*(s+t);

				// for the all tanks
				    if ((GUI.robotList.getElementAt(rN).alive())){
					if ((i <= DrawingPanel.p_old[rN].x + 1) && (i >= DrawingPanel.p_old[rN].x - 1) && (j >= DrawingPanel.p_old[rN].y - 1) && (j <= DrawingPanel.p_old[rN].y + 1)  ){
						
						if(DrawingPanel.N == rN){
							g2.setColor(DrawingPanel.COLOURSHOOT);
						}
						else 
							g2.setColor(DrawingPanel.COLOURSHADOW);
						g2.fillPolygon(hex(x,y));
						g2.setColor(DrawingPanel.COLOURGRID);
						g2.drawPolygon(hex(x,y));
						
						if(n > 0 && (GUI.robotList.getElementAt(n).alive())){
							g2.setColor(DrawingPanel.COLOURGRID);
							g2.drawPolygon(hex(x,y));
							g2.drawImage(img[n], x+r, y+r, 40, 40, null);
						}
						
						if(checkOutofScoutRange(i,j,rN-12) && checkOutofSniperRange(i,j,rN-6) && checkOutofTankRange(i,j,rN)){
							g2.setColor(DrawingPanel.COLOURCELL);
							g2.fillPolygon(hex(x,y));
							g2.setColor(DrawingPanel.COLOURGRID);
							g2.drawPolygon(hex(x,y));
						}
					}
					else;
				    }
				} // end of showRange of tank
	
	
	
	// helper function:
	
	/*
	 * to check whether the given coordinates is out of the shooting range of corresponding index of tank
	 */
	public static boolean checkOutofTankRange(int i, int j, int rN){
		if ((GUI.robotList.getElementAt(rN).alive())){
		if ((i <= DrawingPanel.p_old[rN].x + 1) && (i >= DrawingPanel.p_old[rN].x - 1) && (j >= DrawingPanel.p_old[rN].y - 1) && (j <= DrawingPanel.p_old[rN].y + 1)  ){
			if ((DrawingPanel.p_old[rN].y % 2) == 0){
				if((i == DrawingPanel.p_old[rN].x + 1) && ((j == DrawingPanel.p_old[rN].y - 1) || (j == DrawingPanel.p_old[rN].y + 1)) ){
					return true;
				}
				else 
					return false;
			}
			else{
				if((i == DrawingPanel.p_old[rN].x - 1) && ((j == DrawingPanel.p_old[rN].y - 1) || (j == DrawingPanel.p_old[rN].y + 1)) ){
					return true;
				}
				else
					return false;
			}

		}
		else 
			return true;
		}
		else 
			return true;
		
	} // end of checkOutofTankRange
	
	
	/*
	 * to check whether the given coordinates is out of the shooting range of corresponding index of sniper
	 */
	public static boolean checkOutofSniperRange(int i, int j, int rN){
		if ((GUI.robotList.getElementAt(rN).alive())){
		if ((i <= DrawingPanel.p_old[rN].x + 3) && (i >= DrawingPanel.p_old[rN].x - 3) && (j >= DrawingPanel.p_old[rN].y - 3) && (j <= DrawingPanel.p_old[rN].y + 3)  ){
			if ((DrawingPanel.p_old[rN].y % 2) == 0){
				if((i == DrawingPanel.p_old[rN].x + 3) && ((j >= DrawingPanel.p_old[rN].y - 3) && (j <= DrawingPanel.p_old[rN].y + 3) && (j != DrawingPanel.p_old[rN].y)) ){
					return true;
				}
				else if(((i == DrawingPanel.p_old[rN].x + 2)) && ((j == DrawingPanel.p_old[rN].y - 3) || (j == DrawingPanel.p_old[rN].y + 3) ) ){
					return true;
				}
				
				else if(((i == DrawingPanel.p_old[rN].x - 3)) && ((j == DrawingPanel.p_old[rN].y - 3) || (j == DrawingPanel.p_old[rN].y + 3) || (j == DrawingPanel.p_old[rN].y - 2) || (j == DrawingPanel.p_old[rN].y + 2) ) ){
					return true;
				}
				else 
					return false;
			}
			else{
				if((i == DrawingPanel.p_old[rN].x - 3) && ((j >= DrawingPanel.p_old[rN].y - 3) && (j <= DrawingPanel.p_old[rN].y + 3) && (j != DrawingPanel.p_old[rN].y)) ){
					return true;
				}
				else if(((i == DrawingPanel.p_old[rN].x - 2)) && ((j == DrawingPanel.p_old[rN].y - 3) || (j == DrawingPanel.p_old[rN].y + 3) ) ){
					return true;
				}
				
				else if(((i == DrawingPanel.p_old[rN].x + 3)) && ((j == DrawingPanel.p_old[rN].y - 3) || (j == DrawingPanel.p_old[rN].y + 3) || (j == DrawingPanel.p_old[rN].y + 2)|| (j == DrawingPanel.p_old[rN].y - 2))){
					return true;
				}
				else 
					return false;
			}
			
		}
		else
			return true;
		}
		else 
			return true;
		
	} // end of checkoutofSniperrange
	
	/*
	 * to check whether the given coordinates is out of the shooting range of corresponding index of scout
	 */
	public static boolean checkOutofScoutRange(int i, int j, int rN){
		if ((GUI.robotList.getElementAt(rN).alive())){
		if ((i <= DrawingPanel.p_old[rN].x + 2) && (i >= DrawingPanel.p_old[rN].x - 2) && (j >= DrawingPanel.p_old[rN].y - 2) && (j <= DrawingPanel.p_old[rN].y + 2)){
			if ((DrawingPanel.p_old[rN].y % 2) == 0){
				if((i == DrawingPanel.p_old[rN].x + 2) && ((j >= DrawingPanel.p_old[rN].y - 2) && (j <= DrawingPanel.p_old[rN].y + 2) && (j != DrawingPanel.p_old[rN].y)) ){
					return true;
				}
				else if((i == DrawingPanel.p_old[rN].x - 2) && ((j == DrawingPanel.p_old[rN].y - 2) || (j == DrawingPanel.p_old[rN].y + 2)) ){
					return true;
				}
				else 
					return false;
			}
			else{
				if((i == DrawingPanel.p_old[rN].x - 2) && ((j >= DrawingPanel.p_old[rN].y - 2) && (j <= DrawingPanel.p_old[rN].y + 2) && (j != DrawingPanel.p_old[rN].y)) ){
					return true;
				}
				else if((i == DrawingPanel.p_old[rN].x + 2) && ((j == DrawingPanel.p_old[rN].y - 2) || (j == DrawingPanel.p_old[rN].y + 2)) ){
					return true;
				}
				else 
					return false;
			}
		}
		else 
			return true;
		}
		else 
			return true;
	} // end of checkOutofScoutrange
	
		
}// end of the class