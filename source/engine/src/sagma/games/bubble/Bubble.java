package sagma.games.bubble;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.fluid.Fluid;
import sagma.core.material.Material;
import sagma.core.math.Vec3;
import sagma.core.render.Game;
import sagma.core.render.Render;

public class Bubble extends Game {
	Fluid fluid;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		disableMouse();
		
		setCameraLocation(0, 0, -10);
		
		Render.MOTION_BLUR = 0.5f;
		Render.POINTER_ACCURACY = 0.0f;
		
		fluid = new Fluid(100);
		fluid.setBounds(new Vec3(-2, -2, -2), new Vec3(2, 2, 2));
		fluid.setMaterial(Material.getMaterial("Shaders/wave"));
		
		RandomVectorGenerator gen = new RandomVectorGenerator(-1, 1, -1, 1, -1, 1);
		for (int i = 0; i < 100; i++) {
			Vec3 pos = gen.nextVector(this);
			Vec3 speed = new Vec3(0, 0, 0);
			
			fluid.addFluidPoint(pos, speed);
		}
		
		add(fluid);
	}
	
	public void heartbeat() {
		if (keyIsDown(Q)) {
			fluid.start();
		}
		if (keyIsDown(W)) {
			fluid.stop();
		}
	}
}