import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3f;
public class NoiseTest{
	 static Double[] array;
	 static ArrayList<Double> arrayList;
	   

	 public static double mountains (Point3f v)
	   {
		 float x = v.x;
		 float y = v.y;
		 float z = v.z;
		     arrayList = new ArrayList<Double>();
		      double h = .5;
		      double lacunarity = 2;
boolean first = true;
		      double frequency = 2;
		      if(first){
		      for (int i = 0; i < 30; i++) {
		         arrayList.add(Math.pow (frequency, -h));
		         frequency *= lacunarity;
		      }
		      first = false;
		      array = new Double[arrayList.size()];
		      arrayList.toArray(array);
		      }
		   double freq = 3;
	      x *= freq;
	      y *= freq;
	      z *= freq;

	      double signal = 0.0;
	      double value  = 0.0;
	      double weight = 1.0;
	      int octaves = 10;


	      double offset = .65;
	      double gain = 2;

	      for (int i = 0; i < octaves; i++)
	      {
	        
	         signal = noise(x,y,z);

	         signal = Math.abs (signal);
	         signal = offset - signal;

	       
	         signal *= signal;
	         signal *= weight;
	         weight = signal * gain;
	         if (weight > 1.0)
	            weight = 1.0;
	         if (weight < 0.0)
	            weight = 0.0;


	         // Add the signal to the output value.
	         value += (signal *array[i]);

	         // Go to the next octave.
	         x *= lacunarity;
	         y *= lacunarity;
	         z *= lacunarity;
	      }
	      return (value * 1.25) - 1.0;
	     
	   }

	 public static Point3f displace(float x, float y, float z, Long seed)
	 {
		 double power = 1.4;
		 float frequency = .0125f;
		 int octaves = 4;
	      float x0, y0, z0;
	      float x1, y1, z1;
	      float x2, y2, z2;

	      x0 = (float) (x + (12414.0 / 65536.0));
	      y0 = (float) (y + (65124.0 / 65536.0));
	      z0 = (float) (z + (31337.0 / 65536.0));
	      x1 = (float) (x + (26519.0 / 65536.0));
	      y1 = (float) (y + (18128.0 / 65536.0));
	      z1 = (float) (z + (60493.0 / 65536.0));
	      x2 = (float) (x + (53820.0 / 65536.0));
	      y2 = (float) (y + (11213.0 / 65536.0));
	      z2 = (float) (z + (44845.0 / 65536.0));
	      float xD = (float) (x + (calculateNoise(x0, y0, z0,octaves,.5f,frequency,seed) * power));
	      float yD = (float) (y + (calculateNoise(x1, y1, z1,octaves,.5f,frequency,seed) * power));
	      float zD = (float) (z + (calculateNoise(x2, y2, z2,octaves,.5f,frequency,seed) * power));

	       Point3f displaced = new Point3f(xD, yD, zD);
	      return displaced;
	   }

	
	
	  



	 public static double plains (double x, double y, double z)
	   {
		 double lacunarity = 2;
		 double p = .5;
		 double frequency = 2;
		 int octaves = 10;
	      double sum = 0;
	      double smoothed = 0;
	      double level_persistence = 1.0;

	      x *= frequency;
	      y *= frequency;
	      z *= frequency;

	      for (int i = 0; i < octaves; i++)
	      {
	       
	         smoothed = noise(x,y,z);
	         smoothed = 2.0 * Math.abs(smoothed) - 1.0;
	         sum += smoothed * level_persistence;

	         x *= lacunarity;
	         y *= lacunarity;
	         z *= lacunarity;
	         level_persistence *= p;
	      }

	      sum += 0.5;
	      return sum;
	   }
	 
	
	 
	public static float calculateNoise(float x, float y, float z, int octaves, float persistence, float frequency,long seed){
		  double lacunarity = 2;
	      double sum = 0;
	      double noise = 0;
	      double pers = 1.0;
	      x *= frequency;
	      y *= frequency;
	      z *= frequency;

	      for (int i = 0; i<octaves; i++)
	      {
	         noise = noise(x, y, z);
	         sum += noise * pers;
	         x *= lacunarity;
	         y *= lacunarity;
	         z *= lacunarity;
	         pers *= persistence;
		}
		return (float) sum;
	}

