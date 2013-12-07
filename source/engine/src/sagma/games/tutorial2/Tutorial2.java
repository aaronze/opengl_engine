package sagma.games.tutorial2;

import java.awt.event.KeyEvent;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.event.CollisionAction;
import sagma.core.event.CollisionEventAction;
import sagma.core.input.KeyBind;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.render.Game;

public class Tutorial2 extends Game {
	float MOVE_SPEED = 0.05f;
	float JUMP_SPEED = 0.07f;
	int NUMBER_OF_BLOCKS = 10;
	int MAX_JUMPS = 1;
	Vec3 GRAVITY = new Vec3(0, -0.005f, 0);
	
	VectorGenerator RANDOM_LOCATION = new RandomVectorGenerator(-1, 1, -1, 1, 0, 0);
	
	Model playerModel;
	Model blockModel;
	
	Player player;
	int jumpsTaken = 0;

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_2D);
		
		playerModel = makeModel("Models/fern.obj");
		blockModel = makeModel("Models/fern.obj");
		
		player = new Player();
		add(player);
		
		for (int i = 0; i < NUMBER_OF_BLOCKS; i++) {
			Block block = new Block();
			RANDOM_LOCATION.setNextVector(this, block.getLocation());
			add(block);
		}
		
		add(new MoveLeft());
		add(new MoveRight());
		add(new Jump());
		
		add(new CollisionAction(Player.class, Block.class, new PlayerCollidesWithBlock()));
	}
	
	class Player extends Instance {
		public Player() {
			super(playerModel);
			setSize(0.1f);
			addAcceleration(GRAVITY);
		}
	}
	
	class Block extends Instance {
		public Block() {
			super(blockModel);
			setSize(0.1f);
		}
	}
	
	class MoveRight extends KeyBind {
		@Override
		public boolean isKey(int key) {
			if (key == KeyEvent.VK_RIGHT) return true;
			if (key == KeyEvent.VK_D) return true;
			return false;
		}

		@Override
		public void keyPressed() {
			player.addSpeed(MOVE_SPEED, 0, 0);
		}

		@Override
		public void keyReleased() {
			Vec3 speed = player.getState().getSpeed();
			player.setSpeed(0, speed.y, speed.z);
		}
		
	}
	
	class MoveLeft extends KeyBind {
		@Override
		public boolean isKey(int key) {
			if (key == KeyEvent.VK_LEFT) return true;
			if (key == KeyEvent.VK_A) return true;
			return false;
		}

		@Override
		public void keyPressed() {
			player.addSpeed(-MOVE_SPEED, 0, 0);
		}

		@Override
		public void keyReleased() {
			Vec3 speed = player.getState().getSpeed();
			player.setSpeed(0, speed.y, speed.z);
		}
	}
	
	class Jump extends KeyBind {

		@Override
		public boolean isKey(int key) {
			if (key == KeyEvent.VK_UP) return true;
			if (key == KeyEvent.VK_SPACE) return true;
			if (key == KeyEvent.VK_W) return true;
			return false;
		}

		@Override
		public void keyPressed() {
			if (jumpsTaken < MAX_JUMPS) {
				player.addSpeed(0, JUMP_SPEED, 0);
				jumpsTaken++;
			}
		}

		@Override
		public void keyReleased() {
		}
		
	}
	
	class PlayerCollidesWithBlock implements CollisionEventAction {

		@Override
		public void actionEvent(Object a, Object b) {
			Player p = (Player)a;
			Block block = (Block)b;
			
			Direction direction = directionFrom(block, p);
			
			if (direction == Direction.DIR_UP) {
				Vec3 speed = p.getState().getSpeed();
				p.setSpeed(speed.x, -GRAVITY.y, speed.z);
				jumpsTaken = 0;
			} else if (direction == Direction.DIR_DOWN) {
				Vec3 speed = p.getState().getSpeed();
				if (speed.y > 0) {
					p.setSpeed(speed.x, GRAVITY.y, speed.z);
				}
			} else {
				Vec3 speed = p.getState().getSpeed();
				p.setSpeed(0, speed.y, speed.z);
			}
		}
		
	}

}
