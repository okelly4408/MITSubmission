import java.nio.ByteBuffer;
import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapAxis;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;


public class Main extends SimpleApplication{

	
	static int[] permutationArr;

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
	
	private Geometry pla;
	private Material mat_P;
	DirectionalLight sun;
	private Geometry atm;
	private ShaderVars shaderVars;
	private Material mat_A;
	float angle = (float)Math.PI;
	public final static Vector3f lPos = new Vector3f(0, 0f, - 200 * 10f);
    public final static Vector3f lDir = new Vector3f(0,0,0).subtract(lPos).normalize();
	public static void main(String[] args) {
		AppSettings set = new AppSettings(true);
		//	set.setFrameRate(85);
			set.setTitle("GPU Atmospheric Scattering");
			set.setResolution(1024, 768);
			set.setFrameRate(100);
			set.setVSync(false);
			Main app = new Main();
			app.setShowSettings(false);
			app.setDisplayFps(false);
			app.setDisplayStatView(false);
			app.setSettings(set);
			app.start();
	}

	@Override
	public void simpleInitApp() {
		
		rootNode.setCullHint(CullHint.Never);
		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		
		 sun.setDirection(lDir);
		 rootNode.addLight(sun);
		cam.setLocation(new Vector3f(-240f, 100f, -240f));
		flyCam.setMoveSpeed(50f);
		
		Mesh plan = makePlanet();
		pla = new Geometry("Mesh" , plan);
		mat_P = new Material(assetManager, "GroundFromSpace.j3md");
		mat_P.setTexture("permSampler", permutationTexture());
		mat_P.setTexture("gradSampler", gradientTexture());
	//	mat_P.getAdditionalRenderState().setWireframe(true);
		setupGroundMaterial(mat_P);
		pla.setMaterial(mat_P);
		
		
		
		Mesh atmosphere = makeAtmosphere();
		atm = new Geometry("Mesh", atmosphere);
		mat_A = new Material(assetManager, "SkyFromSpace.j3md");
		mat_A.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Front);
		setupSkyMaterial(mat_A);
		atm.setMaterial(mat_A);
        
		rootNode.attachChild(pla);
		rootNode.attachChild(atm);
	
		

	}
	
public void simpleUpdate(float tpf){  
	angle += tpf * 0.01;
    sun.setDirection(new Vector3f(-FastMath.sin(angle) * 200, FastMath.HALF_PI,
                                   FastMath.cos(angle) * 200).normalize());
        Vector3f camLocation = cam.getLocation();
       float camHeight = camLocation.length();

          if(camHeight > shaderVars.getOuterRadius()){
           mat_P.setVector3("v3CameraPos", camLocation);
           mat_P.setFloat("fCameraHeight", camHeight);
           mat_P.setFloat("fCameraHeight2", (camHeight * camHeight));
           mat_P.setVector3("v3LightPos", sun.getDirection().normalize());

           mat_A.setVector3("v3CameraPos", camLocation);
           mat_A.setFloat("fCameraHeight", camHeight);
           mat_A.setFloat("fCameraHeight2", (camHeight * camHeight)); 
           mat_A.setVector3("v3LightPos", sun.getDirection().normalize());
           
           pla.setMaterial(mat_P);
           atm.setMaterial(mat_A);
          }
}
public Mesh makePlanet(){
	shaderVars = new ShaderVars(200, new Vector3f(0,0,0));
	Mesh pl = new Sphere(128,128,shaderVars.getRadius());
	return pl;
}

public Mesh makeAtmosphere(){
	Mesh atmosphere = new Sphere(128, 128, shaderVars.getOuterRadius());
	return atmosphere;
}



private void setupGroundMaterial(Material mat) {
    mat.setVector3("v3LightPos", sun.getDirection().normalize());
    mat.setVector3("v3InvWavelength", shaderVars.getInvWavelength4());
    mat.setFloat("fKrESun", shaderVars.getKrESun());
    mat.setFloat("fKmESun", shaderVars.getKmESun());
    mat.setFloat("fOuterRadius", shaderVars.getOuterRadius());
    mat.setFloat("fOuterRadius2", shaderVars.getOuterRadius() * shaderVars.getOuterRadius());
    mat.setFloat("fInnerRadius", shaderVars.getInnerRadius());
    mat.setFloat("fInnerRadius2", shaderVars.getInnerRadius() * shaderVars.getInnerRadius());
    mat.setFloat("fKr4PI", shaderVars.getKr4PI());
    mat.setFloat("fKm4PI", shaderVars.getKm4PI());
    mat.setFloat("fScale", shaderVars.getScale());
    mat.setFloat("fScaleDepth", shaderVars.getScaleDepth());
    mat.setFloat("fScaleOverScaleDepth", shaderVars.getScaleOverScaleDepth());
    mat.setFloat("fSamples", shaderVars.getfSamples());
    mat.setInt("nSamples", shaderVars.getnSamples());
    mat.setFloat("fExposure", shaderVars.getExposure());

    
}
private void setupSkyMaterial(Material mat) {
mat.setVector3("v3LightPos", sun.getDirection().normalize());
mat.setVector3("v3InvWavelength", shaderVars.getInvWavelength4());
mat.setFloat("fKrESun", shaderVars.getKrESun());
mat.setFloat("fKmESun", shaderVars.getKmESun());
mat.setFloat("fOuterRadius", shaderVars.getOuterRadius());
mat.setFloat("fInnerRadius", shaderVars.getInnerRadius());
mat.setFloat("fOuterRadius2", shaderVars.getOuterRadius() * shaderVars.getOuterRadius());
mat.setFloat("fInnerRadius2", shaderVars.getInnerRadius() * shaderVars.getInnerRadius());
mat.setFloat("fKr4PI", shaderVars.getKr4PI());
mat.setFloat("fKm4PI", shaderVars.getKm4PI());
mat.setFloat("fScale", shaderVars.getScale());
mat.setFloat("fScaleDepth", shaderVars.getScaleDepth());
mat.setFloat("fScaleOverScaleDepth", shaderVars.getScaleOverScaleDepth());
mat.setFloat("fSamples", shaderVars.getfSamples());
mat.setInt("nSamples", shaderVars.getnSamples());
mat.setFloat("fg", shaderVars.getG());
mat.setFloat("fg2", shaderVars.getG() * shaderVars.getG());
mat.setFloat("fExposure", shaderVars.getExposure());
}

public int getPermvalue(int i){
	return permutationArr[i % 256];
}

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
			int A =  getPermvalue(x) + y;
            int AA = getPermvalue(A);
            int AB = getPermvalue(A + 1);
            int B =  getPermvalue(x + 1) + y;
            int BA = getPermvalue(B);
            int BB = getPermvalue(B + 1);
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
			grads[ permutationArr[x] % 16][1], 
			grads[ permutationArr[x] % 16][2], 1);
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







}
