package sagma.games.rts.util;

import sagma.core.model.Instance;

/**
 * This class is intended to be used by the GlobalEmitter to emit multiple instances of the same type.
 * The number of instances emitted by the global emitter will be limited by this class under certain
 * circumstances.
 * @author Michael
 *
 */
public class InstanceContainer{
	private int noOfInstances;
	private Class<? extends Instance> type;

	/**
	 * Will use the type to construct an instance using reflection when the emitter fires
	 * Will fire the noOfInstances when the emitter pulses
	 * @param modelName
	 * @param noOfInstances - the number of instances of this model the container contains
	 */
	public InstanceContainer(Class<? extends Instance> type, int noOfInstances){
		this.type = type;
		this.noOfInstances = noOfInstances;
	}

	/**
	 * Will use the modelName to construct an instance when the emmitter fires
	 * Contains only one instance of the model
	 * @param modelName
	 */
	public InstanceContainer(Class<? extends Instance> type){
		this.type = type;
		this.noOfInstances = 1;
	}

	public int getNoOfInstances() {
		return noOfInstances;
	}

	public void setNoOfInstances(int noOfInstances) {
		this.noOfInstances = noOfInstances;
	}		

	public Class<? extends Instance> getType() {
		return type;
	}

	public void setType(Class<? extends Instance> type) {
		this.type = type;
	}

}