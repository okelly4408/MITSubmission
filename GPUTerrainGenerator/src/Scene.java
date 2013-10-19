import java.nio.ByteBuffer;
import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapAxis;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;


public class Scene extends SimpleApplication{

	static int[] permutationArr;
	private float H =  0.81000006f;
	private float Off = 0.8200002f;
	private float gain = 2.2399998f;
	private float lacunarity = 2.123212f;
	private float amp = .5f;
	private Material mat, matr;
	private static ViewPort vp;
	static int c = 0;
	static boolean isDone = true;
	private Geometry ge;

	static float[][] grads = new float[][]{
		{1,1,0},
	    {-1,1,0},
	    {1,-1,0},
	    {-1,-1,0},
	    {1,0,1},
	    {-1,0,1},
	    {1,0,-1},
	    {-1,0,-1}, 
	    {0,1,1},
	    {0,-1,1},
	    {0,1,-1},
	    {0,-1,-1},
	    {1,1,0},
	    {0,-1,1},
	    {-1,1,0},
	    {0,-1,-1}	
	};
	public static void main(String[] args) {
		
		AppSettings set = new AppSettings(true);
		Scene hm = new Scene();
		hm.setSettings(set);
		set.setHeight(768);
		set.setWidth(1024);
		set.setTitle("Terrain Viewer");
		hm.setShowSettings(false);
		hm.setDisplayStatView(false);
		hm.start();
		

	}

	@Override
	public void simpleInitApp() {
		
		cam.setRotation(new Quaternion(0.29266608f, 0.40400368f, -0.13819563f, 0.85558724f));
		cam.setLocation(new Vector3f(-203.63002f, 815.3117f, -165.41049f));
		cam.lookAtDirection(new Vector3f(0.5892528f, -0.6116936f, 0.52783716f), Vector3f.UNIT_Y);
		Camera c = new Camera(1024,1024);
		c.setLocation(new Vector3f (0,0,1));
		c.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
		vp =  renderManager.createPreView("View", c);
		flyCam.setMoveSpeed(250);
		cam.setFrustumFar(1000000);
		Vector3f v1 = new Vector3f(0,0,0);
		Vector3f v2 = new Vector3f(0,0,1024);
		Vector3f v3 = new Vector3f(1024,0,1024);
		Vector3f v4 = new Vector3f(1024,0,0);
		BoundingSphere sp = new BoundingSphere();
		
	    QuadN qq = new QuadN(QuadN.faces.TOP.toString(),null,v1,v2,v3,v4,1024,0,0,false,null,null,true,null,true);
	    sp.setRadius(1024/2);
		sp.setCenter(QuadN.getCenter(qq));
		qq.sphere = sp;
		Vector3f[] verts = QuadN.addVertices(qq);
		qq.leaves = verts;
		QuadN.list.add(qq);
		 Mesh mesh = new Mesh();
		 int[] indices = new int[(verts.length/4) * 6];
		 int n = 0;
		for(int i = 0; i<verts.length;i+=4){
			  indices[n++]=i;
		  	  indices[n++]=i+1;
		  	  indices[n++]=i+2;
		  	  indices[n++]=i+2;
		  	  indices[n++]=i+3;
		  	  indices[n++]=i;
	  	  
	    }
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
		mesh.setBound(new BoundingSphere(2000f, new Vector3f(0,0,0)));
		mesh.updateBound();
		Geometry geo = new Geometry("Mesh", mesh);
		matr = new Material(assetManager, "MatTest.j3md");
		matr.setTexture("heightMap", getHeightMap());
		matr.getAdditionalRenderState().setWireframe(false);
		geo.setMaterial(matr);
		rootNode.attachChild(geo);
		initKeys();

	}

