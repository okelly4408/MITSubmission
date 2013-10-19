uniform sampler2D heightMap;

varying vec2 uv;

void main(void){

vec3 normal;

float texel = 1.0/1024.0;

vec2 o00 = uv + vec2(-texel, -texel);
vec2 o10 = uv + vec2(0.0, -texel);
vec2 o20 = uv + vec2(texel, -texel);

vec2 o01 = uv + vec2(-texel, 0.0);
vec2 o21 = uv + vec2(texel, 0.0);

vec2 o02 = uv + vec2(-texel, texel);
vec2 o12 = uv + vec2(0.0,  texel);
vec2 o22 = uv + vec2(texel,  texel);


float h00 = texture2D( heightMap, o00 ).r;
float h10 = texture2D( heightMap, o10 ).r;
float h20 = texture2D( heightMap, o20 ).r;

float h01 = texture2D( heightMap, o01 ).r;
float h21 = texture2D( heightMap, o21 ).r;

float h02 = texture2D( heightMap, o02 ).r;
float h12 = texture2D( heightMap, o12 ).r;
float h22 = texture2D( heightMap, o22 ).r;

float Gx = h00 - h20 + 2.0 * h01 - 2.0 * h21 + h02 - h22;
float Gy = h00 + 2.0 * h10 + h20 - h02 - 2.0 * h12 - h22;

float Gz = 0.25 * sqrt(1.0 - Gx * Gx - Gy * Gy);

normal =  vec3(normalize(vec3(2.0 * Gx, 2.0 * Gy, Gz)));

vec3 color;
float slope = length(vec2(normal.x, normal.y));
vec3 color1 = vec3(0.1 , 0.30 , 0.0);
//vec3 color1 = vec3(1.0);
//vec3 color2 = vec3(0.733,0.588,0.0627);
vec3 color2 = vec3(0.20 , 0.20 , 0.20);
if(slope < 0.25){
//float blend = smoothstep(0.0,0.35,slope);
//color = mix(color1,color2,blend); 
color = color1;
}else{
//float blend = smoothstep(0.35,1.0,slope);
//color = mix(color2,color1,blend) ;
color = color2;
}




vec3 light = normalize(vec3(1.0,-2.8,1.0));
float ldn = dot(light, normal);

ldn = max(0.0,ldn);

gl_FragColor = vec4( color * (1.75 * ldn),1.0);

//gl_FragColor = vec4(vec3(0.5) + (normal * 0.5), 1.0);

}