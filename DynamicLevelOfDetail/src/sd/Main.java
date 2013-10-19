package sd;

import java.util.concurrent.ScheduledThreadPoolExecutor;



import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setHeight(768);
		settings.setWidth(1024);
		Main m = new Main();
		m.setShowSettings(false);
		m.setDisplayStatView(false);
		m.setSettings(settings);
		m.start();

	}
private Material mat;
private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(12);

private Quad q1, q2, q3, q4;
private String path = "MatTest.j3md";
private float size = 1024;
private float ftexSize = 256;
private int itexSize = (int)ftexSize;
	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(275);
		cam.setLocation(new Vector3f(0,800,0));
		cam.setFrustumFar(10000);
		Vector3f v1 = new Vector3f(0,0,0);
		Vector3f v2 = new Vector3f(0,0,1024);
		Vector3f v3 = new Vector3f(1024,0,1024);
		Vector3f v4 = new Vector3f(1024,0,0);
		BoundingSphere sp = new BoundingSphere();
		
	    Quad q = new Quad(Quad.faces.TOP.toString(),null,v1,v2,v3,v4,1024,0,0,false,null,null,true,null,true,null,null, new Vector2f(0,0),0);
	    sp.setRadius(1024/2);
		sp.setCenter(Quad.getCenter(q));
		q.sphere = sp;
		q.leaves = Quad.addVertices(q);
		q.mesh = Quad.createMesh(q);
		Quad.list.add(q);
		Geometry geo = q.mesh;
		mat = new Material(assetManager, "MatTest.j3md");
		mat.setVector4("Color", new Vector4f(1.0f,0.0f,0.0f,1.0f));
		Heightmap.initialize();
		mat.setTexture("Heightmap", Heightmap.createHeightmap(itexSize, new Vector2f(0,0), 1f, q));
		mat.setVector2("Offset", q.heightmapOffset);
		mat.setFloat("size", q.width);
		mat.getAdditionalRenderState().setWireframe(false);
		geo.setMaterial(mat);
		rootNode.attachChild(geo);

	}
	public void simpleUpdate(float tpf){

		 for(int i = 0; i<Quad.list.size(); i++){
		    	Quad q = Quad.list.get(i);
		    	Vector3f center = Quad.getCenter(q); // this simply averages the four corners of the quad
		    	float width = q.width; // "width" is the size of one of the quads sides
		    	float distance = center.distance(cam.getLocation()); // this gets the distance from the center of the quad and the camera
		    	float view = (1.5f * width); // this creates the scale distance
		    	// if children = true the quad has children being displayed on screen
		    	//q.childs is the arraylist holding the quad's children 
		    	
		    if(view >= distance && !q.children && q.width > (8) ){
	    	      	    	    	 
		    	Quad.split(q,q.index1,q.index2);
   			     q1 = q.childs.get(0);
		    	 q2 = q.childs.get(1);
		    	 q3 = q.childs.get(2);
		    	 q4 = q.childs.get(3);
		    	float scale = q1.width/size;
			    q1.mesh.setMaterial(makeMaterial(path));
			    q1.mesh.getMaterial().setVector4("Color", new Vector4f(1, 1,1,1f));
			    q1.mesh.getMaterial().setFloat("size", q1.width);
			    q1.mesh.getMaterial().setTexture("Heightmap",Heightmap.createHeightmap(itexSize, new Vector2f(q1.index1 * (scale * ftexSize), q1.index2 * (scale * ftexSize)), scale, q1));
			    q1.mesh.getMaterial().setVector2("Offset", q1.heightmapOffset);
			    
			//  System.out.println(q1.heightmapOffset + " "+q1.width + " " + q1.third);

			   q2.mesh.setMaterial(makeMaterial(path));
			   q2.mesh.getMaterial().setVector4("Color", new Vector4f(1, 0,0,1f));    
			   q2.mesh.getMaterial().setFloat("size", q2.width);
			   q2.mesh.getMaterial().setTexture("Heightmap",Heightmap.createHeightmap(itexSize, new Vector2f(q2.index1 * (scale * ftexSize), q2.index2 * (scale * ftexSize)), scale, q2));
			   q2.mesh.getMaterial().setVector2("Offset", q2.heightmapOffset);
			    			   			   
			   q3.mesh.setMaterial(makeMaterial(path));
			   q3.mesh.getMaterial().setVector4("Color", new Vector4f(0f, 0,1f,1f));
			   q3.mesh.getMaterial().setFloat("size", q3.width);
			   q3.mesh.getMaterial().setTexture("Heightmap", Heightmap.createHeightmap(itexSize, new Vector2f(q3.index1 * (scale * ftexSize), q3.index2 * (scale * ftexSize)), scale, q3));
			   q3.mesh.getMaterial().setVector2("Offset", q3.heightmapOffset);
			   
//			 System.out.println(q3.leaves[46] + "  "+q3.index1 + " "+q3.index2);
//			 System.out.println(new Vector2f(q3.realIndex.x * q3.width, q3.realIndex.y * q3.width));
//			 System.out.println((q3.leaves[46].x - (q3.realIndex.x * q3.width))/q3.width + ", "+(q3.leaves[46].z - (q3.realIndex.y * q3.width))/q3.width);
			    
			   q4.mesh.setMaterial(makeMaterial(path));
			   q4.mesh.getMaterial().setVector4("Color", new Vector4f(0,1f,0,1f));	    
			   q4.mesh.getMaterial().setFloat("size", q4.width);
			   q4.mesh.getMaterial().setTexture("Heightmap", Heightmap.createHeightmap(itexSize, new Vector2f(q4.index1 * (scale * ftexSize), q4.index2 * (scale * ftexSize)), scale, q4));
			   q4.mesh.getMaterial().setVector2("Offset", q4.heightmapOffset);
			    
			    rootNode.attachChild(q1.mesh);
			    rootNode.attachChild(q2.mesh);
			    rootNode.attachChild(q3.mesh);
			    rootNode.attachChild(q4.mesh);
			    
			    rootNode.detachChild(q.mesh); 	    	    	   

		    } 
		    else if(view < distance && q.children && q.parent !=null){
	            Quad.list.removeAll(q.childs);
		    	Quad.list.add(q.parent);
		    	rootNode.detachChild(q.childs.get(0).mesh);
		    	rootNode.detachChild(q.childs.get(1).mesh);
		    	rootNode.detachChild(q.childs.get(2).mesh);
		    	rootNode.detachChild(q.childs.get(3).mesh);
		    	q.childs.clear();
		    	q.receiving = true;
		    	q.children = false;
		    	rootNode.attachChild(q.mesh);
		    } 
		    } 
				   
	}	
	
	
	public boolean inFrustum(Quad q){
		FrustumIntersect s = cam.contains(q.sphere);
		if(s.equals(FrustumIntersect.Inside) || s.equals(FrustumIntersect.Intersects)){
			return true;
		}else{return false;}
	}
	
	private Material makeMaterial(String p){
		Material mat = new Material(assetManager, p);
		mat.getAdditionalRenderState().setWireframe(false);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		return mat;
	}
	
	public void destroy() {
        super.destroy();
        executor.shutdown();
	}
}
