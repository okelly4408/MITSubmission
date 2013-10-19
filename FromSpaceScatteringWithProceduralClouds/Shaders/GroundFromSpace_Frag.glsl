
varying vec4 c0, c1;

uniform sampler2D m_permSampler;
uniform sampler2D m_gradSampler;

varying vec4 pos;

vec3 fade(vec3 t)
{
	return (t * t * t * (t * (t * 6.0 - 15.0) + 10.0)); 
}
vec4 perm2d(vec2 p)
{
	return texture2D(m_permSampler, p);
}
float gradperm(float x, vec3 p)
{
	return dot(vec3(texture2D(m_gradSampler, vec2(x,0.0)).xyz), p);
}

float inoise(in vec3 p)
{
	vec3 P = mod(floor(p), 256.0);	
  	p -= floor(p);                      
	vec3 f = fade(p);                 

	P = P / 256.0;
	const float one = 1.0 / 256.0;
	  
	vec4 AA = perm2d(P.xy) + P.z;
 
  	return mix( mix( mix( gradperm(AA.x, p ),  
                             gradperm(AA.z, p + vec3(-1.0, 0.0, 0.0) ), f.x),
                       mix( gradperm(AA.y, p + vec3(0.0, -1.0, 0.0) ),
                             gradperm(AA.w, p + vec3(-1.0, -1.0, 0.0) ), f.x), f.y),
                             
                 mix( mix( gradperm(AA.x+one, p + vec3(0.0, 0.0, -1.0) ),
                             gradperm(AA.z+one, p + vec3(-1.0, 0.0, -1.0) ), f.x),
                       mix( gradperm(AA.y+one, p + vec3(0.0, -1.0, -1.0) ),
                             gradperm(AA.w+one, p + vec3(-1.0, -1.0, -1.0) ), f.x), f.y), f.z);
}

bool firstRMF = true;
float exponentArray[12];
float RMF(in vec3 v)  {
float H = 0.90;
float offset = .9499999;
float gain = 2.0;
float lacunarity = 2.18793194;
		 float result, frequency, signal, weight;
		
		 int octaves = 12;

         int i;
             frequency =1.0;
             if(firstRMF){     	 
             for (i=0; i<octaves; i++) {
                   exponentArray[i] =  pow(frequency, -H); 
                   frequency *= lacunarity;
                     
             }
             firstRMF = false;
             }

       
         signal = inoise(v);
         
         if (signal < 0.0) signal = -signal;
             

         signal = offset - signal;
         
         signal *=signal;
        
         result = signal;
         weight = 1.0;

         for (i = 1; i < octaves; i++) {
    
             v*=lacunarity;

          
             weight = signal * gain;

             if ( weight > 1.0 ) weight = 1.0;
                 
             if ( weight < 0.0 ) weight = 0.0;
                 

            signal = (inoise(v));

             if ( signal < 0.0 ) signal = -signal;
                 

             signal = offset - signal;
             signal *= signal;
       
             signal *= weight;
             result += signal * exponentArray[i];
         }
         
         return (result - 0.75)/1.5;        
     }

float HMF( vec3 point)
{
float H = 0.15;
float lacunarity = 2.13408435;
int octaves = 12;
float offset = 0.85;
      float frequency, result, signal, weight; 
      int   i;
      
	float array[12];    
            frequency = 1.0;
            for (i=0; i<octaves; i++) {
                  
                  array[i] = pow( frequency, -H);
                  frequency *= lacunarity;
            }

      result = ( inoise( point ) + offset ) * array[0];
      weight = result;

      point *= lacunarity;
     

      for (i=1; i<octaves; i++) {
            if ( weight > 1.0 )  weight = 1.0;
            signal = ( inoise( point ) + offset ) * array[i];
            result += weight * signal;
            weight *= signal;

            point *= lacunarity;
            
      } 

      return( result - 1.0 )/2.0;

}

float fbm(in vec3 v){
 
          int octaves = 12;
          float frequency = 0.45;
          float persistence = 0.7;
          float lacunarity = 2.0;
          float sum = 0.0;
          float noise = 0.0;
          float pers = 1.0;
          v *= frequency;
 
          for (int i = 0; i<octaves; i++)
          {
             noise = inoise(v);
             sum += noise * pers;
             v *= lacunarity;
             pers *= persistence;
        }
        return  sum;
}
void main (void)
{
vec4 color = vec4(0.0);
float n = fbm(vec3(pos.xyz/200.0));
color = vec4(n);
//if(n > 0.5){
//color = vec4(0.1 , 0.30 , 0.0,1.0);
//}
gl_FragColor = c0 + color * c1;
gl_FragColor.a = 1.0;
	
}