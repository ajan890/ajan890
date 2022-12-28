#include "Actor.h"
#include "StudentWorld.h"

#include <iostream> //DELETE THIS

//ACTOR METHODS
void Actor::move(int x, int y) {
	moveTo(getX() + x, getY() + y);
}

//PEACH IMPLEMENTATION
Peach::Peach(StudentWorld* world, int x, int y) :
	Actor(world, IID_PEACH, x, y, 0, 0, 1, false)
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
	return false;
}

void Peach::doSomething() {
	if (getIsDead()) return;
	if (starPowerTicks > 0) {
		invincibilityTicks--;
		starPowerTicks--;
		isInvincible = true;
	}
	else {
		isInvincible = false;
	}
	if (fireRecharge > 0) {
		fireRecharge--;
	}

	//RECHARGE (fireballs ig)
	//GROUND DETECTION
	if (this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() - 1, false) || this->getWorld()->checkSolidCollision(this->getX(), this->getY() - 1, false)) { //check both sides of sprite to prevent clipping
		grounded = true;
	} else { 
		grounded = false;
	}
	if (remainingJumpDistance > 0) { 
		remainingJumpDistance--;
		//check jump top of sprite collision
		if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() + SPRITE_HEIGHT, true) && !this->getWorld()->checkSolidCollision(this->getX(), this->getY() + SPRITE_HEIGHT, true)) { //check both sides of sprite to prevent clipping
			move(0, 4);
		}
		else {
			remainingJumpDistance = 0;
		}
	}
	if (!grounded && remainingJumpDistance == 0) {
		for (int i = 3; i >= 0; i--) {
			if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH - 1, this->getY() - i, false) && !this->getWorld()->checkSolidCollision(this->getX(), this->getY() - i, false)) {
				move(0, -i);
				break;
			}
		}

	} 
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
				//move down
				break;
			case KEY_PRESS_LEFT:
				//move left
				setDirection(left);
				if (!this->getWorld()->checkSolidCollision(this->getX() - 1, this->getY(), false)) {
					move(-4, 0);
				}
				else {
					this->getWorld()->playSound(SOUND_PLAYER_BONK);
				}
				break;
			case KEY_PRESS_RIGHT:
				//move right
				setDirection(right);
				if (!this->getWorld()->checkSolidCollision(this->getX() + SPRITE_WIDTH, this->getY(), false)) {
					move(4, 0);
				}
				else {
					this->getWorld()->playSound(SOUND_PLAYER_BONK);
				}
				break;
			default:
				break;
			}
			

			

		}
}

//CONSTRUCTORS FOR ALL OTHER CLASSES

Block::Block(StudentWorld* world, int x, int y) :
	Actor(world, IID_BLOCK, x, y, 0, 2, 1, true) {
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
	Actor(world, IID_PIPE, x, y, 0, 2, 1, true) {

}

Flag::Flag(StudentWorld* world, int x, int y) :
	Actor(world, IID_FLAG, x, y, 0, 1, 1, false) {

}

Mario::Mario(StudentWorld* world, int x, int y) :
	Actor(world, IID_MARIO, x, y, 0, 1, 1, false) {

}

Flower::Flower(StudentWorld* world, int x, int y) :
	Actor(world, IID_FLOWER, x, y, 0, 1, 1, false) {

}

Mushroom::Mushroom(StudentWorld* world, int x, int y) :
	Actor(world, IID_MUSHROOM, x, y, 0, 1, 1, false) {

}

Star::Star(StudentWorld* world, int x, int y) :
	Actor(world, IID_STAR, x, y, 0, 1, 1, false) {

}

PiranhaFireball::PiranhaFireball(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_PIRANHA_FIRE, x, y, dir, 1, 1, false) {

}

PeachFireball::PeachFireball(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_PEACH_FIRE, x, y, dir, 1, 1, false) {

}

Shell::Shell(StudentWorld* world, int x, int y, int dir) :
	Actor(world, IID_SHELL, x, y, dir, 1, 1, false) {

}

Goomba::Goomba(StudentWorld* world, int x, int y) :
	Actor(world, IID_GOOMBA, x, y, (randInt(0, 1) * 180), 0, 1, false) {
	
}

Koopa::Koopa(StudentWorld* world, int x, int y) :
	Actor(world, IID_KOOPA, x, y, (randInt(0, 1) * 180), 0, 1, false) {

}

Piranha::Piranha(StudentWorld* world, int x, int y) :
	Actor(world, IID_PIRANHA, x, y, (randInt(0, 1) * 180), 0, 1, false) {
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
	//do nothing
}
void Mario::doSomething() {
	//do nothing
}
void Flower::doSomething() {
	//move 
}
void Mushroom::doSomething() {
	//move 
}
void Star::doSomething() {
	//move 
}
void PiranhaFireball::doSomething() {
	//move 
}
void PeachFireball::doSomething() {
	//move 
}
void Shell::doSomething() {
	//move 
}
void Goomba::doSomething() {
	//move 
}
void Koopa::doSomething() {
	//move 
}
void Piranha::doSomething() {
	//spit fireball
}

//BONK METHODS
void Peach::bonk() {
	//do nothing
}

void NormalBlock::bonk() {
	this->getWorld()->playSound(SOUND_PLAYER_BONK);
}

void FlowerBlock::bonk() {
	if (!getIsBonked() && this->getWorld()->getPeach()->getY() < getY()) { //the part after && is to make sure the player hits block from bottom
		getWorld()->getLevelData().push_back(new Flower(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void MushroomBlock::bonk() {
	if (!getIsBonked() && this->getWorld()->getPeach()->getY() < getY()) { //the part after && is to make sure the player hits block from bottom
		getWorld()->getLevelData().push_back(new Mushroom(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void StarBlock::bonk() {
	if (!getIsBonked() && this->getWorld()->getPeach()->getY() < getY()) { //the part after && is to make sure the player hits block from bottom
		getWorld()->getLevelData().push_back(new Star(getWorld(), getX(), getY() + 8));
		this->getWorld()->playSound(SOUND_POWERUP_APPEARS);
		doBonk();
	}
	else {
		this->getWorld()->playSound(SOUND_PLAYER_BONK);
	}
}

void Pipe::bonk() {
	//do nothing
}
void Flag::bonk() {
	//end level
}
void Mario::bonk() {
	//end game
}
void Flower::bonk() {
	//grant fire power
}
void Mushroom::bonk() {
	//grant jump power
}
void Star::bonk() {
	//grant invincibility
}
void PiranhaFireball::bonk() {
	//check of i-frames
	//deal damage
	//destroy itself
}
void PeachFireball::bonk() {
	//stuff
}
void Shell::bonk() {
	//move shell
}
void Goomba::bonk() {
	//check for i-frames
	//deal damage
}
void Koopa::bonk() {
	//deal damamge
	//turn into shell
}
void Piranha::bonk() {
	//check for i-frames
	//deal damage
}