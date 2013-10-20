import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Universe extends JPanel{
	  private SimpleUniverse su;
	  private BranchGroup sceneBG;
	  private BoundingSphere bounds;
	  private Graphics screen;
	  private static final Point3d USERPOSN = new Point3d(30,30,30);

	  public Universe()
	  {	 
		
	    setLayout(new BorderLayout());
	    setOpaque( false );
	    setPreferredSize( new Dimension(1024, 1024));

	    GraphicsConfiguration config =
						SimpleUniverse.getPreferredConfiguration();
	    Canvas3D canvas3D = new Canvas3D(config);
	    add("Center", canvas3D);
	    canvas3D.setFocusable(true);     // give focus to the canvas 
	    canvas3D.requestFocus();
	    su = new SimpleUniverse(canvas3D);
	    scenegraph();
	    initUserPosition();        // set user's viewpoint
	    orbitControls(canvas3D);
	    su.addBranchGraph( sceneBG );
	  }
	   void scenegraph(){
		  sceneBG= new BranchGroup();
		      bounds = new BoundingSphere(new Point3d(40,40,40),100);
		      lightScene();
		  sceneBG.addChild( new Cube().getBg());
		  sceneBG.compile();
	  }
	   private void orbitControls(Canvas3D c)
	   /* OrbitBehaviour allows the user to rotate around the scene, and to
	      zoom in and out.  */
	   {
	     OrbitBehavior orbit = 
	 		new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
	     orbit.setSchedulingBounds(bounds);
orbit.setZoomFactor(100);
	     ViewingPlatform vp = su.getViewingPlatform();
	     vp.setViewPlatformBehavior(orbit);	    
	   }  // end of orbitControls()


	   private void lightScene()
	   // one directional light
	   { Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	     Vector3f lightDir = new Vector3f(1.0f, -1.0f, -1f);
	     DirectionalLight light1 =  new DirectionalLight(white, lightDir);
	     light1.setInfluencingBounds(bounds);
	     sceneBG.addChild(light1);
	   } 
	   private void initUserPosition()
	   // Set the user's initial viewpoint using lookAt()
	   {
	     ViewingPlatform vp = su.getViewingPlatform();
	     TransformGroup steerTG = vp.getViewPlatformTransform();
	     vp.getViewPlatform().setActivationRadius(300f);

	     Transform3D t3d = new Transform3D();
	     steerTG.getTransform(t3d);

	     // args are: viewer posn, where looking, up direction
	     t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
	     t3d.invert();
	     javax.media.j3d.View view = su.getViewer().getView();
	     view.setBackClipDistance(100000);
	     view.setFrontClipDistance(0.05);
	     steerTG.setTransform(t3d);
	   }
	  
}