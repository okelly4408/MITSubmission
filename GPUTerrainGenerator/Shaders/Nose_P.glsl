uniform sampler2D m_permSampler2d;
uniform sampler2D m_permGradSampler;
uniform sampler2D m_testTexture;
uniform float m_Off;
uniform float m_Gain;
uniform float m_H;
uniform float m_Amp;
uniform float m_lac;

varying vec4 pos;

vec3 fade(vec3 t)
{
	return (t * t * t * (t * (t * 6.0 - 15.0) + 10.0)); 
}
vec4 perm2d(vec2 p)
{
	return texture2D(m_permSampler2d, p);
}
float gradperm(float x, vec3 p)
{
	return dot(vec3(texture2D(m_permGradSampler, vec2(x,0.0)).xyz), p);
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



//**RIDGED MULTIFRACTAL**//
bool firstRMF = true;
float exponentArray[12];
float RMF(in vec3 v)  {
// H = 1.1499999
//Off = 9499999
//Gain = 2.6499996
		 float result, frequency, signal, weight;
		 float H = m_H;
		 float lacunarity = m_lac;
		 int octaves = 12;
		 float offset = m_Off;
		 float gain = m_Gain;
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
//**RIDGED MULTIFRACTAL**//
     
     
//**Fractional Brownian Motion**//
float fbm(in vec3 v){
 
          int octaves = 12;
          float frequency = 0.45;
          float persistence = 0.8;
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
//**Fractional Brownian Motion**//

//**HYBRID MULTIFRACTAL**//
float HMF( vec3 point)
{
float H = 0.25;
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
//**HYBRID MULTIFRACTAL**//


void main(void){
//if(gl_FragCoord.x == (511.5) || gl_FragCoord.x == (0.5) || gl_FragCoord.y == 0.5 || gl_FragCoord.y == 511.5){
//gl_FragColor = vec4(0.0);
//}
//else{
float n1 =   HMF(vec3(gl_FragCoord.xyz/1024.0))+0.65;
float n2 =   RMF(vec3(gl_FragCoord.xyz/1024.0))+0.65;
float n4 = n1 + n2 ;


gl_FragColor = vec4(n4,n4,n4,1.0);
//}

}

