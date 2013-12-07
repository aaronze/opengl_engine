package sagma.core.model;

import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.vector.CompositeVectorGenerator;
import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec3;

public class State {
	public VectorGenerator positionGenerator;
	public VectorGenerator speedGenerator;
	public VectorGenerator accelerationGenerator;
	public VectorGenerator rotationGenerator;
	public VectorGenerator rotationalSpeedGenerator;
	public VectorGenerator torqueGenerator;
	
	public NumberGenerator massGenerator;
	public NumberGenerator inertiaGenerator;
	
	private Vec3 position = new Vec3(0,0,0);
	private Vec3 speed = new Vec3(0,0,0);
	private Vec3 acceleration = new Vec3(0,0,0);
	private Vec3 rotation = new Vec3(0,0,0);
	private Vec3 rotationalSpeed = new Vec3(0,0,0);
	private Vec3 torque = new Vec3(0,0,0);
	
	public float mass;
	public float inertia;
	
	@Deprecated
	/*
	 * Provided for backward compatability.
	 * This method of declaration is very static.
	 * Please use State(VectorGenerator) instead.
	 */
	public State(Vec3 position) {
		initGenerator();
		
		positionGenerator = new ConstantVectorGenerator(position);
		
		init();
	}
	
	public State(VectorGenerator pos) {
		initGenerator();
		
		positionGenerator = pos;
		
		init();
	}
	
	@Deprecated
	/*
	 * Provided for backward compatability.
	 * This method of declaration is very static.
	 * Please use State(VectorGenerator, VectorGenerator) instead.
	 */
	public State(Vec3 position, Vec3 rotation) {
		initGenerator();
		
		positionGenerator = new ConstantVectorGenerator(position);
		rotationGenerator = new ConstantVectorGenerator(rotation);
		
		init();
	}
	
	public State(VectorGenerator pos, VectorGenerator rot) {
		initGenerator();
		
		positionGenerator = pos;
		rotationGenerator = rot;
		
		init();
	}
	
	@Deprecated
	/*
	 * Provided for backward compatability.
	 * This method of declaration is very static.
	 * Please use State(VectorGenerator, VectorGenerator, VectorGenerator, NumberGenerator,
	 * VectorGenerator, VectorGenerator, VectorGenerator, NumberGenerator) instead.
	 */
	public State(Vec3 initialPosition, Vec3 initialSpeed, Vec3 acceleration, float mass,
			Vec3 initialRotation, Vec3 initialRotationSpeed, Vec3 torque, float inertia) {
		
		initGenerator();
		
		positionGenerator = new ConstantVectorGenerator(initialPosition);
		speedGenerator = new ConstantVectorGenerator(initialSpeed);
		accelerationGenerator = new ConstantVectorGenerator(acceleration);
		massGenerator = new ConstantNumberGenerator(mass);
		
		rotationGenerator = new ConstantVectorGenerator(initialRotation);
		rotationalSpeedGenerator = new ConstantVectorGenerator(initialRotationSpeed);
		torqueGenerator = new ConstantVectorGenerator(torque);
		inertiaGenerator = new ConstantNumberGenerator(inertia);
		
		init();
	}
	
	public State(VectorGenerator pos, VectorGenerator speed, VectorGenerator accel, NumberGenerator mass,
			VectorGenerator rot, VectorGenerator rotSpeed, VectorGenerator torque, NumberGenerator inertia) {
		initGenerator();
		
		positionGenerator = pos;
		speedGenerator = speed;
		accelerationGenerator = accel;
		massGenerator = mass;
		
		rotationGenerator = rot;
		rotationalSpeedGenerator = rotSpeed;
		torqueGenerator = torque;
		inertiaGenerator = inertia;
		
		init();
	}
	
	/*
	 * Warning:
	 * 
	 * Setting the generators to all integrated values (position, rotation, speed and rotational velocity)
	 *  may result in unexpected behaviour if set dynamically. 
	 *  
	 * Unless a lot of thought has gone into it, ConstantVectorGenerators should be used for these
	 *  integrated values.
	 *  
	 * Use other values dynamically with caution. Make sure to read the full documentation 
	 *  and thoroughly test and debug any dynamic (all but ConstantVectorGenerator) values.
	 */
	private void initGenerator() {
		positionGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		rotationGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		speedGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		rotationalSpeedGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		
		accelerationGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		torqueGenerator = new ConstantVectorGenerator(Vec3.EMPTY);
		massGenerator = new ConstantNumberGenerator(0);
		inertiaGenerator = new ConstantNumberGenerator(0);
	}
	
	private void init() {
		positionGenerator.setNextVector(this, position);
		speedGenerator.setNextVector(this, speed);
		accelerationGenerator.setNextVector(this, acceleration);
		mass = massGenerator.nextNumber();
		
		rotationGenerator.setNextVector(this, rotation);
		rotationalSpeedGenerator.setNextVector(this, rotationalSpeed);
		torqueGenerator.setNextVector(this, torque);
		inertia = inertiaGenerator.nextNumber();
	}
	
