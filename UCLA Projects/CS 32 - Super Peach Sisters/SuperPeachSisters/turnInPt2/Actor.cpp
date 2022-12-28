#include "Actor.h"
#include "StudentWorld.h"

//ACTOR METHODS
void Actor::move(int x, int y) {
	moveTo(getX() + x, getY() + y);
}

//walking algorithm for powerups, goombas, koopas
//if stepOff is true, then the actor is allowed to walk off the edge
//if processHostile is true, then actor will check for collision for peach.  if not, then either the actor is not hostile, or collision and actions are done separately
//if turnAround is true, then the actor is allowed to turn around when hitting a wall.  If false, actor will die upon hitting a wall
void walk(Actor* actor, bool stepOff, bool processHostile, bool turnAround, int walkingSpeed) {
	if (actor->getIsDead()) return;

	//check peach collision (all corners)
	if (processHostile && (actor->getWorld()->checkPeachCollision(actor->getX(), actor->getY()) || actor->getWorld()->checkPeachCollision(actor->getX() + SPRITE_WIDTH - 1, actor->getY()) || actor->getWorld()->checkPeachCollision(actor->getX() + SPRITE_WIDTH - 1, actor->getY() + SPRITE_HEIGHT - 1) || actor->getWorld()->checkPeachCollision(actor->getX(), actor->getY() + SPRITE_HEIGHT - 1))) {
		actor->getWorld()->getPeach()->bonk();
		return;
	}

	//check walking
	bool canMoveRight = false;
	bool canMoveLeft = false;
	if (!actor->getWorld()->checkSolidCollision(actor->getX() + SPRITE_WIDTH, actor->getY(), false, false)) { //check block directly on right
		if (actor->getWorld()->checkSolidCollision(actor->getX() + SPRITE_WIDTH, actor->getY() - 1, false, false)) { //check for ground
			canMoveRight = true;
		}
		if (stepOff) canMoveRight = true;
	}
	else if (!turnAround && actor->getDirection() == 0) { //if block on right is solid, actor is going right and can't turn around, kill actor.
		actor->setIsDead(true);
	}
	if (!actor->getWorld()->checkSolidCollision(actor->getX() - 1, actor->getY(), false, false)) { //check block directly on left
		if (actor->getWorld()->checkSolidCollision(actor->getX() - 1, actor->getY() - 1, false, false)) { //check for ground
			canMoveLeft = true;
		}
		if (stepOff) canMoveLeft = true;
	}
	else if (!turnAround && actor->getDirection() == 180) { //if block on left is solid, actor is going left and can't turn around, kill actor.
		actor->setIsDead(true);
	}

	//check if on floor.  If not, then fall down walkingSpeed number of pixels, or highest number of pixels below walkingSpeed
	if (!actor->getWorld()->checkSolidCollision(actor->getX() + SPRITE_WIDTH - 1, actor->getY() - 1, false, false) && !actor->getWorld()->checkSolidCollision(actor->getX(), actor->getY() - 1, false, false)) {
		for (int i = walkingSpeed; i >= 0; i--) {
			if (!actor->getWorld()->checkSolidCollision(actor->getX() + SPRITE_WIDTH - 1, actor->getY() - i, false, false) && !actor->getWorld()->checkSolidCollision(actor->getX(), actor->getY() - i, false, false)) {
				actor->move(0, -i);
				break;
			}
		}
	}

	//walk
	if (actor->getDirection() == 0) {
		if (canMoveRight) {
			actor->move(walkingSpeed, 0);
		}
		else if (canMoveLeft) {
			actor->setDirection(180);
			actor->move(-walkingSpeed, 0);
		}
		else {
			actor->setDirection(180);
		}
	}
	else {
		if (canMoveLeft) {
			actor->move(-walkingSpeed, 0);
		}
		else if (canMoveRight) {
			actor->setDirection(0);
			actor->move(walkingSpeed, 0);
		}
		else {
			actor->setDirection(0);
		}
	}
}


//PEACH IMPLEMENTATION
Peach::Peach(StudentWorld* world, int x, int y) :
	Actor(world, IID_PEACH, x, y, 0, 0, 1, false, false) //peach CAN take damage, but not from shells or peach fireballs
{
	setIsDead(false);
	m_hp = 1;
	isInvincible = false;
	starPower = false;
	shootPower = false;
	jumpPower = false;
	grounded = false;
	remainingJumpDistance = 0;
	fireRecharge = 0;
	invincibilityTicks = 0;
	starPowerTicks = 0;
}

bool Peach::takeDamage() { //true if peach died, false otherwise
	m_hp--;
	if (m_hp == 0) {
		setIsDead(true);
		return true;
	}
	isInvincible = true;
	invincibilityTicks = 10;
	shootPower = false;
	jumpPower = false;
	this->getWorld()->playSound(SOUND_PLAYER_HURT);
	return false;
}

