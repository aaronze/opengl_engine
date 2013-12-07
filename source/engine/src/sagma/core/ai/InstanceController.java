package sagma.core.ai;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.Timer;

import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.profile.Profiler;
import sagma.core.render.Render;

import static sagma.core.math.Math.*;

/**
 * <p>InstanceController specifically takes control over an instance
 * </br>The instance moves according to the controller specification</p>
 * 
 * <p>
 * There are three types of AI:
 * <p><ul>
 * <li>Flock - Instances of Flocks tend to stay together
 * <li>Predators - Predators of this instance to avoid
 * <li>Prey - Prey for this instance to chase
 * </ul></p>
 * </p>
 * 
 * <p>
 * For example, to set up a basic Bird/Bug AI environment you would make the following:
 * </br>Bird flocks with Bird
 * </br>Bird preys on Bug
 * </br>
 * </br>Bug has a predator of Bird
 * </br>Bug does not flock
 * </br>
 * </br>Knowing this AI table you can then make the instances like the following:
 * </br>
 * <code>
 * </br>public class Bird extends Instance {
 * </br>public Bird() {
 * </br>
 * </br>InstanceController ic = new InstanceController(this);
 * </br>ic.addFlock(Bird.class);
 * </br>ic.addPrey(Prey.class);
 * </br>
 * </br>}
 * </br>}
 * </br>
 * </br>public class Bug extends Instance {
 * </br>public Bug() {
 * </br>
 * </br>InstanceController ic = new InstanceController(this);
 * </br>ic.addPredator(Bird.class);
 * </br>
 * </br>}
 * </br>}
 * </code>
 * </p>
 * 
 * <p>
 * Bugs will now flee birds, and Birds will now chase bugs. If the birds cannot find any bugs to chase, they
 * will group up
 * </p>
 * 
 * <p>
 * To alter the functionality of the AI engine, you can set several parameters.
 * </br>All parameters can be set by using setParameter(float value), where Parameter is replaced by the parameter.
 * </br>Generally when dealing with distances, 1.0f is approximately equal to half of the size of the screen (both width and height).
 * </p>
 * 
 * <p>
 * <ul>
 * <li>FlockRange - Range of instances to start grouping. Default is 1.0.
 * <li>PreyRange - Range of instances to persue prey. Default is 0.8.
 * <li>PredatorRange - Range of instances to flee predators. Default is 0.7
 * </ul>
 * 
 * <ul>
 * <li>IdlePriority - How important doing nothing is. Should be lower then flock, prey and predator priorities. Default is 0.1
 * <li>FlockPriority - How important grouping up is. If set higher then predator priority, then will ignore predators. Default is 0.2
 * <li>PreyPriority - How important persuing prey is. Should be higher then flock. Default is 0.8
 * <li>PredatorPriority - How important fleeing predators is. Should be the highest. Default is 1.0
 * <li>Variance - How likely the AI will perform an incorrect action. Default is 0. Not yet implemented.
 * </ul>
 * 
 * <ul>
 * <li>ComfortRangeInner - How close a group member can get before they are 'too close'. Default is 0.2
 * <li>ComfortRangeOuter - How far a group member can get before they are 'too far'. Default is 2.0
 * </ul>
 * </p>
 * 
 * @author Aaron Kison
 *
 */

public class InstanceController {
	public final static float DEFAULT_PRIORITY_FOR_IDLE = 0.1f;
	public final static float DEFAULT_PRIORITY_FOR_FLOCK = 0.2f;
	public final static float DEFAULT_PRIORITY_FOR_PREY = 0.8f;
	public final static float DEFAULT_PRIORITY_FOR_PREDATOR = 1.0f;
	
	public final static float DEFAULT_VARIANCE = 0f;
	
	public final static float DEFAULT_RANGE_FOR_PREY = 0.8f;
	public final static float DEFAULT_RANGE_FOR_FLOCK = 1.0f;
	public final static float DEFAULT_RANGE_FOR_PREDATOR = 0.7f;
	
	public final static float DEFAULT_COMFORT_RANGE_INNER = 0.2f;
	public final static float DEFAULT_COMFORT_RANGE_OUTER = 2.0f;
	
