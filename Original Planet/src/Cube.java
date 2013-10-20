import java.util.ArrayList;
import java.util.Random;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3f;

public class Cube{
	private BranchGroup bg;
	private static float radius = 40f;
	private static float length = 2;
	static float newHeight, newHeight2;
	private Random r = new Random();
	private double subDivision = 512;
	private double increment = length * (1/subDivision);
public Cube(){
	System.out.println("Meg used = "+(Runtime.getRuntime().totalMemory()-
			Runtime.getRuntime().freeMemory())/(1000*1000)+"M");
	int vert_Length;
	int fix = 0;
	ArrayList<Point3f> pointList = new ArrayList<Point3f>();
	Point3f[] vertices;
	Point3f zero = new Point3f(0,0,0);
	Point3f one = new Point3f(length,0,0);
	Point3f two = new Point3f(length,length,0);
	Point3f three = new Point3f(0,length,0);
	Point3f four = new Point3f(0,0,length);
	Point3f five = new Point3f(length,0,length);
	Point3f six = new Point3f(length,length,length);
	Point3f seven = new Point3f (0,length,length);
	 ArrayList<Point3f> zo = new ArrayList<Point3f>();
     zo = subDivideLine(zero,one);
     Point3f[] zoA = new Point3f[zo.size()];
     zo.toArray(zoA);
     ArrayList<Point3f> ot = new ArrayList<Point3f>();
     ot = subDivideLine(one,two);
     Point3f[] otA = new Point3f[ot.size()];
     ot.toArray(otA);
     ArrayList<Point3f> tt = new ArrayList<Point3f>();
     tt = subDivideLine(three,two);
     Point3f[] ttA = new Point3f[tt.size()];
     tt.toArray(ttA);
     ArrayList<Point3f> tz = new ArrayList<Point3f>();
     tz = subDivideLine(zero,three);
     Point3f[] tzA = new Point3f[tz.size()];
     tz.toArray(tzA);
     ArrayList<Point3f> zf = new ArrayList<Point3f>();
     zf = subDivideLine(zero,four);
     Point3f[] zfA = new Point3f[zo.size()];
     zf.toArray(zfA);
     ArrayList<Point3f> ff = new ArrayList<Point3f>();
     ff = subDivideLine(four,five);
     Point3f[] ffA = new Point3f[ot.size()];
     ff.toArray(ffA);
     ArrayList<Point3f> fo = new ArrayList<Point3f>();
     fo = subDivideLine(one,five);
     Point3f[] foA = new Point3f[tt.size()];
     fo.toArray(foA);
     ArrayList<Point3f> fs = new ArrayList<Point3f>();
     fs = subDivideLine(five,six);
     Point3f[] fsA = new Point3f[ot.size()];
     fs.toArray(fsA);
     ArrayList<Point3f> tsev = new ArrayList<Point3f>();
     tsev = subDivideLine(three,seven);
     Point3f[] tsevA = new Point3f[zo.size()];
     tsev.toArray(tsevA);
     
     System.out.println(zoA.length);
    float y1 = (float) increment;
     float y2 = (float) (increment * 2);
	 y1 =  (float) (-1 * (increment));
	 for(int n = 0; n<subDivision;n++){
	 		float x1 = 0;
	 		float x2 = (float)increment;
	 	float yVal =  (float) (y1 + (increment));
	 	//back
	 		Point3f firstX = new Point3f(x1, yVal,zero.z );
	 		Point3f secondX = new Point3f(x2, yVal,zero.z);
	 		Point3f third = new Point3f(secondX.x,(float) (yVal+(increment)),zero.z);
	 		Point3f fourth = new Point3f(firstX.x, third.y, zero.z);		
	 		pointList.add(firstX);pointList.add(secondX);pointList.add(third);pointList.add(fourth);
	 		//System.out.println(firstX+""+secondX+""+third+""+fourth);
	 		Point3f firstX_Last = new Point3f(zoA[zoA.length-2].x, yVal, zero.z);
	 		Point3f secondX_Last = new Point3f(zoA[zoA.length-1].x,yVal,zero.z);
	 		Point3f thirdX_Last = new Point3f(secondX_Last.x, (float) (yVal+increment), zero.z);
	 		Point3f fourthX_Last = new Point3f(firstX_Last.x, (float) (yVal+increment),zero.z);
	 		pointList.add(firstX_Last);pointList.add(secondX_Last);pointList.add(thirdX_Last);pointList.add(fourthX_Last);
	 		//System.out.println(firstX_Last+""+secondX_Last+""+thirdX_Last+""+fourthX_Last);
	 		
	 		//front 
	 		Point3f firstX_front = new Point3f(x1,yVal,firstX.z+length);
	 		Point3f secondX_front = new Point3f(x2,yVal,secondX.z+length);
	 		Point3f thirdX_front = new Point3f(secondX_front.x,(float) (yVal+(increment)),third.z+length);
	 		Point3f fourthX_front = new Point3f(firstX_front.x,(float) (yVal+(increment)),fourth.z+length);
	 		pointList.add(firstX_front);pointList.add(secondX_front);pointList.add(thirdX_front);pointList.add(fourthX_front);
	 		
	 		Point3f firstXfront_Last = new Point3f(ffA[ffA.length-2].x, yVal, firstX_Last.z+length);
	 		Point3f secondXfront_Last = new Point3f(ffA[ffA.length-1].x,yVal,secondX_Last.z+length);
	 		Point3f thirdXfront_Last = new Point3f(secondXfront_Last.x, (float) (yVal+increment), thirdX_Last.z+length);
	 		Point3f fourthXfront_Last = new Point3f(firstXfront_Last.x, (float) (yVal+increment),fourthX_Last.z+length);
	 		pointList.add(firstXfront_Last);pointList.add(secondXfront_Last);pointList.add(thirdXfront_Last);pointList.add(fourthXfront_Last);
	 		
	 		//right
	 		Point3f firstX_right = new Point3f(length,yVal,x1);
	 		Point3f secondX_right = new Point3f(length,yVal,x2);
	 		Point3f thirdX_right = new Point3f(length,(float) (yVal+increment),secondX_right.z);
	 		Point3f fourthX_right = new Point3f(length,(float) (yVal+increment),firstX_right.z);
	 		pointList.add(firstX_right);pointList.add(secondX_right);pointList.add(thirdX_right);pointList.add(fourthX_right);
	 		
	 		Point3f firstlastX_right = new Point3f(length,yVal,foA[foA.length-2].z);
	 		Point3f secondlastX_right = new Point3f(length,yVal,foA[foA.length-1].z);
	 		Point3f thirdlastX_right = new Point3f(length,(float) (yVal+increment),secondlastX_right.z);
	 		Point3f fourthlastX_right = new Point3f(length,(float) (yVal+increment),firstlastX_right.z);
	 		pointList.add(firstlastX_right);pointList.add(secondlastX_right);pointList.add(thirdlastX_right);pointList.add(fourthlastX_right);
	 		y1 = yVal;
	 		
	 		//left
	 		Point3f firstX_left = new Point3f(0,yVal,x1);
	 		Point3f secondX_left = new Point3f(0,yVal,x2);
	 		Point3f thirdX_left = new Point3f(0,(float) (yVal+increment),secondX_left.z);
	 		Point3f fourthX_left = new Point3f(0,(float) (yVal+increment),firstX_left.z);
	 		pointList.add(firstX_left);pointList.add(secondX_left);pointList.add(thirdX_left);pointList.add(fourthX_left);
	 		Point3f firstlastX_left = new Point3f(0,yVal,zfA[zfA.length-2].z);
	 		Point3f secondlastX_left = new Point3f(0,yVal,zfA[zfA.length-1].z);
	 		Point3f thirdlastX_left = new Point3f(0,(float) (yVal+increment),secondlastX_left.z);
	 		Point3f fourthlastX_left = new Point3f(0,(float) (yVal+increment),firstlastX_left.z);
	 		pointList.add(firstlastX_left);pointList.add(secondlastX_left);pointList.add(thirdlastX_left);pointList.add(fourthlastX_left);
	 		//top
	 		
	 		Point3f firstX_top = new Point3f(yVal,length,x1);
	 		Point3f secondX_top = new Point3f(yVal,length,x2);
	 		Point3f thirdX_top = new Point3f((float) (yVal+increment), length,secondX_top.z);
	 		Point3f fourthX_top = new Point3f((float) (yVal+increment), length,firstX_top.z);
	 		pointList.add(firstX_top);pointList.add(secondX_top);pointList.add(thirdX_top);pointList.add(fourthX_top);
	 		
	 		Point3f firstlastX_top = new Point3f(yVal,length,tsevA[tsevA.length-2].z);
	 		Point3f secondlastX_top = new Point3f(yVal,length,tsevA[tsevA.length-1].z);
	 		Point3f thirdlastX_top = new Point3f((float) (yVal+increment),length,secondlastX_top.z);
	 		Point3f fourthlastX_top = new Point3f((float) (yVal+increment),length,firstlastX_top.z);
	 		pointList.add(firstlastX_top);pointList.add(secondlastX_top);pointList.add(thirdlastX_top);pointList.add(fourthlastX_top);
	 		//bottom
	 		Point3f firstX_bottom = new Point3f(yVal,0,x1);
	 		Point3f secondX_bottom = new Point3f(yVal,0,x2);
	 		Point3f thirdX_bottom = new Point3f((float) (yVal+increment), 0,secondX_bottom.z);
	 		Point3f fourthX_bottom = new Point3f((float) (yVal+increment), 0,firstX_bottom.z);
	 		pointList.add(firstX_bottom);pointList.add(secondX_bottom);pointList.add(thirdX_bottom);pointList.add(fourthX_bottom);
	 		
	 		Point3f firstlastX_bottom = new Point3f(yVal,0,zfA[zfA.length-2].z);
	 		Point3f secondlastX_bottom = new Point3f(yVal,0,zfA[zfA.length-1].z);
	 		Point3f thirdlastX_bottom = new Point3f((float) (yVal+increment),0,secondlastX_bottom.z);
	 		Point3f fourthlastX_bottom = new Point3f((float) (yVal+increment),0,firstlastX_bottom.z);
	 		pointList.add(firstlastX_bottom);pointList.add(secondlastX_bottom);pointList.add(thirdlastX_bottom);pointList.add(fourthlastX_bottom);
	 		y1 = yVal;
	 	}
	 int iterations = 0;

	 for(int ii = 0; ii<subDivision-2; ii++){
	 for(int n = 0; n<subDivision; n++){
	 	if(ii > 0 || n > 0 ){
	 		fix = 1;
	 	}	
	 	if(iterations%subDivision==0){
	 		y1 = 0;
	 		y2 = 0;
	 		fix = 0;
	 	}
	 	iterations++;
	 	float ySPre = (float) (y1+(increment*fix));
	 	float yEPre = (float) (y2+(increment*fix));
	 Point3f startX_back = new Point3f(zoA[ii].x, ySPre, zoA[ii].z);
	 Point3f endX_back = new Point3f(zoA[ii+1].x, yEPre, zoA[ii+1].z);
	 Point3f startX_front = new Point3f(ffA[ii].x,ySPre,startX_back.z+length);
	 Point3f endX_front = new Point3f(ffA[ii+1].x,yEPre,endX_back.z+length);
	 Point3f startX_bottom = new Point3f(ySPre,0,zfA[ii].z);
	 Point3f endX_bottom = new Point3f(yEPre,0,zfA[ii+1].z);
	 Point3f startX_top = new Point3f(ySPre,length,tsevA[ii].z);
	 Point3f endX_top = new Point3f(yEPre,length,tsevA[ii+1].z);
	 Point3f startX_right = new Point3f(length,ySPre,foA[ii].z);
	 Point3f endX_right = new Point3f(length,yEPre,foA[ii+1].z);
	 Point3f startX_left = new Point3f(0,ySPre,zfA[ii].z);
	 Point3f endX_left = new Point3f(0,yEPre,zfA[ii+1].z);
	 y1 = ySPre;
	 y2 = yEPre;
	 //back
	 pointList.add(startX_back);pointList.add(endX_back);pointList.add(new Point3f((endX_back.x), tzA[n].y, tzA[n].z)); pointList.add(new Point3f((startX_back.x), tzA[n].y, tzA[n].z));
	 //front
	 pointList.add(startX_front);pointList.add(endX_front);pointList.add(new Point3f((endX_front.x), fsA[n].y, length)); pointList.add(new Point3f((startX_back.x), fsA[n].y, fsA[n].z));
	 //bottom
	 pointList.add(startX_bottom);pointList.add(endX_bottom);pointList.add(new Point3f(zoA[n].x,0,endX_bottom.z));pointList.add(new Point3f(zoA[n].x,0,startX_bottom.z));
	 //top
	 pointList.add(startX_top);pointList.add(endX_top);pointList.add(new Point3f(ttA[n].x,length,endX_top.z));pointList.add(new Point3f(ttA[n].x,length,startX_top.z));
	 //right
	 pointList.add(startX_right);pointList.add(endX_right);pointList.add(new Point3f(length,otA[n].y,endX_right.z));pointList.add(new Point3f(length,otA[n].y,startX_right.z));
	 //left
	 pointList.add(startX_left);pointList.add(endX_left);pointList.add(new Point3f(0,tzA[n].y,endX_left.z));pointList.add(new Point3f(0,tzA[n].y,startX_left.z));
	 //System.out.println(startX+""+endX+""+new Point3f((float) (endX.x), tzA[n].y, tzA[n].z) + ""+new Point3f((float) (startX.x), tzA[n].y, tzA[n].z));
	 }
	 }
	
      vertices = new Point3f[pointList.size()];
      pointList.toArray(vertices);
      float[] yCompare = new float[vertices.length];
      Long seed = 365693607443004017l;
      NoiseTest.initialize(seed);
      for(int i = 0; i<vertices.length;i++){
    	  Point3f centered = originCenter(vertices[i]);
    	  vertices[i] = centered;
    	  float newX = (float) (vertices[i].x * (Math.sqrt(1-((vertices[i].y*vertices[i].y)/2)-((vertices[i].z*vertices[i].z)/2)+(((vertices[i].y*vertices[i].y)*(vertices[i].z*vertices[i].z))/3))));
    	  float newY = (float) (vertices[i].y * (Math.sqrt(1-((vertices[i].x*vertices[i].x)/2)-((vertices[i].z*vertices[i].z)/2)+(((vertices[i].x*vertices[i].x)*(vertices[i].z*vertices[i].z))/3))));
    	  float newZ = (float) (vertices[i].z * (Math.sqrt(1-((vertices[i].y*vertices[i].y)/2)-((vertices[i].x*vertices[i].x)/2)+(((vertices[i].y*vertices[i].y)*(vertices[i].x*vertices[i].x))/3))));
    	  newX +=length/2;
    	  newY +=length/2;
    	  newZ +=length/2;
    	  
    	  vertices[i].x = (newX*radius);
    	  vertices[i].y = (newY*radius);
    	  vertices[i].z = (newZ*radius);
    	  
    }   
   //   Long seed = r.nextLong();
  System.out.println(seed);
Point3f sphereCenter = new Point3f(radius,radius,radius);
Point3f newPoint;
for(int i = 0; i<vertices.length;i++){
float noise1 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 10, (float) .01,1, seed);
float noise2 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 5, (float) .07,1, seed);
float noise3 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 8, (float) .1,1, seed);
float noise = noise1 + noise2 + noise3;
float cont_1 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 10, (float) .05,1, seed+400);
float cont_3 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 8, (float) .4,1, seed+400);
float cont_2 = NoiseTest.calculateNoise(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius, 6, (float) .75,1, seed+400);
float cont = cont_1 + cont_2 + cont_3 ;
float amount = .2f;
	Point3f mountain_point = NoiseTest.displace(((vertices[i].x)/radius),((vertices[i].y)/radius),((vertices[i].z)/radius) , seed+200);
	  newHeight = ((float)NoiseTest.mountains(mountain_point));
	  newHeight2 =  ((float)NoiseTest.plains(vertices[i].x/radius, vertices[i].y/radius, vertices[i].z/radius));
	 newHeight2 = NoiseTest.bias(newHeight2, .32f, -.55f);
