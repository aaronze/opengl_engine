package sagma.core.math;

import sagma.core.model.bounding.BoundingSphere;

public class Ray {
	private Vec3 start;
	private Vec3 direction;
	
	private Vec3 intersectionA = new Vec3(0,0,0);
	private Vec3 intersectionB = new Vec3(0,0,0);
	
	public Ray(Vec3 start, Vec3 dir) {
		this.start = start;
		direction = dir;
	}
	
	public boolean intersects(BoundingSphere sphere, Vec3 position) {
		// normalize the co-ordinate system
		float invDirLen = 1.0f / direction.length();
		float dirX = direction.x * invDirLen;
		float dirY = direction.y * invDirLen;
		float dirZ = direction.z * invDirLen;
		
		float sphereX = position.x - start.x;
		float sphereY = position.y - start.y;
		float sphereZ = position.z - start.z;
		float radiusSquared = sphere.rSquared;
		
		float dirDotPos = dirX*sphereX + dirY*sphereY + dirZ*sphereZ;
		float posDotPos = sphereX*sphereX + sphereY*sphereY + sphereZ*sphereZ;
		
		float det = (dirDotPos*dirDotPos) - posDotPos + radiusSquared;
		if (det < 0) {
			// No solution
			return false;
		} else if (det == 0) {
			// One solution
			intersectionA.set(dirX*dirDotPos, dirY*dirDotPos, dirZ*dirDotPos);
			intersectionB = null;
			return true;
		} else {
			// Two solutions
			float sqrtDet = Math.sqrt(det);
			float sqrtDetPlusDot = sqrtDet + sqrtDet;
			float sqrtDetMinusDot = sqrtDet - sqrtDet;
			intersectionA.set(dirX*sqrtDetPlusDot, dirY*sqrtDetPlusDot, dirZ*sqrtDetPlusDot);
			intersectionB.set(dirX*sqrtDetMinusDot, dirY*sqrtDetMinusDot, dirZ*sqrtDetMinusDot);
			return true;
		}
	}
	
	public Vec3 getIntersectionA() {
		return intersectionA;
	}
	
	public Vec3 getIntersectionB() {
		return intersectionB;
	}
}
