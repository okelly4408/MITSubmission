package sd;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class Quad{
	public  Vector3f first,second,third,fourth;
	public int index1, index2;
	public  float width;
	public boolean children;
	public ArrayList<Quad> childs;
	public Vector3f[] leaves;
	public Quad parent;
	private static float num_leaves = 8;
	private static float size = 1024f;
	private static float hsize = size/2;
	private static int verticesPerQuad = (int) (num_leaves * num_leaves * 4);
	private static float nhsize = -hsize;
	private static float denom1 = hsize*hsize*2;
	private static float denom2 = hsize*hsize*hsize*hsize*3;
	public static ArrayList<Quad> list = new ArrayList<Quad>();
	public boolean receiving;
	public String face;
	public BoundingSphere sphere;
	public enum faces {TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK};
	public boolean isRoot;
	public int[] indices;
	public Geometry mesh;
	public Vector2f heightmapOffset;
	public float centerOff;
	private static Main ma = new Main();
	
public Quad( String f ,Quad p, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4,float size,int x, int y, boolean has, ArrayList<Quad> child, Vector3f[] l, boolean get, BoundingSphere sphere, boolean isRoot, int[] indices, Geometry mesh, Vector2f heightmapOffset, float centerOffset){
		first = v1;
		second = v2;
		third = v3;
		fourth = v4;
		width = size;
		index1 = x;
		index2 = y;
		children = has;
		childs = child;
		leaves = l;
		receiving = get;
		parent = p;
		face = f;
		this.sphere = sphere;
		this.isRoot = isRoot;
		this.indices = indices;
		this.mesh = mesh;
		this.heightmapOffset = heightmapOffset;	
	}
	public String toString(Quad q){
		String s = "("+q.first+","+q.second+","+q.third+","+q.fourth+") "+q.width+" "+q.index1+" "+q.index2;
		return s;

	}
	// System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
	public static Vector3f[] getVertices(){

	int size = list.size();
	int n = 0;
	for(int i = 0; i<size; i++){
		if(list.get(i).receiving){
		n++;
		}
	}
	n*=1024;
Vector3f[] r = new Vector3f[n];
int s = 0;
		for(int i = 0; i<size; i++){
			Quad q = list.get(i);
			if(q.receiving){
			System.arraycopy(q.leaves, 0, r, s, q.leaves.length);
			s +=1024;
			}else{
				continue;
				}
		}

		return r;
	}
	
	public static  Vector3f[] makeVerts(Quad q, Vector3f p, int x, int y, float increment){
		Vector3f[] r = new Vector3f[4];
		Vector3f back_left = new Vector3f(0,0,0);
		Vector3f front_right = new Vector3f(0,0,0);
		Vector3f front_left = new Vector3f(0,0,0);
		Vector3f back_right = new Vector3f(0,0,0);
		float xf = x;
		float yf = y;
		Vector3f bottom = new Vector3f (nhsize, nhsize, nhsize);
//		Vector3f top = new Vector3f(nhsize, hsize, nhsize);
		Vector3f back = new Vector3f(nhsize, nhsize, nhsize);
		Vector3f front = new Vector3f(nhsize, nhsize, hsize);
		Vector3f left = new Vector3f(nhsize, nhsize, nhsize);
		Vector3f right = new Vector3f(hsize, nhsize, nhsize);
if(q.face.equals(faces.BOTTOM.toString())){
	
		 back_left = new Vector3f (xf*increment, 0 , yf*increment).add(p).add(bottom);
		 front_right = new Vector3f ((xf*increment) + increment, 0, (yf*increment)+increment).add(p).add(bottom);
		 front_left = new Vector3f (xf*increment, 0 , (yf*increment)+increment).add(p).add(bottom);
		 back_right = new Vector3f((xf*increment) + increment, 0, yf*increment).add(p).add(bottom);
		 
} else if(q.face.equals(faces.TOP.toString())){
	 back_left = new Vector3f (xf*increment, 0 , yf*increment).add(p);
	 front_right = new Vector3f ((xf*increment) + increment, 0, (yf*increment)+increment).add(p);
	 front_left = new Vector3f (xf*increment, 0 , (yf*increment)+increment).add(p);
	 back_right = new Vector3f((xf*increment) + increment, 0, yf*increment).add(p);
	 
} else if(q.face.equals(faces.BACK.toString())){
	
	 back_left = new Vector3f (xf*increment, yf*increment , 0).add(p).add(back);
	 front_right = new Vector3f ((xf*increment) + increment, (yf*increment)+increment, 0).add(p).add(back);
	 front_left = new Vector3f (xf*increment, (yf*increment)+increment , 0).add(p).add(back);
	 back_right = new Vector3f((xf*increment) + increment, yf*increment, 0).add(p).add(back);
	 
} else if(q.face.equals(faces.FRONT.toString())){
	
	 back_left = new Vector3f (xf*increment, yf*increment , 0).add(p).add(front);
	 front_right = new Vector3f ((xf*increment) + increment, (yf*increment)+increment, 0).add(p).add(front);
	 front_left = new Vector3f (xf*increment, (yf*increment)+increment , 0).add(p).add(front);
	 back_right = new Vector3f((xf*increment) + increment, yf*increment, 0).add(p).add(front);
	 
} else if(q.face.equals(faces.LEFT.toString())){
	
	 back_left = new Vector3f (0, xf*increment , yf*increment).add(p).add(left);
	 front_right = new Vector3f (0, (xf*increment) + increment, (yf*increment)+increment).add(p).add(left);
	 front_left = new Vector3f (0, xf*increment , (yf*increment)+increment).add(p).add(left);
	 back_right = new Vector3f(0, (xf*increment) + increment, yf*increment).add(p).add(left);
	 
} else if(q.face.equals(faces.RIGHT.toString())){
	
	 back_left = new Vector3f (0, xf*increment , yf*increment).add(p).add(right);
	 front_right = new Vector3f (0, (xf*increment) + increment, (yf*increment)+increment).add(p).add(right);
	 front_left = new Vector3f (0, xf*increment , (yf*increment)+increment).add(p).add(right);
	 back_right = new Vector3f(0, (xf*increment) + increment, yf*increment).add(p).add(right);
	 
}

		r[0] = ((back_left));
		r[1] = ((front_left));
		r[2] = ((front_right));
		r[3] = ((back_right));

		return r;
	}
	
	public static void split(Quad quad, int i, int ii){
		quad.receiving = false;
		quad.children = true;
		ArrayList<Quad> temp = new ArrayList<Quad>();
		i*=2;
		ii*=2;
		Vector3f parent = new Vector3f(0,0,0);
		if(quad.face.equals(faces.BOTTOM.toString()) || quad.face.equals(faces.TOP.toString())){
		parent = new Vector3f(i*quad.width/2, 0, ii*quad.width/2);
		}else if(quad.face.equals(faces.LEFT.toString()) || quad.face.equals(faces.RIGHT.toString())){
		parent = new Vector3f(0, i*quad.width/2, ii*quad.width/2);	
		}else if(quad.face.equals(faces.BACK.toString()) || quad.face.equals(faces.FRONT.toString())){
	    parent = new Vector3f( i*quad.width/2, ii*quad.width/2, 0);
		}
		
		Vector3f[] vv = new Vector3f[4];
			for(int x = 0; x<2; x++){
				for(int y = 0; y<2; y++){	
					float growth = quad.width/2;
					BoundingSphere sp = new BoundingSphere();
					vv = makeVerts(quad,parent,x,y,growth);
					Vector3f p1 = vv[0];
					Vector3f p2 = vv[1];
					Vector3f p3 = vv[2];
					Vector3f p4 = vv[3];
					Quad c = new Quad (quad.face,quad,p1,p2,p3,p4,growth,(x+i),(y+ii),false,null,null,true,null,false,null,null,null,0);
					c.heightmapOffset = new Vector2f(c.index1 * growth,c.index2 * growth);
					sp.setCenter(getCenter(c)); sp.setRadius(growth/2);
					c.sphere = sp;
					c.leaves = addVertices(c);
					c.mesh = createMesh(c);
					list.add(c);
					temp.add(c);
			}
				
			}

			quad.childs = temp;

	} 	
		
	public static Vector3f[] addVertices(Quad q){
		
	    Vector3f parent = new Vector3f(0,0,0);
		if(q.face.equals(faces.BOTTOM.toString()) || q.face.equals(faces.TOP.toString())){
		parent = new Vector3f(q.index1*q.width, 0, q.index2*q.width);
		}else if(q.face.equals(faces.LEFT.toString()) || q.face.equals(faces.RIGHT.toString())){
		parent = new Vector3f(0, q.index1*q.width, q.index2*q.width);	
		}else if(q.face.equals(faces.BACK.toString()) || q.face.equals(faces.FRONT.toString())){
	    parent = new Vector3f( q.index1*q.width, q.index2*q.width, 0);
		}


		Vector3f[] v = new Vector3f[verticesPerQuad];
		Vector3f[] vv = new Vector3f[4];
		int[] i = new int[(verticesPerQuad*6)/4];
		int n = 0;
		int nn = 0;
		int ii = 0;
		for(int x = 0; x < num_leaves; x++){
			for(int y = 0; y < num_leaves; y++){
				float g = (q.width/num_leaves);
			vv = makeVerts(q,parent,x,y,g);
			v[n++] = vv[0];
			v[n++] = vv[1];
			v[n++] = vv[2];
			v[n++] = vv[3];
			i[nn++] = ii+0;
			i[nn++] = ii+1;
			i[nn++] = ii+2;
			i[nn++] = ii+2;
			i[nn++] = ii+3;
			i[nn++] = ii+0;
			ii+=4;
			}
			
		}
		q.indices = i;
		return v;
	}

	public static Geometry createMesh(Quad q){
		Mesh m = new Mesh();
		FloatBuffer fb = BufferUtils.createFloatBuffer(q.leaves);
		IntBuffer ib = BufferUtils.createIntBuffer(q.indices);
		m.setBuffer(Type.Position, 3, fb);
		m.setBuffer(Type.Index, 3, ib);
		m.setBound(new BoundingSphere(q.width+2000f, new Vector3f(0,0,0)));
		Geometry geo = new Geometry("Mesh", m);
		return geo;
		
	}
	
	public static Material makeMaterial(String path){
		Material mat = new Material(ma.getAssetManager(), path);
		return mat;
	}
	
	public static  Vector3f getCenter(Quad q){
		Vector3f center = ((q.first).add((q.second).add((q.third).add((q.fourth))))).divide(4);
		center.y += q.centerOff;
		return ((center));

	}
	public static Vector3f spherize(Vector3f v){
		 float newX = (float) (v.x * (Math.sqrt(1-((v.y*v.y)/denom1)-((v.z*v.z)/denom1)+(((v.y*v.y)*(v.z*v.z))/denom2))));
		 float newY = (float) (v.y * (Math.sqrt(1-((v.x*v.x)/denom1)-((v.z*v.z)/denom1)+(((v.x*v.x)*(v.z*v.z))/denom2))));
		 float newZ = (float) (v.z * (Math.sqrt(1-((v.y*v.y)/denom1)-((v.x*v.x)/denom1)+(((v.y*v.y)*(v.x*v.x))/denom2))));
		return new Vector3f(newX, newY, newZ);
	}
		}


