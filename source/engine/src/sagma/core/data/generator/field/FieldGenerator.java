package sagma.core.data.generator.field;

import sagma.core.math.Vec3;

/**
 * Generates a field. A field is typically a region of force.
 * 
 * Main used in applications such as gravity force, wind force and sphere generation.
 * 
 * @author Aaron Kison
 *
 */
public abstract class FieldGenerator {
	public abstract float nextField(Vec3 field);
}
