import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * Makes a single square in 3D space of given size
 * 
 * @author KW
 */
public class Tile extends MeshView {
	public Tile(float size, PhongMaterial mat) {
		TriangleMesh m = new TriangleMesh();


		float[] points = { /* v0 */-size / 2, -size / 2, 0, /* v1 */size / 2, -size / 2, 0, /* v2 */size / 2, size / 2,
				0, /* v3 */-size / 2, size / 2, 0 };
		float[] texCoords = { /* t0 */0, 0, /* t1 */0, 1, /* t2 */1, 1, /* t3 */1, 0 };
		
		int[] faces = { /* f0 */0, 0, 1, 1, 2, 2, /* f1 */0, 0, 2, 2, 3, 3 }; // merged

		m.getPoints().setAll(points);
		m.getTexCoords().setAll(texCoords);
		m.getFaces().setAll(faces);

		this.setCullFace(CullFace.FRONT);
		this.setMesh(m);
		this.setMaterial(mat);
	}
}
