package edu.mccc.cos210.tp3d.Util;
import edu.mccc.cos210.tp3d.Util.*;
import com.sun.j3d.utils.picking.PickResult;
import javax.media.j3d.*;
import javax.vecmath.*;
public class KeyCABehavior4 extends KeyCABehavior3 {
	private TransformGroup tg;
	private BranchGroup bg;
	private Transform3D t3d = new Transform3D();
	private Shape3D s3d;
	public KeyCABehavior4(TransformGroup tg, BranchGroup bg) {
		super(tg, bg);
		this.tg = tg;
		this.bg = bg;
	}
	protected void rollLeft() {
		move(new Vector3d(-speed, 0.0, 0.0));
	}
	protected void rollRight() {
		move(new Vector3d(+speed, 0.0, 0.0));
	}
	protected void move(Vector3d v3d) {
		if (!isCollision(v3d)) {
			tg.getTransform(t3d);
			Transform3D tt3d = new Transform3D();
			tt3d.setTranslation(v3d);
			t3d.mul(tt3d);
			tg.setTransform(t3d);
		} else {
			ApplicationBundle ab = (ApplicationBundle) s3d.getUserData();
			if (ab != null) {
				CollisionBehavior cb = (CollisionBehavior) ab.getApplicationData("CollisionBehavior");
				if (cb != null) {
					int postId = cb.getPostId();
					postId(postId);
				}
			}
		}
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
		SceneGraphPath[] sgpa = bg.pickAllSorted(ps);
		if (!(sgpa == null)) {
			for (int i = 0; i < sgpa.length; i++) {
				if (sgpa[i].getObject() instanceof Shape3D) {
					s3d = (Shape3D) sgpa[i].getObject();
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