void Peach::doSomething() {
	//check if dead
	if (getIsDead()) return;
	//check invincibility ticks
	if (invincibilityTicks > 0) {
		invincibilityTicks--;
		isInvincible = true;
	}
	else {
		isInvincible = false;
	}
	//check star power ticks (always initialized to equal invincibility)
	if (starPowerTicks > 0) { 
		starPowerTicks--; 
		//check enemies hitboxes; damage them if peach has star power.
		this->getWorld()->checkEntityCollision(this, this->getX(), this->getY(), true, true, true);
		this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY(), true, true, true);
		this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT - 1, true, true, true);
		this->getWorld()->checkEntityCollision(this, this->getX(), this->getY() + SPRITE_HEIGHT - 1, true, true, true);
	}
	else { starPower = false; }
	//check fireball cooldown
	if (fireRecharge > 0) {
		fireRecharge--;
	}

	//SOLID BLOCK DETECTION
	if (this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() - 1, false, true) || this->getWorld()->checkSolidCollision(this->getX(), this->getY() - 1, false, true)) { //check both sides of sprite to prevent clipping
		grounded = true;
	} else { 
		grounded = false;
	}
	if (remainingJumpDistance > 0) { 
		remainingJumpDistance--;
		//check jump top of sprite collision
		if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT, true, true) && !this->getWorld()->checkSolidCollision(this->getX(), this->getY() + SPRITE_HEIGHT, true, true)) { //check both sides of sprite to prevent clipping
			move(0, 4);
		}
		else {
			remainingJumpDistance = 0;
		}
	}
	//check if grounded (or fall)
	if (!grounded && remainingJumpDistance == 0) {
		for (int i = 4; i >= 0; i--) {
			if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() - i, false, true) && !this->getWorld()->checkSolidCollision(this->getX(), this->getY() - i, false, true)) {
				move(0, -i);
				break;
			}
		}

	} 
	//KEYSTROKES
	int keyPressed;
		if (getWorld()->getKey(keyPressed)) {
			switch (keyPressed) {
			case KEY_PRESS_UP: 
				//jump
				this->getWorld()->playSound(SOUND_PLAYER_JUMP);
				if (grounded) { //you can only jump when on the ground
					if (!jumpPower) {
						remainingJumpDistance = 8;
					}
					else {
						remainingJumpDistance = 12;
					}
				}
				break;
			case KEY_PRESS_DOWN:
				//nothing
				break;
			case KEY_PRESS_LEFT:
				//move left
				setDirection(left);
				//check top and bottom points to prevent clipping while falling
				if (!this->getWorld()->checkSolidCollision(this->getX() - 1, this->getY(), false, true) && !this->getWorld()->checkSolidCollision(this->getX() - 1, this->getY() + SPRITE_HEIGHT - 1, false, true)) {
					move(-4, 0);
				}
				else {
					this->getWorld()->playSound(SOUND_PLAYER_BONK);
				}
				break;
			case KEY_PRESS_RIGHT:
				//move right
				setDirection(right);
				//check top and bottom points to prevent clipping while falling
				if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH, this->getY(), false, true) && !this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH, this->getY() + SPRITE_HEIGHT - 1, false, true)) {
					move(4, 0);
				}
				else {
					this->getWorld()->playSound(SOUND_PLAYER_BONK);
				}
				break;
			case KEY_PRESS_SPACE:
				//shoot fireball
				if (!shootPower) { break; }
				if (fireRecharge > 0) { break; }
				this->getWorld()->playSound(SOUND_PLAYER_FIRE);
				this->fireRecharge = 8;
				if (getDirection() == right) {
					if (!this->getWorld()->checkSolidCollision(this->getX() + 4, this->getY(), false, true)) {
						PeachFireball* fireball = new PeachFireball(this->getWorld(), this->getX() + 4, this->getY(), getDirection());
						this->getWorld()->getLevelData()->push_back(fireball);
					}
				}
				else {
					if (!this->getWorld()->checkSolidCollision(this->getX() - 4, this->getY(), false, true)) {
						PeachFireball* fireball = new PeachFireball(this->getWorld(), this->getX() - 4, this->getY(), getDirection());
						this->getWorld()->getLevelData()->push_back(fireball);
					}
				}
				
			default:
				break;
			}
			

			

		}
}

//CONSTRUCTORS FOR ALL OTHER CLASSES

Block::Block(StudentWorld* world, int x, int y) :
	Actor(world, IID_BLOCK, x, y, 0, 2, 1, true, false) {
	isBonked = false;
}

