
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;


public class Render extends Shape3D implements KeyListener{

	private static PolygonAttributes polatr = new PolygonAttributes();
	public Render( Point3f[] vertices,  float[] ys){
		
		QuadArray qu= new QuadArray(vertices.length,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		//QuadArray oa = new QuadArray(vertices,QuadArray.COORDINATES | QuadArray.COLOR_3);
		ArrayList<Color3f> colorList = new ArrayList<Color3f>();
		ArrayList<Color3f> oceanList = new ArrayList<Color3f>();
		Color3f white =  new Color3f(1,1,1);
		Color3f gray = new Color3f(.65f,.65f,.65f);
		Color3f green = new Color3f(0,.7f,0);		  
		for(int i  = 0; i<vertices.length; i ++){	
			if(ys[i] == -1){
				colorList.add(new Color3f(.941f,.902f,.549f));
			}
			else if(ys[i] > -.35){
				colorList.add(white);
			} else if(ys[i] <-.35 && ys[i] > -.5){
				colorList.add(gray);
			}
			else if(ys[i] > -.9 && ys[i] < -.5){
				colorList.add(green);
			} else if   (ys[i]< -.9 && ys[i] > -.99) {
				colorList.add(new Color3f(0,.6f,0));
			}
			else{
				colorList.add(new Color3f(0,0,.7f));
			}
		}
	//	(i);
   	 // (i+1);
   	 // (i+2);
	/*	for(int i = 0; i<vertices.length; i+=4){
			Vector3f v1, v2, v3, v4, v5, v6, v7, v8, n1, n2, n3, n4;
			v1 = sub(vertices[i+2] , vertices[i]);
			v2 = sub(vertices[i+1] , vertices[i]);
			v3 = sub(vertices[i] , vertices[i+1]);
			v4 = sub(vertices[i+2] , vertices[i+1]);
			v5 = sub(vertices[i+3] , vertices[i+2]);
			v6 = sub(vertices[i+1] , vertices[i+2]);
			v7 = sub(vertices[i] , vertices[i+3]);
			v8 = sub(vertices[i+2] , vertices[i+3]);
			
			n1 = crossProduct(v1, v2);
			n2 = crossProduct(v3, v4);
			n3 = crossProduct(v6, v5);
			n4 = crossProduct(v8, v7);
			
			qu.setNormal(i , n1);
			qu.setNormal(i+1 , n2);
			qu.setNormal(i+2 , n3);
			qu.setNormal(i+3 , n4);
			
		 } */
	
		Color3f[] colors = new Color3f[colorList.size()];
		Color3f[] oceanColors = new Color3f[oceanList.size()];
		colorList.toArray(colors);
		oceanList.toArray(oceanColors);
		qu.setCoordinates(0, vertices);
		qu.setColors(0,colors);
		GeometryInfo geo = new GeometryInfo(qu);
		NormalGenerator normalGenerator = new NormalGenerator();
		normalGenerator.generateNormals(geo);
	     Stripifier triangle = new Stripifier();
	     triangle.stripify(geo);
	     addGeometry(geo.getGeometryArray());
	    appearance(false);
	}
	

	 public class SimpleBehavior extends Behavior {
	
		    public void initialize() {
		      this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
		    }

		    public void processStimulus(Enumeration criteria) {
		    System.out.println("h");
		      this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
		     
		      }
		    }

		  

	public void appearance(Boolean t){
		Appearance app = new Appearance();
		    polatr.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
			polatr.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ );
			polatr.setCullFace(PolygonAttributes.CULL_NONE);

			polatr.setPolygonMode(PolygonAttributes.POLYGON_FILL);
			
			
		app.setPolygonAttributes(polatr);
	Material mat = new Material();
	Color3f black = new Color3f(0f, 0f, 0f);
	Color3f specular = new Color3f(0.9f, 0.9f, 0.9f); 
	mat.setSpecularColor(specular);
	mat.setDiffuseColor(black);
	mat.setLightingEnable(true);
	app.setMaterial(mat);
	    setAppearance(app);
	    
	}
public Vector3f crossProduct(Vector3f v1, Vector3f v2){
	      float  vx = (v1.y * v2.z) - (v1.z * v2.y);
			
		  float vy = (v1.z * v2.x) - (v1.x * v2.z);
			
	      float vz = (v1.x * v2.y) - (v1.y * v2.x);
	      
	      return new Vector3f (vx, vy, vz);
}

public Vector3f normalize(Vector3f v1){
	float mag = (float) Math.sqrt((v1.x * v1.x) + (v1.y * v1.y) + (v1.z * v1.z));
	float x = v1.x/mag;
	float y = v1.y/mag;
	float z = v1.z/mag;
	return new Vector3f(x,y,z);
}
public Vector3f add(Vector3f v1, Vector3f v2){
	float x1 = v1.x;
	float y1 = v1.y;
	float z1 = v1.z;
	
	float x2 = v2.x;
	float y2 = v2.y;
	float z2 = v2.z;
	
	float x = x1 + x2;
	float y = y1 + y2;
	float z = z1 + z2;
	
	return new Vector3f(x,y,z);
}
public Vector3f sub(Point3f v1, Point3f v2){
	float x1 = v1.x;
	float y1 = v1.y;
	float z1 = v1.z;
	
	float x2 = v2.x;
	float y2 = v2.y;
	float z2 = v2.z;
	
	float x = x1 - x2;
	float y = y1 - y2;
	float z = z1 - z2;
	
	return new Vector3f(x,y,z);
}
	@Override
	public void keyPressed(KeyEvent e) {
		
			System.out.println("Hi");
		
	
	}



	@Override
	public void keyReleased(KeyEvent arg0) {
		System.out.println("Hi");
		
	}



	@Override
	public void keyTyped(KeyEvent arg0) {
		System.out.println("Hi");
		
	}
	
	
	
}
