#ifndef ACTOR_H_
#define ACTOR_H_

#include "GraphObject.h"
#include "StudentWorld.h"


//Base class Actor and implementations
class Actor : public GraphObject {
public:
	virtual void doSomething() = 0;
	virtual void bonk(bool isPeach) {} //default to nothing
	virtual bool takeDamage() { return false; } //default to nothing
	Actor(StudentWorld* world, int imageID, int startX, int startY, int dir, int depth, double size, bool isSolid, bool isDamagable) :
		GraphObject(imageID, startX, startY, dir, depth, size)
	{
		m_world = world;
		m_isSolid = isSolid;
		m_isDead = false;
		m_isDamagable = isDamagable;
	}
	StudentWorld* getWorld() { return m_world; }
	bool getIsSolid() { return m_isSolid; }
	bool getIsDamagable() { return m_isDamagable; }
	void move(int x, int y);
	void setIsDead(bool isDead) { m_isDead = isDead; }
	bool getIsDead() { return m_isDead; }
	
private:
	StudentWorld* m_world;
	bool m_isSolid;
	bool m_isDead;
	bool m_isDamagable;
};

void walk(Actor* actor, bool stepOff, bool processHostile, bool turnAround, int walkingSpeed);

//OTHER CLASSES
class Peach : public Actor {
public:
	Peach(StudentWorld* world, int x, int y);
	void doSomething();
	bool takeDamage();
	void setHP(int hp) { m_hp = hp; }
	void setShootPower(bool sp) { shootPower = sp; }
	void setJumpPower(bool jp) { jumpPower = jp; }
	void setStarPower(bool sp, int time) { starPower = sp; isInvincible = sp; invincibilityTicks = time; starPowerTicks = time; }
	bool getStarPower() { return starPower; }
	bool getJumpPower() { return jumpPower; }
	bool getShootPower() { return shootPower; }

	void bonk();

private:
	int m_hp;
	bool isInvincible;
	int invincibilityTicks;
	int starPowerTicks;
	bool starPower;
	bool shootPower;
	bool jumpPower;
	int remainingJumpDistance;
	bool grounded;
	int fireRecharge;
};

class Block : public Actor {
public:
	Block(StudentWorld* world, int x, int y);
	bool getIsBonked() { return isBonked; }
	void doBonk() { isBonked = true; }
private:
	bool isBonked;
};

class NormalBlock : public Block {
public: 
	NormalBlock(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual void doSomething();
};

class FlowerBlock : public Block {
public:
	FlowerBlock(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual void doSomething();
};

class MushroomBlock : public Block {
public:
	MushroomBlock(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual void doSomething();
};

class StarBlock : public Block {
public:
	StarBlock(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual void doSomething();
};

class Pipe : public Actor {
public:
	Pipe(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Flag : public Actor {
public:
	Flag(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Mario : public Actor {
public:
	Mario(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Flower : public Actor {
public:
	Flower(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Mushroom : public Actor {
public:
	Mushroom(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Star : public Actor {
public:
	Star(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class PiranhaFireball : public Actor {
public:
	PiranhaFireball(StudentWorld* world, int x, int y, int dir);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class PeachFireball : public Actor {
public:
	PeachFireball(StudentWorld* world, int x, int y, int dir);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Shell : public Actor {
public:
	Shell(StudentWorld* world, int x, int y, int dir);
	virtual void bonk(bool isPeach);
	void doSomething();
};

class Goomba : public Actor {
public:
	Goomba(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual bool takeDamage();
	void doSomething();
};

class Koopa : public Actor {
public:
	Koopa(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual bool takeDamage();
	void doSomething();
};

class Piranha : public Actor {
public:
	Piranha(StudentWorld* world, int x, int y);
	virtual void bonk(bool isPeach);
	virtual bool takeDamage();
	void doSomething();
private:
	int m_fireDelay;
};


#endif // ACTOR_H_