	public Texture2D getHeightMap(){
		Texture2D hm = new Texture2D(1024,1024,Format.RGBA32F);	
		Quad q = new Quad(2,2);
		mat = new Material(assetManager, "material.j3md"); 
		mat.setFloat("H", H);
		mat.setFloat("Amp", amp);
		mat.setFloat("Off", Off);
		mat.setFloat("Gain", gain);
		mat.setFloat("lac", lacunarity);
		ge = new Geometry("Mesh",q);
		ge.setMaterial(mat);
		mat.setTexture("permSampler2d", permutationTexture());
		mat.setTexture("permGradSampler", gradientTexture());
		FrameBuffer fbo = new FrameBuffer(1024,1024,1);
		fbo.setColorTexture(hm);
		vp.setOutputFrameBuffer(fbo);
		vp.attachScene(ge);
		if(vp.isEnabled()){
		ge.updateGeometricState();
		}
		return hm;
	}

	public int perm2d(int i){
		return permutationArr[i % 256];
	}
	
	private void initKeys() {
		
	    inputManager.addMapping("Inc H",  new KeyTrigger(KeyInput.KEY_H), new KeyTrigger(KeyInput.KEY_RBRACKET));
	    inputManager.addMapping("Dec H", new KeyTrigger(KeyInput.KEY_H), new KeyTrigger(KeyInput.KEY_LBRACKET));
	    
	    inputManager.addMapping("Inc Off",  new KeyTrigger((KeyInput.KEY_RSHIFT)));
	    inputManager.addMapping("Dec Off", new KeyTrigger((KeyInput.KEY_LSHIFT)));
	    
	    inputManager.addMapping("Inc Gain",  new KeyTrigger(KeyInput.KEY_G), new KeyTrigger(KeyInput.KEY_O));
	    inputManager.addMapping("Dec Gain", new KeyTrigger(KeyInput.KEY_G), new KeyTrigger(KeyInput.KEY_P));
	    	  
	    inputManager.addMapping("Recreate", new KeyTrigger(KeyInput.KEY_R));
	    inputManager.addMapping("AmpChange", new KeyTrigger(KeyInput.KEY_U));
	    
	    inputManager.addMapping("LacUp", new KeyTrigger(KeyInput.KEY_L));
	    inputManager.addMapping("LacDown", new KeyTrigger(KeyInput.KEY_TAB));


	    inputManager.addListener(actionListener,"Inc H", "Dec H", "Inc Off", "Dec Off", 
	    		"Inc Gain", "Dec Gain" , "Recreate", "AmpChange", "LacUp", "LacDown");
	 
	  }
	 
	
	private ActionListener actionListener = new ActionListener() {
	    public void onAction(String name, boolean keyPressed, float tpf) {
	        if (name.equals("Inc H") && !keyPressed) {
	        H += 0.03;
	        mat.setFloat("H", H);
	        System.out.println("H "+H);
	        
	        }
	        if(name.equals("Dec H") && !keyPressed){
	        	H-=0.03;
	        	mat.setFloat("H", H);
	        	System.out.println("H "+H);
	        }
	        
	        if (name.equals("Inc Off") && !keyPressed) {
		        Off += 0.03;
		        mat.setFloat("Off", Off);
		        System.out.println("Off "+Off);
		        }
		        if(name.equals("Dec Off") && !keyPressed){
		        	Off-=0.03;
		        	mat.setFloat("Off", Off);
			        System.out.println("Off "+Off);

		        }
		        
		        if (name.equals("Inc Gain") && !keyPressed) {
			        gain += 0.04;
			        mat.setFloat("Gain", gain);
			        System.out.println("Gain "+gain);

			        }
			        if(name.equals("Dec Gain") && !keyPressed){
			        	gain-=0.04;
			        	mat.setFloat("Gain", gain);
			            System.out.println("Gain "+gain);
			        }
			        if(name.equals("Recreate") && !keyPressed){ 
			        recompute();

			        }
			        if(name.equals("AmpChange") && !keyPressed){
			        	amp += 0.05;
			        	mat.setFloat("Amp",amp);
			        	System.out.println(amp);
			        }
			        if (name.equals("LacUp") && !keyPressed) {
			        	vp.clearScenes();
				        lacunarity += 0.02;
				        mat.setFloat("lac", + lacunarity);
			            System.out.println("Lacunarity "+ lacunarity);

				        }
				        if(name.equals("LacDown") && !keyPressed){
				        	lacunarity-=0.02;
				        	mat.setFloat("lac", + lacunarity);
				            System.out.println("Lacunarity "+ lacunarity);
				        }
	      
	    }
	  };
	  
