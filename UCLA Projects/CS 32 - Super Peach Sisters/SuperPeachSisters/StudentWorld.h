#ifndef STUDENTWORLD_H_
#define STUDENTWORLD_H_

#include "GameWorld.h"
#include "Level.h"
#include <string>
#include <vector>

class Actor;
class Peach;

class StudentWorld : public GameWorld
{
public:
  StudentWorld(std::string assetPath);
  ~StudentWorld();
  virtual int init();
  virtual int move();
  virtual void cleanUp();
  bool checkEntityCollision(Actor* actor, int x, int y, bool damage, bool checkMulti, bool isPeach);
  bool checkSolidCollision(int x, int y, bool callBonk, bool isPeach);
  bool checkPeachCollision(int x, int y);
  Peach* getPeach() { return peach; }
  std::vector<Actor*>*  getLevelData() { return &levelData; }
  void completeLevel() { levelComplete = true; }
  void win() { playerWon = true; }
private:
	std::vector<Actor*> levelData;
	Peach* peach;
	bool levelComplete = false;
	bool playerWon = false;
	int levelNumber;
};

#endif // STUDENTWORLD_H_
