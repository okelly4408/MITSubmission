//Based on the work of Sean O'Neil GPU Gems Volume 2

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform vec3 m_v3CameraPos;		
uniform vec3 m_v3LightPos;		
uniform vec3 m_v3InvWavelength;	
uniform float m_fCameraHeight;	
uniform float m_fCameraHeight2;	
uniform float m_fOuterRadius;		
uniform float m_fOuterRadius2;	
uniform float m_fInnerRadius;		
uniform float m_fInnerRadius2;	
uniform float m_fKrESun;			
uniform float m_fKmESun;			
uniform float m_fKr4PI;			
uniform float m_fKm4PI;			
uniform float m_fScale;			
uniform float m_fScaleDepth;		
uniform float m_fScaleOverScaleDepth;	

attribute vec4 inPosition;

const int nSamples = 2;
const float fSamples = 2.0;

varying vec3 v3Direction;
varying vec3 secondary, first;

float scale(float fCos)
{
	float x = 1.0 - fCos;
	return m_fScaleDepth * exp(-0.00287 + x*(0.459 + x*(3.83 + x*(-6.80 + x*5.25))));
}

void main(void)
{
gl_Position = g_WorldViewProjectionMatrix * inPosition;

	vec3 v3Pos = vec3(g_WorldMatrix * inPosition);
	vec3 v3Ray = v3Pos - m_v3CameraPos;
	float fFar = length(v3Ray);
	v3Ray /= fFar;

	float B = 2.0 * dot(m_v3CameraPos, v3Ray);
	float C = m_fCameraHeight2 - m_fOuterRadius2;
	float fDet = max(0.0, B*B - 4.0 * C);
	float fNear = 0.5 * (-B - sqrt(fDet));

	vec3 v3Start = m_v3CameraPos + v3Ray * fNear;
	fFar -= fNear;
	float fStartAngle = dot(v3Ray, v3Start) / m_fOuterRadius;
	float fStartDepth = exp(-1.0 / m_fScaleDepth);
	float fStartOffset = fStartDepth*scale(fStartAngle);

	float fSampleLength = fFar / fSamples;
	float fScaledLength = fSampleLength * m_fScale;
	vec3 v3SampleRay = v3Ray * fSampleLength;
	vec3 v3SamplePoint = v3Start + v3SampleRay * 0.5;

	vec3 v3FrontColor = vec3(0.0, 0.0, 0.0);
	for(int i=0; i<nSamples; i++)
	{
		float fHeight = length(v3SamplePoint);
		float fDepth = exp(m_fScaleOverScaleDepth * (m_fInnerRadius - fHeight));
		float fLightAngle = dot(m_v3LightPos, v3SamplePoint) / fHeight;
		float fCameraAngle = dot(v3Ray, v3SamplePoint) / fHeight;
		float fScatter = (fStartOffset + fDepth*(scale(fLightAngle) - scale(fCameraAngle)));
		vec3 v3Attenuate = exp(-fScatter * (m_v3InvWavelength * m_fKr4PI + m_fKm4PI));
		v3FrontColor += v3Attenuate * (fDepth * fScaledLength);
		v3SamplePoint += v3SampleRay;
	}
	secondary  = v3FrontColor * m_fKmESun;
	first = v3FrontColor * (m_v3InvWavelength * m_fKrESun);
	v3Direction = m_v3CameraPos - v3Pos;
}