NormalBlock::NormalBlock(StudentWorld* world, int x, int y) :
	Block(world, x, y) {

}
FlowerBlock::FlowerBlock(StudentWorld* world, int x, int y) :
	Block(world, x, y) {
}
MushroomBlock::MushroomBlock(StudentWorld* world, int x, int y) :
	Block(world, x, y) {
}
StarBlock::StarBlock(StudentWorld* world, int x, int y) :
	Block(world, x, y) {
}

Pipe::Pipe(StudentWorld* world, int x, int y) :
	Actor(world, IID_PIPE, x, y, 0, 2, 1, true, false) {

}

Flag::Flag(StudentWorld* world, int x, int y) :
	Actor(world, IID_FLAG, x, y, 0, 1, 1, false, false) {

}

Mario::Mario(StudentWorld* world, int x, int y) :
	Actor(world, IID_MARIO, x, y, 0, 1, 1, false, false) {

}

Flower::Flower(StudentWorld* world, int x, int y) :
	Actor(world, IID_FLOWER, x, y, 0, 1, 1, false, false) {

}

Mushroom::Mushroom(StudentWorld* world, int x, int y) :
	Actor(world, IID_MUSHROOM, x, y, 0, 1, 1, false, false) {

}

Star::Star(StudentWorld* world, int x, int y) :
	Actor(world, IID_STAR, x, y, 0, 1, 1, false, false) {

}

PiranhaFireball::PiranhaFireball(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_PIRANHA_FIRE, x, y, dir, 1, 1, false, false) {

}

PeachFireball::PeachFireball(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_PEACH_FIRE, x, y, dir, 1, 1, false, false) {

}

Shell::Shell(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_SHELL, x, y, dir, 1, 1, false, false) {

}

Goomba::Goomba(StudentWorld* world, int x, int y) :
	Actor(world, IID_GOOMBA, x, y, (randInt(0, 1) * 180), 0, 1, false, true) {
	
}

Koopa::Koopa(StudentWorld* world, int x, int y) :
	Actor(world, IID_KOOPA, x, y, (randInt(0, 1) * 180), 0, 1, false, true) {

}

Piranha::Piranha(StudentWorld* world, int x, int y) :
	Actor(world, IID_PIRANHA, x, y, (randInt(0, 1) * 180), 0, 1, false, true) {
	m_fireDelay = 0;
}

