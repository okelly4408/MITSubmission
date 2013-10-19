attribute vec4 inPosition;
varying vec4 pos;
uniform mat4 g_WorldViewProjectionMatrix;

void main(void){

pos = vec4(inPosition);
pos.x += -1.0;
pos.y += -1.0;

gl_Position =  pos;
}