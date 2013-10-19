uniform mat4 g_WorldViewProjectionMatrix;
uniform float m_f;
attribute vec4 inPosition;
uniform sampler2D m_Heightmap;
uniform float m_size;
uniform vec2 m_Offset;
varying vec2 uv;
void main(void){
vec4 pos1 = inPosition;

uv = (vec2( ((pos1.x - m_Offset.x)/m_size) , ((pos1.z - m_Offset.y)/m_size)));

float h = texture2D(m_Heightmap, uv)*140.0;
pos1.y += h;
gl_Position = (g_WorldViewProjectionMatrix * (pos1));
}