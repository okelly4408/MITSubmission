
uniform vec3 m_v3LightPos;
uniform float m_fg;
uniform float m_fg2;

varying vec3 v3Direction;
varying vec3 secondary, first;

void main (void)
{
	float fCos = dot(m_v3LightPos, v3Direction) / length(v3Direction);
	float fMiePhase = 1.5 * ((1.0 - m_fg2) / (2.0 + m_fg2)) * (1.0 + fCos*fCos) / pow(1.0 + m_fg2 - 2.0*m_fg*fCos, 1.5);
    gl_FragColor = vec4(first,1.0) + fMiePhase * vec4(secondary,1.0);
	gl_FragColor.a = gl_FragColor.b;
}
