attribute vec4 inPosition;

uniform mat4 g_WorldViewProjectionMatrix;
uniform sampler2D heightMap; 

varying vec2 uv;


float nsize = 1024.0/2.0;
float denom1 = nsize*nsize*2.0;
float denom2 = nsize*nsize*nsize*nsize*3.0;
vec3 spherize( in vec4 v){
float x = (v.x * (sqrt(1.0-((v.y*v.y)/denom1)-((v.z*v.z)/denom1)+(((v.y*v.y)*(v.z*v.z))/denom2))));
float y = (v.y * (sqrt(1.0-((v.x*v.x)/denom1)-((v.z*v.z)/denom1)+(((v.x*v.x)*(v.z*v.z))/denom2))));
float z = (v.z * (sqrt(1.0-((v.y*v.y)/denom1)-((v.x*v.x)/denom1)+(((v.y*v.y)*(v.x*v.x))/denom2))));
return vec3(x,y,z);
}


vec2 scale(in vec2 v){
return ((v + 1.0)/2.0);
}

void main(void){
vec4 pos1 = inPosition;
uv = (vec2(pos1.x/1024.0 , pos1.z/1024.0));
float h = texture2D(heightMap, uv).r*140.0;
pos1.y += h;

//vec4 pos = vec4(spherize(inPosition),1.0);
//vec3 normalpos = normalize(vec3(pos.xyz));
//pos.x = normalpos.x * (h + nsize); 
//pos.z = normalpos.z * (h + nsize);

gl_Position = g_WorldViewProjectionMatrix * (pos1);
}