	private float idlePriority = DEFAULT_PRIORITY_FOR_IDLE;
	private float flockPriority = DEFAULT_PRIORITY_FOR_FLOCK;
	private float preyPriority = DEFAULT_PRIORITY_FOR_PREY;
	private float predatorPriority = DEFAULT_PRIORITY_FOR_PREDATOR;
	
	@SuppressWarnings("unused")
	private float variance = DEFAULT_VARIANCE; // TODO Implement variance
	
	private float flockRange = DEFAULT_RANGE_FOR_FLOCK;
	private float preyRange = DEFAULT_RANGE_FOR_PREY;
	private float predatorRange = DEFAULT_RANGE_FOR_PREDATOR;
	
	private float comfortRangeInner = DEFAULT_COMFORT_RANGE_INNER;
	private float comfortRangeOuter = DEFAULT_COMFORT_RANGE_OUTER;
	
	private Instance controlled;
	private Timer beat;
	private Timer actionBeat;
	private ArrayList<ArrayList<Class<? extends Instance>>> flock;
	private ArrayList<ArrayList<Class<? extends Instance>>> prey;
	private ArrayList<ArrayList<Class<? extends Instance>>> predators;
	//private ArrayList<Class<? extends Instance>> flock;
	//private ArrayList<Class<? extends Instance>> prey;
	//private ArrayList<Class<? extends Instance>> predators;
	
	private ArrayList<Instance> flockInRange;
	private ArrayList<Instance> preyInRange;
	private ArrayList<Instance> predatorInRange;
	
	public final static int IDLE =     0x000F;
	public final static int FLOCK =    0x00F0;
	public final static int PREY =     0x0F00;
	public final static int PREDATOR = 0xF000;
	private int action;
	
	public final static int TARGET_SINGLE = 0;
	public final static int TARGET_AVERAGE = 1;
	private int TARGET_TYPE = TARGET_AVERAGE;
	
