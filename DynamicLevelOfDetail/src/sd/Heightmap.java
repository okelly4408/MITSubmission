package sd;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_MEM_USE_HOST_PTR;
import static org.jocl.CL.CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE;
import static org.jocl.CL.CL_QUEUE_PROFILING_ENABLE;
import static org.jocl.CL.CL_RGBA;
import static org.jocl.CL.CL_UNSIGNED_INT8;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadImage;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clSetKernelArg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_image_format;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ImageRaster;
import com.jme3.texture.plugins.AWTLoader;

public class Heightmap {

	
	private static cl_context context;

    private static cl_command_queue commandQueue;

    private static cl_mem inputImageMem;
    
    private static cl_kernel compKernel;
    
    private static cl_image_format imageFormat;
    
    /* This class mainly contains openCL utility code */
	public static void initialize(){
		String programSource = readFile("Shaders/compute.cl");
		
		final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        CL.setExceptionsEnabled(true);

        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{device}, 
            null, null, null);
        
        int imageSupport[] = new int[1];
        clGetDeviceInfo (device, CL.CL_DEVICE_IMAGE_SUPPORT,
            Sizeof.cl_int, Pointer.to(imageSupport), null);
        if (imageSupport[0]==0)
        {
            System.exit(1);
            return;
        }

        long properties = 0;
        properties |= CL_QUEUE_PROFILING_ENABLE;
        properties |= CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE;
        commandQueue = clCreateCommandQueue(context, device, properties, null);

        cl_program program = clCreateProgramWithSource(context,
            1, new String[]{ programSource }, null, null);

        clBuildProgram(program, 0, null, null, null, null);

         compKernel = clCreateKernel(program, "compKernel", null);
         
            imageFormat = new cl_image_format();
	        imageFormat.image_channel_order = CL_RGBA;
	        imageFormat.image_channel_data_type = CL.CL_FLOAT;
	}
	
	
	public static Texture createHeightmap(int size, Vector2f index, float scale, Quad q){
  //image for writing to in kernel
		BufferedImage input = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_RGB);
            Graphics g = input.createGraphics();
            g.drawImage(input, 0, 0, null);
            g.dispose(); 
            
           
         
		float[] float2 = new float[]{index.x, index.y};
		float[] scaled = new float[]{scale};
				
		    imageFormat = new cl_image_format();
	        imageFormat.image_channel_order = CL_RGBA;
	        imageFormat.image_channel_data_type = CL_UNSIGNED_INT8;
		
		DataBufferInt dataBufferSrc =
                (DataBufferInt)input.getRaster().getDataBuffer();
          int dataSrc[] = dataBufferSrc.getData();

	        inputImageMem = CL.clCreateImage2D(
	        context, CL_MEM_READ_WRITE | CL_MEM_USE_HOST_PTR,
	        new cl_image_format[]{imageFormat}, size, size,
	        size * Sizeof.cl_uint, Pointer.to(dataSrc), null);
	        
	        
	           
	        long globalWorkSize[] = new long[2];
	        globalWorkSize[0] = size;
	        globalWorkSize[1] = size;

	        clSetKernelArg(compKernel, 0, Sizeof.cl_mem, Pointer.to(inputImageMem));
	        clSetKernelArg(compKernel, 1, Sizeof.cl_float*2, Pointer.to(float2));
	        clSetKernelArg(compKernel, 2, Sizeof.cl_float, Pointer.to(scaled));
	

	        clEnqueueNDRangeKernel(commandQueue, compKernel, 2, null,
	        globalWorkSize, null, 0, null, null);
 
	        clEnqueueReadImage(
	            commandQueue, inputImageMem, true, new long[3],
	            new long[]{size, size, 1},
	            size * Sizeof.cl_uint, 0,
	            Pointer.to(dataSrc), 0, null, null); 

	       
	              
	        Texture tex = new Texture2D(new AWTLoader().load(input, false));
	        
	     //  get elevation data for the center point
	        ImageRaster r = ImageRaster.create(tex.getImage());
	        q.centerOff = (r.getPixel(size/2, size/2).r)*140; 

	        return tex;
	
	
}

	
	private static String readFile(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
