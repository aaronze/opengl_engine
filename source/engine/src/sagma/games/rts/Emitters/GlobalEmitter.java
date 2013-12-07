package sagma.games.rts.emitters;

import static sagma.core.math.Math.cos;
import static sagma.core.math.Math.sin;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.data.manager.ModelManager;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.render.Render;
import sagma.games.rts.InstanceContainer;
import sagma.games.rts.RTS;
import sagma.games.rts.entity.cellestial.Moon;
import sagma.games.rts.entity.cellestial.Planet;
import sagma.games.rts.entity.cellestial.Sun;
import sagma.games.rts.entity.projectile.Bullet;


/**
 * Intended to act as a planet chunk emitter, emitting asteroids of mineral types contained
 * within the parent and only ever producing less minerals than the parent contains. 
 * It should be added as a child of the planet it is to emit from but may be independent from
 * other entities if need be.
 * It will emit at a random angle within a range relative to a native angle. 
 * If no range is provided, the angle of emission is a completely random z rotation
 * The spawn location of the children will be the center of the parent
 * 
 * @author Michael
 *
 */
public class GlobalEmitter extends Instance{
	Instance parent;
	public Hashtable<String, InstanceContainer> childrenTable;
	float nativeAngle;
	float rotationRange;

	private int index = 0;
	private int totalInstances = 0;

	public GlobalEmitter(Instance parent){
		this.parent = parent;
		childrenTable = new Hashtable<String, InstanceContainer>();
		this.nativeAngle = 0;
		this.rotationRange = 180f;
		if(parent!= null){
			this.setLocation(parent.getLocation());
		}
	}

	public GlobalEmitter(Instance parent, float nativeAngle, float rotationRange){
		this.parent = parent;
		childrenTable = new Hashtable<String, InstanceContainer>();
		this.nativeAngle = nativeAngle;
		this.rotationRange = rotationRange;
		if(parent != null){
			this.setLocation(parent.getLocation());
		}
	}

	public GlobalEmitter(Vec3 pos, float nativeAngle, float rotationRange){
		this.parent = null;
		childrenTable = new Hashtable<String, InstanceContainer>();
		this.nativeAngle = nativeAngle;
		this.rotationRange = rotationRange;
		this.setLocation(pos);
	}


	/**
	 * 
	 * @param ind - Each container has a number of instances. If there were two different 
	 * containers and the first had 3 as its noOfInstances, inputting 2 into this method would fire
	 * the first container's instance.
	 */
	public void shoot(int ind){
		int instancesSoFar = 0;

		Set<String> set = childrenTable.keySet();
		Iterator<String> itr = set.iterator();
		
		while (itr.hasNext()) {
			InstanceContainer i = childrenTable.get(itr.next());

			instancesSoFar += i.getNoOfInstances();
			if(ind < instancesSoFar){
				shoot(i);
			}			
		}
	}

	/**
	 * Shoots an instance of the specified InstanceContainer.
	 * Used internally but useful nonetheless.
	 * This is orderless, it does not reduce number of instances contained
	 * This might be used for hardcoding a set of weapons for a turret,
	 * rather than for single use planet destruction
	 * @param i
	 */
	public void shoot(InstanceContainer container){
		Instance instance = null;
		try{
			Class<? extends Instance> instanceClass = container.getType();
			if(instanceClass.isAssignableFrom(Planet.class)){
				instance = new Planet("craters");
			}
			else if(instanceClass.isAssignableFrom(Bullet.class)){
				instance = new Bullet(this);
				((Bullet)instance).addParticleEmitter();
			}
			else if(instanceClass.isAssignableFrom(Moon.class)){
				instance = new Moon();
			}
			else if(instanceClass.isAssignableFrom(Sun.class)){
				instance = new Sun();
			}
			else{
				throw new Exception();
			}

			
			RandomNumberGenerator rand = new RandomNumberGenerator(nativeAngle -rotationRange, nativeAngle + rotationRange);

			Vec3 trajectory = new Vec3(0,0,0);
			float angle = rand.nextNumber();

			float x = sin(180 - angle); //Add 90 because turret image on its side
			float y = cos(180 - angle); //WILL be source of BUGS later
			trajectory.set(x, y, 0);
			trajectory.m_normalize();
			trajectory.m_scale(Bullet.BASE_SPEED);
			trajectory.m_scale((float)Math.random());

			instance.addSpeed(trajectory);

			if(parent != null){
				instance.setLocation(parent.getPosition().x+getPosition().x, parent.getPosition().y+getPosition().y, 1);
			}
			else{
				instance.setLocation(this.getPosition());
			}
			instance.setRotation(0, 0, (float)Math.random()*360.0f);
			instance.addRotationalSpeed(0, 0, (float)Math.random());
			instance.setScale(0.2f);

			Render.add(instance);
		}catch(Exception e){
			System.err.println(this + " attempting to emit unsupported type: Aborted.");
		}
	}


	/**
	 * shoots a random instance from among the collection
	 */
	public void shootRandom(){
		RandomNumberGenerator rand = new RandomNumberGenerator(totalInstances);
		int i = (int)rand.nextNumber();
		shoot(i);
	}

	/**
	 * shoots the next instance in order and loops back
	 */
	public void shootNext(){
		shoot(index++);
		if(index > totalInstances){
			index = 0;
		}
	}

	/**
	 * shoots all contained instances
	 */
	public void pulse(){
		Set<String> set = childrenTable.keySet();
		Iterator<String> itr = set.iterator();

		while (itr.hasNext()) {
			ModelManager.loadLater(RTS.resources + "asteroid.jpg");

			InstanceContainer i = childrenTable.get(itr.next());
			for(int j = 0; j < i.getNoOfInstances(); j++){
				shoot(i);
			}
		}
	}


	@Override
	public void heartbeat(){

	}


	public void add(String key, InstanceContainer i){
		childrenTable.put(key, i);
		totalInstances += i.getNoOfInstances();
	}
}