	private static int grad3[][] = {
		{1,1,0},{-1,1,0},{1,-1,0},
		{-1,-1,0},
        {1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
        {0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}};
private static int[] p = new int[256];

public static void random(long seed){
	for(int i = 0; i<=255;i++){
		p[i]= i;
	}
		Random random = new Random(seed);
		for(int i = 0; i<p.length; i++){
			int normal = p[i];
			int ii = i + random.nextInt(p.length - i);
			p[i] = p[ii];
			p[ii] = normal;		
		}
}
private static int perm[] = new int[512];

public static void initialize(Long seed) {
	random(seed); 
	for(int i=0; i<512; i++) 
    perm[i]=p[i & 255];
	}
private static double dot(int g[], double x, double y, double z) {
return g[0]*x + g[1]*y + g[2]*z; }

//	Implementation of Ken Perlin's Simplex Noise
public static double noise(double xin, double yin, double zin) {
double n0, n1, n2, n3;
final double F3 = 1.0/3.0;
double s = (xin+yin+zin)*F3; 
int i = (int)Math.floor(xin+s);
int j = (int)Math.floor(yin+s);
int k = (int)Math.floor(zin+s);
final double G3 = 1.0/6.0; 
double t = (i+j+k)*G3; 
double X0 = i-t; 
double Y0 = j-t;
double Z0 = k-t;
double x0 = xin-X0; 
double y0 = yin-Y0;
double z0 = zin-Z0;

int i1, j1, k1; 
int i2, j2, k2; 
if(x0>=y0) {
  if(y0>=z0)
    { i1=1; j1=0; k1=0; i2=1; j2=1; k2=0; 
    	} 
    else if(x0>=z0) { 
    	i1=1; j1=0; k1=0; i2=1; j2=0; k2=1; 
    	} 
    else { 
    	i1=0; j1=0; k1=1; i2=1; j2=0; k2=1;
    	}
  }
else {
  if(y0<z0) {
	  i1=0; j1=0; k1=1; i2=0; j2=1; k2=1; 
	  } 
  else if(x0<z0) { 
	  i1=0; j1=1; k1=0; i2=0; j2=1; k2=1; 
	  } 
  else {
	  i1=0; j1=1; k1=0; i2=1; j2=1; k2=0; 
	  } 
}
double x1 = x0 - i1 + G3;
double y1 = y0 - j1 + G3;
double z1 = z0 - k1 + G3;
double x2 = x0 - i2 + 2.0*G3; 
double y2 = y0 - j2 + 2.0*G3;
double z2 = z0 - k2 + 2.0*G3;
double x3 = x0 - 1.0 + 3.0*G3; 
double y3 = y0 - 1.0 + 3.0*G3;
double z3 = z0 - 1.0 + 3.0*G3;

int ii = i & 255;
int jj = j & 255;
int kk = k & 255;
int gi0 = perm[ii+perm[jj+perm[kk]]] % 12;
int gi1 = perm[ii+i1+perm[jj+j1+perm[kk+k1]]] % 12;
int gi2 = perm[ii+i2+perm[jj+j2+perm[kk+k2]]] % 12;
int gi3 = perm[ii+1+perm[jj+1+perm[kk+1]]] % 12;
// Calculate the contribution from the four corners
double t0 = 0.5 - x0*x0 - y0*y0 - z0*z0;
if(t0<0) n0 = 0.0;
else {
  t0 *= t0;
  n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
}
double t1 = 0.5 - x1*x1 - y1*y1 - z1*z1;
if(t1<0) n1 = 0.0;
else {
  t1 *= t1;
  n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
}
double t2 = 0.5 - x2*x2 - y2*y2 - z2*z2;
if(t2<0) n2 = 0.0;
else {
  t2 *= t2;
  n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
}
double t3 = 0.5 - x3*x3 - y3*y3 - z3*z3;
if(t3<0) n3 = 0.0;
else {
  t3 *= t3;
  n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
}

return 32.0*(n0 + n1 + n2 + n3);
}
public static float limit(float y, float upper, float lower){

	if(y >= upper){
		return y = upper;
	}
	else if(y <= lower){
		return y = lower;
	}else{
		return y;
	}
}
public static float bias(float y, float scale, float enviro){
	float newy = (y * scale) + enviro;
return newy;
}
public static double curveDown (float y_land1, float y_land2, float y_landShape , float lowerBound, float upperBound,double curve_width)
{
	   double boundSize = upperBound - lowerBound;
	   curve_width = (curve_width > boundSize / 2) ? boundSize / 2: curve_width;
	

   double x;

   if (curve_width > 0.0)
   {
      if (y_landShape < (lowerBound - curve_width))
         return y_land1;
      else if (y_landShape< (lowerBound + curve_width))
      {
         double lowerCurve = (lowerBound - curve_width);
         double upperCurve = (lowerBound + curve_width);
         x = hermite((y_landShape - lowerCurve) / (upperCurve - lowerCurve));
         return simple(y_land1,y_land2,x);
      }
      else if (y_landShape < (upperBound - curve_width))
         
         return y_land2;
      else if (y_landShape < (upperBound + curve_width))
      {
         
         double lowerCurve = (upperBound - curve_width);
         double upperCurve = (upperBound + curve_width);
         x = hermite((y_landShape - lowerCurve) / (upperCurve - lowerCurve));
         return simple(y_land1, y_land2,x);
      }
      else
     
         return y_land1;         
   }
   else
   {
      if (y_landShape < lowerBound || y_landShape > upperBound)
         return y_land1;
      else
        return y_land2;
   }
}
public static double hermite(double x){	
      return (x * x * (3.0 - 2.0 * x));  
}
public static double simple(double x1, double x2, double y)
{
  return ((1 - y) * x1) + (y * x2);
}

public static double voronoi (float x, float y, float z, long seed)
{

	float frequency = 1;
	float displacement = 1;
    
    x *= frequency;
    y *= frequency;
    z *= frequency;
    
    int xInt = (x > 0.0? (int)x: (int)x - 1);
    int yInt = (y > 0.0? (int)y: (int)y - 1);
    int zInt = (z > 0.0? (int)z: (int)z - 1);
    
    double minDist = 2147483647.0;
    double xCandidate = 0;
    double yCandidate = 0;
    double zCandidate = 0;
       
    for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++)
    {
        for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++)
        {
            for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++)
            {
                // Calculate the position and distance to the seed point inside of
                // this unit cube.
                initialize(seed+100);
                double xPos = xCur + noise(xCur, yCur, zCur);
                initialize(seed+534);
                double yPos = yCur + noise(xCur, yCur, zCur);
                initialize(seed+398);
                double zPos = zCur + noise(xCur, yCur, zCur);
                double xDist = xPos - x;
                double yDist = yPos - y;
                double zDist = zPos - z;
                double dist = xDist * xDist + yDist * yDist + zDist * zDist;
                
                if (dist < minDist)
                {
                    // This seed point is closer to any others found so far, so record
                    // this seed point.
                    minDist = dist;
                    xCandidate = xPos;
                    yCandidate = yPos;
                    zCandidate = zPos;
                }
            }
        }
    }
    
    double value;

        double xDist = xCandidate - x;
        double yDist = yCandidate - y;
        double zDist = zCandidate - z;
        value = (Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist)
                 ) * Math.sqrt(3) - 1.0;
   //value = 0;
    
   
        initialize(seed+239);
    return value + (displacement * (double)noise (
                                                                  (int)(Math.floor (xCandidate)),
                                                                  (int)(Math.floor (yCandidate)),
                                                                  (int)(Math.floor (zCandidate))));

}
}




