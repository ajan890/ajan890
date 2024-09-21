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
  bool checkSolidCollision(int x, int y, bool callBonk);
  Peach* getPeach() { return peach; }
  std::vector<Actor*> getLevelData() { return levelData; }
private:
	std::vector<Actor*> levelData;
	Peach* peach;
};

#endif // STUDENTWORLD_H_