//DO SOMETHING FOR ALL OTHER CLASSES
void NormalBlock::doSomething() {
	//do nothing
}
void FlowerBlock::doSomething() {
	//do nothing
}
void MushroomBlock::doSomething() {
	//do nothing
}
void StarBlock::doSomething() {
	//do nothing
}
void Pipe::doSomething() {
	//do nothing
}
void Flag::doSomething() {
	if (getIsDead()) return;
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1)) {
		setIsDead(true);
		this->getWorld()->increaseScore(1000);
		this->getWorld()->completeLevel();
	}
}
void Mario::doSomething() {
	
	if (getIsDead()) return;
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->increaseScore(1000);
		setIsDead(true);
		this->getWorld()->win();
	}
	
}
void Flower::doSomething() {
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->increaseScore(50);
		this->getWorld()->getPeach()->setShootPower(true);
		this->getWorld()->getPeach()->setHP(2);
		setIsDead(true);
		this->getWorld()->playSound(SOUND_PLAYER_POWERUP);
		return;
	}
	walk(this, true, false, true, 2);
}
void Mushroom::doSomething() {
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->increaseScore(75);
		this->getWorld()->getPeach()->setJumpPower(true);
		this->getWorld()->getPeach()->setHP(2);
		setIsDead(true);
		this->getWorld()->playSound(SOUND_PLAYER_POWERUP);
		return;
	}
	walk(this, true, false, true, 2);
}
void Star::doSomething() {
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->increaseScore(100);
		this->getWorld()->getPeach()->setStarPower(true, 150);
		setIsDead(true);
		this->getWorld()->playSound(SOUND_PLAYER_POWERUP);
		return;
	}
	walk(this, true, false, true, 2);
}
void PiranhaFireball::doSomething() {
	if (this->getWorld()->checkPeachCollision(this->getX(), this->getY()) || this->getWorld()->checkPeachCollision(this->getX() + SPRITE_WIDTH - 1, this->getY()) || this->getWorld()->checkPeachCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(this->getX(), this->getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->getPeach()->bonk();
		this->setIsDead(true);
		return;
	}
	walk(this, true, false, false, 2);
}
void PeachFireball::doSomething() { 
	if (this->getWorld()->checkEntityCollision(this, this->getX(), this->getY(), true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY(), true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT - 1, true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX(), this->getY() + SPRITE_HEIGHT - 1, true, false, false)) {
		this->setIsDead(true);
		return;
	}
	walk(this, true, false, false, 2);
}

void Shell::doSomething() {
	if (this->getWorld()->checkEntityCollision(this, this->getX(), this->getY(), true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY(), true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT - 1, true, false, false) || this->getWorld()->checkEntityCollision(this, this->getX(), this->getY() + SPRITE_HEIGHT - 1, true, false, false)) {
		this->setIsDead(true);
		return;
	}
	walk(this, true, false, false, 2);
}
void Goomba::doSomething() {
	walk(this, false, true, true, 1);
}
void Koopa::doSomething() {
	walk(this, false, true, true, 1);
}
void Piranha::doSomething() {
	if (getIsDead()) return;
	increaseAnimationNumber();
	//check peach collision (all corners)
	if (this->getWorld()->checkPeachCollision(getX(), getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY()) || this->getWorld()->checkPeachCollision(getX() + SPRITE_WIDTH - 1, getY() + SPRITE_HEIGHT - 1) || this->getWorld()->checkPeachCollision(getX(), getY() + SPRITE_HEIGHT - 1)) {
		this->getWorld()->getPeach()->bonk();
		return;
	}
	if (this->getWorld()->getPeach()->getY() > this->getY() - 1.5 * SPRITE_HEIGHT && this->getWorld()->getPeach()->getY() < this->getY() + 1.5 * SPRITE_HEIGHT) {
		if (this->getWorld()->getPeach()->getX() < this->getX()) setDirection(left);
		else setDirection(right);
	} else {
		return;
	}
	if (m_fireDelay > 0) {
		m_fireDelay--;
		return;
	} else {
		if (abs(this->getWorld()->getPeach()->getX() - this->getX()) / 8 < SPRITE_WIDTH) {
			PiranhaFireball* fireball = new PiranhaFireball(this->getWorld(), this->getX(), this->getY(), this->getDirection());
			this->getWorld()->getLevelData()->push_back(fireball);
			this->getWorld()->playSound(SOUND_PIRANHA_FIRE);
			m_fireDelay = 40;
		}
	}
}

//BONK METHODS
void Peach::bonk() {
	//if has i-frames, do nothing
	//if not, take damage
	if (isInvincible) return;
	this->takeDamage();
}

void NormalBlock::bonk(bool isPeach) {
	this->getWorld()->playSound(SOUND_PLAYER_BONK);
}

void FlowerBlock::bonk(bool isPeach) {
	if (!getIsBonked()) {
		getWorld()->getLevelData()->push_back(new Flower(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void MushroomBlock::bonk(bool isPeach) {
	if (!getIsBonked()) {
		getWorld()->getLevelData()->push_back(new Mushroom(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void StarBlock::bonk(bool isPeach) {
	if (!getIsBonked()) { 
		getWorld()->getLevelData()->push_back(new Star(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void Pipe::bonk(bool isPeach) {
	this->getWorld()->playSound(SOUND_PLAYER_BONK);
}
void Flag::bonk(bool isPeach) {
	//do nothing
}
void Mario::bonk(bool isPeach) {
	//do nothing
}
void Flower::bonk(bool isPeach) {
	//do nothing
}
void Mushroom::bonk(bool isPeach) {
	//do nothing
}
void Star::bonk(bool isPeach) {
	//do nothing
}
void PiranhaFireball::bonk(bool isPeach) {
	//do nothing
}
void PeachFireball::bonk(bool isPeach) {
	//do nothing
}
void Shell::bonk(bool isPeach) {
	//do nothing
}
void Goomba::bonk(bool isPeach) {
	if (!isPeach) return;
	this->getWorld()->playSound(SOUND_PLAYER_KICK);
	takeDamage();

}
void Koopa::bonk(bool isPeach) {
	if (!isPeach) { return; }
	this->getWorld()->playSound(SOUND_PLAYER_KICK);
	this->takeDamage();
}
void Piranha::bonk(bool isPeach) {
	if (!isPeach) return;
	this->getWorld()->playSound(SOUND_PLAYER_KICK);
	takeDamage();
}

bool Goomba::takeDamage() {
	setIsDead(true);
	this->getWorld()->increaseScore(100);
	return true;
}
bool Koopa::takeDamage() {
	this->setIsDead(true);
	this->getWorld()->increaseScore(100);
	//CREATE SHELL
	Shell* shell = new Shell(this->getWorld(), this->getX(), this->getY(), this->getDirection());
	this->getWorld()->getLevelData()->push_back(shell);
	return true;
}
bool Piranha::takeDamage() {
	setIsDead(true);
	this->getWorld()->increaseScore(100);
	return true; 
} 

