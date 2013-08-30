package edu.mccc.cos210.tp3d.View;
import com.cbthinkx.util.Debug;
import com.sun.j3d.utils.geometry.ColorCube;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import edu.mccc.cos210.tp3d.Model.*;
import edu.mccc.cos210.tp3d.View.*;
import edu.mccc.cos210.tp3d.Util.*;
import edu.mccc.cos210.tp3d.*;
public class GUI extends JFrame {
	private Model model;
	private Controller controller;
	private ControlPanel controlPanel;
	private TransformGroup viewTG;
	private Transform3D viewT;
	private VirtualUniverse alley;
	private Locale foulLine;
	private View view;
	private Canvas3D viewCanvas;
	private ScoreModel score;
	private Scoreboard scoreboard;
	private BoundingSphere everywhere = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
	/**
	 * Constructor taking six parameters.  This is the only constructor provided.  Because each paramater is of an object type,
	 * no default values are possible.
	 * @param model The model holds information about implementors of the interface MovableObject in tp3d.
	 * @param controller The Controller controls the model and views of tp3d.
	 * @param music A reference to the music thread.
	 * @param sound A reference to the sound thread.
	 * @param fc The FlowControl holds state data for tp3d.
	 * @param score The ScoreModel holds scoring data for tp3d.
	 */
	public GUI(Model model, Controller controller, Music music, Sound sound, FlowControl fc, ScoreModel score) {
		super("Ten Pin 3D");
		Debug.println("GUI.GUI()");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		Dimension d = new Dimension(800, 800);
		this.setSize(d);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension ssize = toolkit.getScreenSize();
		int x = ((int)ssize.width - (int)d.width) / 2;
		int y = ((int)ssize.height - (int)d.width) / 2;
		this.setLocation(x, y);
		this.setResizable(true);
		this.model = model;
		this.controller = controller;
		this.controlPanel = new ControlPanel(controller, music, sound, fc, score);
		this.scoreboard = new Scoreboard(fc, score);
		Canvas viewCanvas = getCanvas();
		this.getContentPane().add(viewCanvas, BorderLayout.SOUTH);
		viewCanvas.setVisible(true);
		this.getContentPane().add(controlPanel, BorderLayout.WEST);
		this.getContentPane().add(scoreboard, BorderLayout.EAST);
		controlPanel.setFocusable(false);
		viewCanvas.setFocusable(true);
		viewCanvas.requestFocus();
		this.pack();
		this.setVisible(true);
	}
	/**
	 * repaints the GUI.
	 */
	public void repaint() {
		super.repaint();
		scoreboard.repaint();
		controlPanel.repaint();
	}
	/**
	 * Builds the view branch of the scene graph.  The view branch of the scene graph contians information about the system and
	 * about how the virtual universe is meant to be displayed.
	 */
	public BranchGroup buildView() {
		Debug.println("GUI.buildView()");
		BranchGroup viewBranch = new BranchGroup();
		ViewPlatform viewPlatform = new ViewPlatform();
		viewTG = new TransformGroup();
		viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		viewBranch.addChild(viewTG);
		Transform3D viewT = new Transform3D();
		viewT.set(new Vector3d(0, 2, -3));
		Transform3D temp = new Transform3D();
		temp.rotY(Math.PI);
		viewT.mul(temp);
		viewTG.setTransform(viewT);
		viewTG.addChild(viewPlatform);
		GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = localGE.getScreenDevices();
		GraphicsConfiguration[] graphicsConfigurations = graphicsDevices[0].getConfigurations();
		GraphicsConfigTemplate3D viewTemp = new GraphicsConfigTemplate3D();
		GraphicsConfiguration viewConfig = viewTemp.getBestConfiguration(graphicsConfigurations);
		viewCanvas = new Canvas3D(viewConfig);
		view = new View();
		view.setFrontClipDistance(0.001);
		view.setBackClipDistance(500.0);
		view.setWindowMovementPolicy(View.VIRTUAL_WORLD);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		view.attachViewPlatform(viewPlatform);
		return viewBranch;
	}
	/**
	 * Builds the content branch of the scene graph.  The content branch contains information about the objects which are
	 * to be displayed in the virtual universe.
	 */
	public BranchGroup buildContent() {
		Debug.println("GUI.GUI()");
		BranchGroup contentBranch = new BranchGroup();
		DirectionalLight light = new DirectionalLight(
			new Color3f(1.0f, 1.0f, 1.0f),
			new Vector3f(0, -1, 0)
		);
		light.setInfluencingBounds(everywhere);
		contentBranch.addChild(light);
		AmbientLight aLight = new AmbientLight(
			true,
			new Color3f(0.3f, 0.3f, 0.3f)
		);
		aLight.setInfluencingBounds(everywhere);
		contentBranch.addChild(aLight);
		for (int i = 0; i <= 10; i++) {
			contentBranch.addChild(getMO(i));
		}
		Lane lane1 = new Lane();
		Lane lane2 = new Lane();
		Lane lane3 = new Lane();
		Walls walls = new Walls();
		contentBranch.addChild(lane1.getBG());
		contentBranch.addChild(walls.getBG());
		Transform3D lane2t3d = new Transform3D();
		lane2t3d.set(new Vector3f(Lane.LANE_WIDTH, 0, 0));
		TransformGroup lane2Offset = new TransformGroup(lane2t3d);
		lane2Offset.addChild(lane2.getBG());
		Transform3D lane3t3d = new Transform3D();
		lane3t3d.set(new Vector3f(-Lane.LANE_WIDTH, 0, 0));
		TransformGroup lane3Offset = new TransformGroup(lane3t3d);
		lane3Offset.addChild(lane3.getBG());
		contentBranch.addChild(lane3Offset);
		contentBranch.addChild(lane2Offset);
		KeyCABehavior4 nav = new KeyCABehavior4(viewTG, contentBranch);
		nav.setSchedulingBounds(everywhere);
		contentBranch.addChild(nav);
		return contentBranch;
	}
	/**
	 * returns the Control Panel object.  After the GUI constucts itself, this method can be used to obtain a refence to the control panel.
	 * @return the ControlPanel
	 */
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	/**
	 * String output.
	 * @return String representation of the GUI.
	 */
	public String toString() {
		Debug.println("GUI.toString()");
		return this.getTitle();
	}
	private BranchGroup getMO(int i) {
		MovableObject [] pins = model.getPins();
		MovableObject mo = pins[i];
		BranchGroup bG = mo.getBG();
		return bG;
	}
	/**
	 * returns a Canvas3D object for the 3D view. The Canvas3D can be added to a heavyweight swing component in order to provide a view of the virtual world.
	 * @return A Canvas3D object.
	 */
	private Canvas3D getCanvas() {
		Debug.println("GUI.getCanvas()");
		alley = new VirtualUniverse();
		foulLine = new Locale(alley);
		BranchGroup branchGroup = buildView();
		foulLine.addBranchGraph(branchGroup);
		branchGroup = buildContent();
		foulLine.addBranchGraph(branchGroup);
		viewCanvas.setSize(600, 600);
		viewCanvas.setMinimumSize(new Dimension(480, 4800));
		view.addCanvas3D(viewCanvas);
		return viewCanvas;
	}
}