	public void step() {
		accelerationGenerator.setNextVector(this, acceleration);
		speed.m_add(acceleration);
		position.m_add(speed);
		
		torqueGenerator.setNextVector(this, torque);
		rotationalSpeed.m_add(torque);
		rotation.m_add(rotationalSpeed);
	}
	
	public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }
    
    public float getRotX() {
    	return rotation.x;
    }
    
    public float getRotY() {
    	return rotation.y;
    }
    
    public float getRotZ() {
    	return rotation.z;
    }

    public void move(Vec3 v) {
        position.m_add(v);
    }
    
    public void addSpeed(Vec3 vec) {
    	speed.m_add(vec);
    }
    
    public void addRotationSpeed(Vec3 vec) {
    	rotationalSpeed.m_add(vec);
    }
    
    public void setSpeed(Vec3 vec) {
    	speed = vec;
    }
    
    public void addAcceleration(Vec3 vec) {
    	accelerationGenerator = new CompositeVectorGenerator(accelerationGenerator, new ConstantVectorGenerator(vec));
    }
    
    public void addTorque(Vec3 vec) {
    	torqueGenerator = new CompositeVectorGenerator(torqueGenerator, new ConstantVectorGenerator(vec));
    }
    
    public void setAcceleration(Vec3 vec) {
    	accelerationGenerator = new ConstantVectorGenerator(vec);
    }

    public void setLocation(Vec3 loc) {
    	position.set(loc);
    }

    public void setRotation(Vec3 rot) {
    	rotation.set(rot);
    }
    
    public Vec3 getPosition() {
    	return position;
    }
    
    public Vec3 getRotation() {
    	return rotation;
    }
    
    public Vec3 getRotationalSpeed() {
    	return rotationalSpeed;
    }
    
    public Vec3 getAcceleration() {
    	return acceleration;
    }
    
    public Vec3 getSpeed() {
    	return speed;
    }
    
    public Vec3 getTorque() {
    	return torque;
    }
    
    public float getMass() {
    	return mass;
    }
    
    public float getInertia() {
    	return inertia;
    }
    
    public void setAcceleration(VectorGenerator v) {
    	accelerationGenerator = v;
    }
    
    public void addAcceleration(VectorGenerator v) {
    	accelerationGenerator = new CompositeVectorGenerator(accelerationGenerator, v);
    }
    
    public void setTorque(VectorGenerator v) {
    	torqueGenerator = v;
    }
    
    public void addTorque(VectorGenerator v) {
    	torqueGenerator = new CompositeVectorGenerator(torqueGenerator, v);
    }
    
    public Vec3 getLocation() {
    	return position;
    }
    
    public void addRotation(Vec3 v) {
    	rotation.m_add(v);
    }
    
    public void setRotationalSpeed(Vec3 v) {
    	rotationalSpeed = v;
    }

	public void addRotationalSpeed(Vec3 vec3) {
		rotationalSpeed.m_add(vec3);
	}

	public void addPosition(Vec3 vec3) {
		position.m_add(vec3);
	}
	
	public void setLocation(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void addLocation(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
	
	public void setSpeed(float x, float y, float z) {
		speed.x = x;
		speed.y = y;
		speed.z = z;
	}
	
	public void addSpeed(float x, float y, float z) {
		speed.x += x;
		speed.y += y;
		speed.z += z;
	}
	
	public void setAcceleration(float x, float y, float z) {
		acceleration.x = x;
		acceleration.y = y;
		acceleration.z = z;
	}
	
	public void addAcceleration(float x, float y, float z) {
		acceleration.x += x;
		acceleration.y += y;
		acceleration.z += z;
	}

	public void setRotation(float x, float y, float z) {
		if(x > 360f){
			rotation.x = x%360f;
		}
		else if(x < 0f){
			rotation.x = 360f - Math.abs(x%360f);
		}
		else{
			rotation.x = x;
		}
		
		if(y > 360f){
			rotation.y = y%360f;
		}
		else if(y < 0f){
			rotation.y = 360f - Math.abs(y%360f);
		}
		else{
			rotation.y = y;
		}
		
		if(z > 360f){
			rotation.z = z%360f;
		}
		else if(z < 0f){
			rotation.z = 360f - Math.abs(z%360f);
		}
		else{
			rotation.z = z;
		}
	}

	public void addRotation(float x, float y, float z) {
		rotation.x += x;
		if(rotation.x > 360f){
			rotation.x = x%360f;
		}
		else if(rotation.x < 0f){
			rotation.x = 360f - Math.abs(rotation.x%360f);
		}
		
		rotation.y += y;
		if(rotation.y > 360f){
			rotation.y = rotation.y%360f;
		}
		else if(rotation.y < 0f){
			rotation.y = 360f - Math.abs(rotation.y%360f);
		}
		
		rotation.z += z;
		if(rotation.z > 360f){
			rotation.z = rotation.z%360f;
		}
		else if(rotation.z < 0f){
			rotation.z = 360f - Math.abs(rotation.z%360f);
		}
		
	}
}
