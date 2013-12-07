package sagma.games.rts.emitters;

import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.render.Render;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;
import sagma.games.rts.entity.projectile.Bullet;
import sagma.games.rts.entity.ship.Ship;
import static sagma.core.math.Math.*;

/**
 * This is intended to act as a gun for ships. It can be rotated within a range and will shoot
 * an instance out of the end. 
 * Aiming allows the emitter to rotate around the z axis within its rotation range.
 * 
 * 
 * TODO - BUGS
 * The gun now works for rotationRanges of between 0 and 86 anymore and rotation will push the gun past 0o to 360o which
 * destroys all the logic checks. My brain isn't turned on enough to work it out but we can always create a new object for
 * fully rotating turrets.
 * 
 * @author Michael
 *
 */
public class DirectedEmitter extends Instance {
	public static final float DEFAULT_ROTATION_SPEED = 3f;

	Instance parent;
	Instance child;

	//in degrees
	float nativeAngle;
	//range to left and right of nativeAngle in degrees, if 180o or over, will rotate the full 360o
	float rotationRange;
	float rotationSpeed;

	Vec3 rotationOrigin;
	Vec3 firingOrigin;
	private int cooldown = 0;

	private Vec3 trajectory = new Vec3(0,0,0);

	public static final int WEAPON_COOLDOWN = 1000; // 1000 ms = 1 second
	private int recharge;


	public DirectedEmitter(Instance parent, Instance child, Vec3 positionOffset, Vec3 rotationOriginOffset, 
			Vec3 firingOffset, float nativeAngle, float rotationRange) {
		super("gun");
		setVisible(false);
		this.parent = parent;
		this.child = child;
		this.nativeAngle = nativeAngle;
		this.rotationRange = rotationRange;
		this.rotationSpeed = DEFAULT_ROTATION_SPEED;

		if(parent != null){
			//Add to centroid of ship, add the position offset then add the rotation offset
			setPosition(new Vec3(parent.getLocation().add(positionOffset).add(rotationOriginOffset)));
			setRotation(new Vec3(parent.getRotation().add(0,0,nativeAngle)));
		}
		else{
			setPosition(new Vec3(0,0,0));
		}
		this.firingOrigin = getPosition().add(firingOffset);
		this.rotationOrigin = getPosition().add(rotationOriginOffset);
		setScale(0.08f);
		recharge = WEAPON_COOLDOWN;
	}


	public void shoot(){
		if (cooldown == 0) {
			if(parent instanceof Ship){
				if((Player)((Ship)parent).getOwner() == RTS.playerList.get(0)){
					RTS.soundSystem.playOnce(RTS.soundCollection.get("shoot"));
				}
			}


			//((Ship) parent).getSounds().play(Mixer3D.SOUND_1);
			if(child instanceof Bullet){
				Bullet b = new Bullet(parent);

				//b.addParticleEmitter();
				b.addExplosion();


				float angle = parent.getRotation().z + getRotation().z;

				float x = sin(180 - angle - 90); //Add 90 because turret image on its side
				float y = cos(180 - angle - 90); //WILL be source of BUGS later
				trajectory.set(x, y, 0);
				trajectory.m_normalize();
				trajectory.m_scale(Bullet.BASE_SPEED);

				b.addSpeed(trajectory.add(parent.getSpeed()));

				Vec2 parentLoc = parent.getPosition().xy();
				Vec2 myLoc = getPosition().xy();
				Vec2 parentDirection = directionOfAngle(360 - parent.getRotation().z);
				parentDirection.m_normalize();
				parentDirection.m_scale(myLoc.length()*0.5f);

				Vec3 turretPos = getPosition().rotateAround(new Vec3(0,0,0), parent.getRotation().z);

				Vec2 position = parentLoc.add(parentDirection).add(turretPos.xy());
				b.setLocation(position, 1);

				//b.addParticleEmitter();

				Render.add(b);
				addCooldown(recharge);
			}
		}
	}

	public void setRechargeRate(int recharge){
		this.recharge = recharge;
	}
	public int getRechargeRate(){
		return recharge;
	}


	public void addCooldown(long ms) {
		float interval = 1000f / ms;
		float steps = Render.FRAMES_PER_SECOND / interval;
		cooldown += steps;
	}

	public void aim(Vec2 glMouseCoord){
		Vec2 turretMouseVec = glMouseCoord.subtract(getPosition().xy());
		turretMouseVec.m_normalize();

		float angleToCursor = atan2(turretMouseVec.y, turretMouseVec.x);
		if(angleToCursor < 0){
			angleToCursor = 360 - abs(angleToCursor);
		}

		float dirFromNative = checkRotationDirection(angleToCursor, nativeAngle);
		float dirToCursor = checkRotationDirection(angleToCursor, getRotation().z);

		if(angleToCursor < nativeAngle+rotationRange && angleToCursor > nativeAngle-rotationRange){
			if (dirToCursor > 0) {
				addRotation(0,0, rotationSpeed);
				if(checkRotationDirection(angleToCursor, getRotation().z) < 0){
					setRotation(0,0, angleToCursor);
				}
			}
			else if (dirToCursor < 0) {
				addRotation(0,0, -rotationSpeed);
				if(checkRotationDirection(angleToCursor, getRotation().z) > 0){
					setRotation(0,0, angleToCursor);
				}
			}
		}
		else{
			if (dirFromNative > 0) {
				addRotation(0,0, rotationSpeed);
			}
			else if (dirFromNative < 0) {
				addRotation(0,0, -rotationSpeed);
			}
		}
		
		if(getRotation().z > nativeAngle+rotationRange){			
			setRotation(0,0, nativeAngle+rotationRange);
		}
		else if(getRotation().z < nativeAngle-rotationRange){
			setRotation(0,0, nativeAngle-rotationRange);
		}
	}


	@Override
	public void heartbeat() {
		super.heartbeat();
		if (cooldown > 0) cooldown--;
	}

	public Instance getParent(){
		return parent;
	}

	/**
	 * 
	 * @param desiredAngle
	 * @param currentAngle
	 * @return if greater than zero, desiredAngle is clockwise
	 * if less than zero, desiredAngle is counterClockwise
	 * else desiredAngle equals currentAngle
	 */
	public static float checkRotationDirection(float desiredAngle, float currentAngle){
		return ((desiredAngle- currentAngle)+540) %360 - 180;
	}


}