float height = (float) NoiseTest.curveDown(newHeight2, newHeight, noise,0,1000f, .143);
float c_len  = 0;
if(height > -.65){
c_len = .15f;
}else if (height > -.75 && height < -.65){
c_len = .095f;
}else{
c_len = .085f;
}
float coast = (float) NoiseTest.curveDown(-.99f, height, cont, amount, 1000, c_len);
if(cont <= amount){
if(cont + c_len > amount)
cont += c_len;
}
if(cont > amount){
if(noise > .7){
	  float xNorm = vertices[i].x - sphereCenter.x;
	  float yNorm =vertices[i].y - sphereCenter.y;
    float zNorm =vertices[i].z - sphereCenter.z;
    float magnitude=(float) Math.sqrt(xNorm*xNorm+yNorm*yNorm+zNorm*zNorm);
    xNorm/=magnitude;
    yNorm/=magnitude;
    zNorm/=magnitude;
    newPoint = new Point3f ((xNorm * (coast+radius)), (yNorm * (coast+radius)), (zNorm * (coast+radius)));
    vertices[i] = newPoint;
    if(coast >= -.99 && coast < -.99f){
  		yCompare[i] = -1f;
      }
  		else{	
  yCompare[i] = coast;
  		}
  		 
    
}else{
float xNorm = vertices[i].x - sphereCenter.x;
  float yNorm =vertices[i].y - sphereCenter.y;
  float zNorm =vertices[i].z - sphereCenter.z;
  float magnitude=(float) Math.sqrt(xNorm*xNorm+yNorm*yNorm+zNorm*zNorm);
  xNorm/=magnitude;
  yNorm/=magnitude;
  zNorm/=magnitude;
  newPoint = new Point3f ((xNorm * (coast+radius)), (yNorm * (coast+radius)), (zNorm * (coast+radius)));
  vertices[i] = newPoint;
  
  if(coast >= -.99 && coast < -.97){
		yCompare[i] = -1;
  }
		else{	
yCompare[i] = coast;
		} 		 
}}else{
float xNorm = vertices[i].x - sphereCenter.x;
  float yNorm =vertices[i].y - sphereCenter.y;
float zNorm =vertices[i].z - sphereCenter.z;
float magnitude=(float) Math.sqrt(xNorm*xNorm+yNorm*yNorm+zNorm*zNorm);
xNorm/=magnitude;
yNorm/=magnitude;
zNorm/=magnitude;
newPoint = new Point3f ((xNorm * (-.99f+radius)), (yNorm * (-.99f+radius)), (zNorm * (-.99f+radius)));
vertices[i] = newPoint;
yCompare[i] = -.99f;
}
}
    vert_Length = (vertices.length);
      System.out.println(vert_Length);
      bg = new BranchGroup();
     bg.addChild(new Render(vertices, yCompare));
      
	}



