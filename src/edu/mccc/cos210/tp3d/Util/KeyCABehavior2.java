package edu.mccc.cos210.tp3d.Util;
import edu.mccc.cos210.tp3d.Util.*;
import com.sun.j3d.utils.picking.PickResult;
import javax.media.j3d.*;
import javax.vecmath.*;
public class KeyCABehavior2 extends KeyBehavior {
	private TransformGroup tg;
	private BranchGroup bg;
	public KeyCABehavior2(TransformGroup tg, BranchGroup bg) {
		super(tg);
		this.tg = tg;
		this.bg = bg;
	}
	protected boolean isCollision(Vector3d v3d) {
		Transform3D t3d = new Transform3D();
		tg.getTransform(t3d);
		Matrix4d m4d = new Matrix4d();
		t3d.get(m4d);
		GMatrix gm1 = new GMatrix(
			4,
			1,
			new double[] {
				v3d.x,
				v3d.y,
				v3d.z,
				0.0
			}
		);
		GMatrix gm2 = new GMatrix(
			4,
			4,
			new double[] {
				m4d.m00, m4d.m01, m4d.m02, 0.0,
				m4d.m10, m4d.m11, m4d.m12, 0.0,
				m4d.m20, m4d.m21, m4d.m22, 0.0,
				0.0, 0.0, 0.0, 0.0
			}
		);
		gm1.mul(gm2, gm1);
		double[] d = new double[4];
		gm1.getColumn(0, d);
		Vector3d v3d0 = new Vector3d();
		v3d0.x = speed == SLOW ? d[0] * 20.0 : d[0] * 2.0;
		v3d0.y = speed == SLOW ? d[1] * 20.0 : d[1] * 2.0;
		v3d0.z = speed == SLOW ? d[2] * 20.0 : d[2] * 2.0;
		PickSegment ps = new PickSegment(
			new Point3d(m4d.m03, m4d.m13, m4d.m23),
			new Point3d(
				m4d.m03 + v3d0.x,
				m4d.m13 + v3d0.y,
				m4d.m23 + v3d0.z
			)
		);
		SceneGraphPath[] sgpa = bg.pickAll(ps);
		if (!(sgpa == null)) {
			for (int i = 0; i < sgpa.length; i++) {
				if (sgpa[i].getObject() instanceof Shape3D) {
					Shape3D s3d = (Shape3D) sgpa[i].getObject();
					PickResult pr = new PickResult(sgpa[i], ps);
					if (pr.numIntersections() > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
