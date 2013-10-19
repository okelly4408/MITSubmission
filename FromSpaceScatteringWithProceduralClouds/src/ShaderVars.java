import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class ShaderVars {
    private int nSamples;          
    private float Kr;               
    private float KrESun, Kr4PI;   
    private float Km;               
    private float KmESun, Km4PI;    
    private float ESun;             
    private float G;                
    private float innerRadius;     
    private float scale;            
    private float scaleDepth;      
    private float scaleOverScaleDepth; 

    private Vector3f wavelength;
    private Vector3f invWavelength4; 
    private float exposure;

    private Vector3f position;
    private float rotationSpeed;
    
    public ShaderVars(float radius, Vector3f pos) {
        innerRadius = radius;
        position = pos;
        calculateVars();
    }

    private void calculateVars() {
        nSamples = 2;
        Kr = 0.0025f;
        Km = 0.0015f;
        ESun = 15f;
        exposure = 2f;
        wavelength = new Vector3f(0.650f, 0.570f, 0.475f);
        G = -0.950f;            
        invWavelength4 = new Vector3f();
        scaleDepth = 0.25f;        
        scale = 1.0f / ((innerRadius * 1.025f) - innerRadius);
        scaleOverScaleDepth = scale / scaleDepth;
        KrESun = Kr * ESun;
        KmESun = Km * ESun;
        Kr4PI = Kr * 4.0f * FastMath.PI;
        Km4PI = Km * 4.0f * FastMath.PI;

        invWavelength4.x = 1.0f / FastMath.pow(wavelength.x, 4.0f);
        invWavelength4.y = 1.0f / FastMath.pow(wavelength.y, 4.0f);
        invWavelength4.z = 1.0f / FastMath.pow(wavelength.z, 4.0f);
    }

    public float getRadius() { return innerRadius; }
    public int getnSamples() { return nSamples; }
    public float getfSamples() { return (float)nSamples; }
    public float getKr() { return Kr; }
    public float getKrESun() { return KrESun; }
    public float getKr4PI() { return Kr4PI; }
    public float getKm() { return Km; }
    public float getKmESun() { return KmESun; }
    public float getKm4PI() { return Km4PI; }
    public float getESun() { return ESun; }
    public float getG() { return G; }
    public float getInnerRadius() { return innerRadius; }
    public float getOuterRadius() { return innerRadius * 1.025f; }   
    public float getScale() { return scale; }
    public float getScaleDepth() { return scaleDepth; }
    public float getScaleOverScaleDepth() { return scaleOverScaleDepth; }
    public Vector3f getWavelength() { return wavelength; }
    public Vector3f getInvWavelength4() { return invWavelength4; }
    public float getExposure() { return exposure; }
    public Vector3f getPosition() { return position; }
    public float getRotationSpeed() { return rotationSpeed; }
}