	public void init(){		
		long seed = new Random().nextLong();
		System.out.println(seed);
		// 2130976733373650671l
			Random r = new Random(seed);
			 permutationArr = new int[256];
			for(int i = 0; i<permutationArr.length; i++){
				permutationArr[i] = -1;
			}
			
			for(int i = 0; i< permutationArr.length; i++){
				while(true){
					int iP = Math.abs(r.nextInt()) % permutationArr.length;
					if(permutationArr[iP] == -1){
						permutationArr[iP] = i;
						break;
					}
				}
			}
	} 
	public Texture2D permutationTexture(){
		init();
		ByteBuffer data = BufferUtils.createByteBuffer( (int)Math.ceil(Format.RGBA16F.getBitsPerPixel() / 8.0) * 256 * 256);
		Image permImage = new Image();
		permImage.setWidth(256);
		permImage.setHeight(256);
		permImage.setFormat(Format.RGBA16F);
		permImage.setData(data);
		
		ImageRaster rP = ImageRaster.create(permImage);
		for(int x = 0; x < 256; x++){
			for(int y = 0; y < 256; y++){
				int A = perm2d(x) + y;
                int AA = perm2d(A);
                int AB = perm2d(A + 1);
                int B = perm2d(x + 1) + y;
                int BA = perm2d(B);
                int BB = perm2d(B + 1);
                ColorRGBA c = new ColorRGBA((float)AA/255f, (float)AB/255f, (float)BA/255f, (float)BB/255f);
                rP.setPixel(x, y, c);
				
			}
		} 
		
		Texture2D permutationTable = new Texture2D(permImage);
		permutationTable.setWrap(WrapMode.Repeat);
		permutationTable.setMagFilter(MagFilter.Nearest);
		permutationTable.setMinFilter(MinFilter.NearestNoMipMaps);
		
		return permutationTable;
	}
	
	public Texture2D gradientTexture(){
		
		ByteBuffer data2 = BufferUtils.createByteBuffer( (int)Math.ceil(Format.RGBA16F.getBitsPerPixel() / 8.0) * 256 * 1);
		Image gradImage = new Image();
		gradImage.setWidth(256);
		gradImage.setHeight(1);
		gradImage.setFormat(Format.RGBA16F);
		gradImage.setData(data2);
		
		ImageRaster rG = ImageRaster.create(gradImage);
		for(int x = 0; x<256; x++){
			for(int y = 0; y < 1; y++){

				ColorRGBA c = new ColorRGBA(grads[permutationArr[x]%16][0], 
				grads[permutationArr[x] % 16][1], 
				grads[permutationArr[x] % 16][2], 1);
				rG.setPixel(x, y, c);
			}
		} 
		Texture2D gradientTable = new Texture2D(gradImage);
		gradientTable.setWrap(WrapAxis.S, WrapMode.Repeat);
		gradientTable.setWrap(WrapAxis.T, WrapMode.Clamp);
		gradientTable.setMagFilter(MagFilter.Nearest);
		gradientTable.setMinFilter(MinFilter.NearestNoMipMaps);
		
		return gradientTable;
		
	}
	
	
	public void recompute(){
		mat.setTexture("permSampler2d", permutationTexture());
		mat.setTexture("permGradSampler", gradientTexture());
	}
	
}



