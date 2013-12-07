package sagma.core.data.selection;

import java.util.ArrayList;
import java.util.LinkedList;

import sagma.core.math.Vec2;
import sagma.core.model.Instance;
import sagma.core.render.Render;

public class Selection 
{

	@SuppressWarnings("deprecation")
	public static Instance SingleSelect(Vec2 position)
	{
		Instance output = new Instance();
		LinkedList<Instance> instances = Render.instanceManager;
		int size = instances.size();
		Vec2 screenVec;
		for(int i = 0; i < size;i++)
		{
			screenVec= Render.getScreenCoordOfPoint(instances.get(i).getPosition());
			if(screenVec == position && instances.get(i).isPickable())
			{
				output = instances.get(i);
				i = size;
			}
		}
		return output;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<Instance> BoxSelect(Vec2 startVec, Vec2 endVec)
	{
		LinkedList<Instance> instances = Render.instanceManager;

		int instanceCount = instances.size();
		ArrayList<Instance> output = new ArrayList<Instance>();
		Vec2 screenVec;
		boolean down = false;
		boolean left = false;

		//attain state of box
		//left/right
		if(startVec.x > endVec.x)
		{
			left = true;
		}
		//up/down
		if(startVec.y > endVec.y)
		{
			down = true;
		}
		//iterate through the list of instances returning list of selected instances
		for(int i =0; i < instanceCount; i++)
		{
			screenVec= Render.getScreenCoordOfPoint(instances.get(i).getPosition());

			if(left&&down)
			{
				if(screenVec.x >=endVec.x && screenVec.x < startVec.x &&
						screenVec.y >=endVec.y && screenVec.y<= startVec.y
						&& instances.get(i).isPickable())
				{
					output.add(instances.get(i));
				}
			}
			else if(!left&&down)
			{
				if(screenVec.x >=startVec.x && screenVec.x < endVec.x &&
						screenVec.y >=endVec.y && screenVec.y<= startVec.y
						&& instances.get(i).isPickable())
				{
					output.add(instances.get(i));
				}
			}
			else if(!left&&!down)
			{
				if(screenVec.x >=startVec.x && screenVec.x < endVec.x &&
						screenVec.y >=startVec.y && screenVec.y<= endVec.y
						&& instances.get(i).isPickable())
				{
					output.add(instances.get(i));
				}
			}
			else if(left&&!down)
			{
				if(screenVec.x >=endVec.x && screenVec.x < startVec.x &&
						screenVec.y >=startVec.y && screenVec.y<= endVec.y
						&& instances.get(i).isPickable())
				{
					output.add(instances.get(i));
				}
			}

		}
		return output;
	}

}