	public InstanceController(Instance i) {
		controlled = i;
		
		beat = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				heartbeat();
			}
		});
		
		actionBeat = new Timer(400, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionbeat();
			}
			
		});
		
		beat.start();
		actionBeat.start();
	}
	
	public void addFlock(Class<? extends Instance> flockClass) {
		if (flock == null) {
			flock = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			flock.add(new ArrayList<Class<? extends Instance>>());
			flock.add(new ArrayList<Class<? extends Instance>>());
		}
		flock.get(1).add(null);
		flock.get(0).add(flockClass);
	}
	
	
	public void addFlock(Class<? extends Instance> flockClass, Class<? extends Instance> owner) {
		if (flock == null) {
			flock = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			flock.add(new ArrayList<Class<? extends Instance>>());
			flock.add(new ArrayList<Class<? extends Instance>>());
		}
		flock.get(1).add(owner);
		flock.get(0).add(flockClass);
	}

	public void addPrey(Class<? extends Instance> preyClass) {
		if (prey == null) {
			prey = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			prey.add(new ArrayList<Class<? extends Instance>>());
			prey.add(new ArrayList<Class<? extends Instance>>());
		}
		prey.get(1).add(null);
		prey.get(0).add(preyClass);
	}
	
	public void addPrey(Class<? extends Instance> preyClass, Class<? extends Instance> owner) {
		if (prey == null) {
			prey = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			prey.add(new ArrayList<Class<? extends Instance>>());
			prey.add(new ArrayList<Class<? extends Instance>>());
		}
		prey.get(1).add(owner);
		prey.get(0).add(preyClass);
	}
	
	
	public void addPredator(Class<? extends Instance> predatorClass) {
		if (predators == null) {
			predators = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			predators.add(new ArrayList<Class<? extends Instance>>());
			predators.add(new ArrayList<Class<? extends Instance>>());
		}
		predators.get(1).add(null);
		predators.get(0).add(predatorClass);
	}
	
	public void addPredator(Class<? extends Instance> predatorClass, Class<? extends Instance> owner) {
		if (predators == null) {
			predators = new ArrayList<ArrayList<Class<? extends Instance>>>(2);
			predators.add(new ArrayList<Class<? extends Instance>>());
			predators.add(new ArrayList<Class<? extends Instance>>());
		}
		predators.get(1).add(owner);
		predators.get(0).add(predatorClass);
	}
	
	/**
	 * InstanceControllers are automatically started upon creations.
	 * </br>This method only serves to restart after stop is called.
	 */
	public void start() {
		beat.start();
		actionBeat.start();
	}
	
	public void stop() {
		beat.stop();
		actionBeat.stop();
	}
	
	public void setIdlePriority(float f) {
		idlePriority = f;
	}
	
	public void setPredatorPriority(float f) {
		predatorPriority = f;
	}
	
	public void setFlockPriority(float f) {
		flockPriority = f;
	}
	
	public void setPreyPriority(float f) {
		preyPriority = f;
	}
	
	public void setPreyRange(float f) {
		preyRange = f;
	}
	
	public void setPredatorRange(float f) {
		predatorRange = f;
	}
	
	public void setFlockRange(float f) {
		flockRange = f;
	}
	
	public void setComfortRangeInner(float f) {
		comfortRangeInner = f;
	}
	
	public void setComfortRangeOuter(float f) {
		comfortRangeOuter = f;
	}
	
	public void heartbeat() {
		if (action == IDLE) {
			controlled.getState().addSpeed(new Vec3((float)Math.random()*0.001f-0.0005f, (float)Math.random()*0.001f-0.0005f, 0));
			return;
		}
		Profiler.start("Time taken by AI");
		Vec3 pos = controlled.getState().getPosition();
		//controlled.getState().setSpeed(new Vec3(0, 0, 0));
		
		if ((action & FLOCK) == FLOCK) {
			Iterator<Instance> flockList = flockInRange.iterator();
			while (flockList.hasNext()) {
				Instance f = flockList.next();
				if (f == controlled) continue;
				Vec3 fPos = f.getState().getPosition();
				float dist = fPos.distanceTo(pos);
				
				if (dist < comfortRangeInner) {
					// Move away
					moveAwayFrom(f);
					continue;
				} else
				if (dist < comfortRangeOuter) {
					// Move towards
					moveTowards(f);
				}
			}
		}
		if ((action & PREDATOR) == PREDATOR) {
			Iterator<Instance> predatorList = predatorInRange.iterator();
			while (predatorList.hasNext()) {
				Instance f = predatorList.next();
				if (f == controlled) continue;
				Vec3 fPos = f.getState().getPosition();
				float dist = fPos.distanceTo(pos);
				
				if (dist < predatorRange)
					moveAwayFrom(f);
			}
		}
		
		if ((action & PREY) == PREY) {
			if (TARGET_TYPE == TARGET_SINGLE) {
				Iterator<Instance> predatorList = preyInRange.iterator();
				float bestDist = Float.MAX_VALUE;
				Instance preyTarget = null;
				while (predatorList.hasNext()) {
					Instance f = predatorList.next();
					if (f == controlled) continue;
					Vec3 fPos = f.getState().getPosition();
					float dist = fPos.distanceTo(pos);
					if (dist < bestDist) {
						preyTarget = f;
						bestDist = dist;
					}
				}
				if (bestDist < preyRange) {
					moveTowards(preyTarget);
				}
			} else {
				Iterator<Instance> predatorList = preyInRange.iterator();
				while (predatorList.hasNext()) {
					Instance f = predatorList.next();
					if (f == controlled) continue;
					Vec3 fPos = f.getState().getPosition();
					float dist = fPos.distanceTo(pos);
					
					if (dist < preyRange) {
						moveTowards(f);
					}
				}
			}
		}
		
		// Re-orient to movement direction
		float currentAngle = controlled.getRotation().z;
		float desiredAngle = atan2(controlled.getSpeed().y, controlled.getSpeed().x) - 90;
		if (desiredAngle < 0) desiredAngle += 360;
		
		if (abs(currentAngle-desiredAngle) >= 180) {
			if (currentAngle > desiredAngle) currentAngle = -360 + currentAngle;
			else desiredAngle = -360 + desiredAngle;
		}
		
		float angleAdd = 6f;
		if (currentAngle > desiredAngle + angleAdd) {
			controlled.addRotation(0,0,-angleAdd);
		}
		if (currentAngle < desiredAngle - angleAdd) {
			controlled.addRotation(0,0,angleAdd);
		}
		
		Profiler.stop("Time taken by AI");
	}
	
	public void moveTowards(Instance i) {
		Vec3 pos = controlled.getState().getPosition();
		Vec3 iPos = i.getState().getPosition();
		Vec3 dir = pos.subtract(iPos);
		dir.m_normalize();
		dir.m_scale(-0.001f);
		controlled.getState().addSpeed(dir);
	}
	
	public void moveAwayFrom(Instance i) {
		Vec3 pos = controlled.getState().getPosition();
		Vec3 iPos = i.getState().getPosition();
		Vec3 dir = pos.subtract(iPos);
		dir.m_normalize();
		dir.m_scale(0.004f);
		controlled.getState().addSpeed(dir);
	}
	
	public void actionbeat() {
		Profiler.start("Time taken by AI");
		// Sometimes idle
		// Stick to flock
		// Chase after prey
		// Avoid predators
		// Account for variance
				
		float idleScore = idlePriority;
		float preyScore = 0;
		float predatorScore = 0;
		float flockScore = 0;
		
		flockInRange = new ArrayList<Instance>();
		preyInRange = new ArrayList<Instance>();
		predatorInRange = new ArrayList<Instance>();
		
		float preyRangeSquared = preyRange * preyRange;
		float predatorRangeSquared = predatorRange * predatorRange;
		float flockRangeSquared = flockRange * flockRange;
		
		Vec3 pos = controlled.getState().getPosition();
		LinkedList<Instance> objects = Render.instanceManager;
		Iterator<Instance> objectList = objects.iterator();
		
		while (objectList.hasNext()) {
			Instance object = objectList.next();
			Instance owner = object.getOwner();
			
			Vec3 objPos = object.getState().getLocation();
			float distSquared = pos.subtract(objPos).lengthSquared();
			
			if (flock != null) {
				for (int i = 0; i < flock.get(0).size(); i++) {
					Class<? extends Instance> type = flock.get(0).get(i);
					Class<? extends Instance> typeOwner = flock.get(1).get(i);
					if (type.isAssignableFrom(object.getClass()) 
							&& (owner == null || typeOwner == null || typeOwner.isAssignableFrom(owner.getClass()))
							&& distSquared < flockRangeSquared) {
						flockInRange.add(object);
					}
				}
			}
			if (prey != null) {
				for (int i = 0; i < prey.get(0).size(); i++) {
					Class<? extends Instance> type = prey.get(0).get(i);
					Class<? extends Instance> typeOwner = prey.get(1).get(i);
					if (type.isAssignableFrom(object.getClass()) 
							&& (owner == null || typeOwner == null || typeOwner.isAssignableFrom(owner.getClass()))
							&& distSquared < preyRangeSquared) {
						preyInRange.add(object);
					}
				}
			}
			if (predators != null) {
				for (int i = 0; i < predators.get(0).size(); i++) {
					Class<? extends Instance> type = predators.get(0).get(i);
					Class<? extends Instance> typeOwner = predators.get(1).get(i);
					if (type.isAssignableFrom(object.getClass()) 
							&& (owner == null || typeOwner == null || typeOwner.isAssignableFrom(owner.getClass()))
							&& distSquared < predatorRangeSquared) {
						predatorInRange.add(object);
					}
				}
			}
		}
		
		preyScore = preyInRange.size() * preyPriority;
		predatorScore = predatorInRange.size() * predatorPriority;
		int flockSize = flockInRange.size();
		if (flockSize > 5) flockSize = 5;
		flockScore = flockSize * flockPriority;
		
		float max = max(new float[] {idleScore, preyScore, predatorScore, flockScore});
		if (max == idleScore) {
			action = IDLE;
		} else if (max == preyScore) {
			action = PREY;
		} else if (max == predatorScore) {
			action = PREDATOR;
		} else if (max == flockScore) {
			action = FLOCK;
		}
		Profiler.stop("Time taken by AI");
		
	}
	
	public static float max(float[] array) {
		float max = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) max = array[i];
		} 
		return max;
	}
	
	public void setTargetType(int type) {
		TARGET_TYPE = type;
	}
	
}