public ArrayList<Point3f> subDivideLine(Point3f first, Point3f last){
	ArrayList<Point3f> list = new ArrayList<Point3f>();
	float xf = first.x;
	float yf = first.y;
	float zf = first.z;
	
	float xl = last.x;
	float yl = last.y;
	float zl = last.z;
	Point3f newLine;
	if(avg(xf,xl) == avg(length,0)){
		for(int i = 0; i<subDivision; i++){
		
		   float preX = (float) (xf + increment);
			xf = preX;
			newLine = new Point3f (preX, yf, zf );
			list.add(newLine);
		
		}
		return list;
	}
  if(avg(yf,yl) == avg(length,0)){
	  for(int i = 0; i<subDivision; i++){
			if(yf>yl){
				float preY = (float) (yf - increment);
				yf = preY;
				newLine = new Point3f (xf, preY, zf );
				list.add(newLine);
			}else{
				float preY = (float) (yf + increment);
				yf = preY;
				newLine = new Point3f (xf, preY, zf );
				list.add(newLine);
			}
			}
			return list;
  }
	 if(avg(zf,zl) == avg(length,0)){
		 for(int i = 0; i<subDivision; i++){
				if(zf>zl){
					float preZ = (float) (zf - increment);
					zf = preZ;
					newLine = new Point3f (xf, yf, preZ );
					list.add(newLine);
				}else{
					float preZ = (float) (zf + increment);
					zf = preZ;
					newLine = new Point3f (zf, yf, preZ );
					list.add(newLine);
				}
				}
				return list;
	 }
	 else{
		 return list;
	 }
}
public Point3f originCenter(Point3f toCenter){
	float newx;
	float newy;
	float newz;
newx = toCenter.x - length/2;
newz= toCenter.z - length/2;
newy = toCenter.y - length/2;
	                                                                                     
	return new Point3f(newx,newy,newz);
}
public float avg(float x , float y){
	return (x + y)/2;
}
public BranchGroup getBg(){
	System.out.println("RUN");
	return bg;
}
}
