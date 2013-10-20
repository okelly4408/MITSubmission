MITSubmission
=============

This contains all of the described parts of my project including interactive level of detai, GPU generated heightmaps, Sobel filter normal mapping for lighting, and real-time atmospheric scattering
All code is in Java with the use of the JMonkey3D Graphics Library. The project "Original Planetary Code" represenets the base from which this project developed.
It contains the original elevation algorithms as well as a primitive subdivision algorithm which has become obsolete with the creation of my 
dynamic level of detail algorithm. This project uses Java3D as its graphics library. 
Special acknowledgements to Sean O'Neil whose work greatly influenced the atmospheric scattering implementation as well
as Ken Perlin who outlined the basis for procedural noise. 

OriginalPlanetaryCode
=======================
This project contains: 
*Accurate Cube to Sphere Mapping
*Procedural Continents, Mountain Ranges, and Low-Lands
*A Primitive System of Polygon Subdivision

This project contains the code that inspired the rest of the code seen in this project. It is from this original idea that 
allowed me to continue work advancing the developlment of a planetary terrain engine. Although most of this code is obsolete 
and inaccurate, one can glean an idea of how this project originated through the presented code.

DynamicLevelOfDetail
=======================
This project contains:
*Position Dependent Detail
*Non-recursive Quadtree Level of Detail Algorithm
*Dynamic System of Data deletion
*Elevation Data Generated Procedurally on the GPU using openCL for Java

This project contains the code that is used to allow massive pieces of terrain to exist while maintaining performance. 
This efficiency was accomplished through the use of a quadtree level of detail system. Example terrain is efficiently generated
on the GPU using openCL for Java and an Apple implementaion of Perlin Noise. 


FromSpaceScatteringWithProceduralClouds
========================
This Project Contains:
*"Clouds" randomly generated on the GPU using GLSL shaders
*GPU Realtime Atmospheric Scattering Implemented in Java

This project contains the code that can be used to create planets with accurate atmospheric scattering and 
interesting features such as clouds. The clouds are generated procedurally in real-time using Perlin Noise implemented
in a GLSL shader. Atmospheric scattering is accomplished through the use of a GLSL shader containing Rayleigh and Mie
scattering calculations. This implementation was heavily influenced by the work of Sean O'Neil. 


GPUTerrainGenerator
========================
This Project Contains:
*Terrain Generated on the GPU using GLSL Shaders
*Terrain is Renedered to a Texture and Used as a Heightmap
*Lighting is Calculated on the GPU using GLSL Shaders 
*Lighting is Accomplished Through the Use of a Sobel Filer Edge Detector
*Coloring is Accomplished Through the Detection of Steep Terrain

This project contains the code that is used to create terrain entirely on the GPU quickly and efficiently. Using a framebuffer
elevation data is first calculated in a GLSL shader using a Perlin Noise algorithm. This data is then rendered to a texture.
The resultant texture is then used as a "heightmap" which is sent to more shaders that interpret the colors of the texture 
as height values and modify vertices based on these interpretations. Lighting is then calculated through the use of a Sobel Filter applied
to the heightmap texture. 





