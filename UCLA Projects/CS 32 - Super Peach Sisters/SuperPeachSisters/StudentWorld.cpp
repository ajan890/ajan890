#include "StudentWorld.h"
#include "GameConstants.h"
#include "Actor.h"
#include <string>
using namespace std;

const bool DEBUG_MODE = false; //change to true to print debug messages

GameWorld* createStudentWorld(string assetPath)
{
	return new StudentWorld(assetPath);
}

// Students:  Add code to this file, StudentWorld.h, Actor.h, and Actor.cpp

StudentWorld::StudentWorld(string assetPath)
: GameWorld(assetPath)
{
    peach = nullptr;
    levelNumber = 1;
}

StudentWorld::~StudentWorld() {
    cleanUp();
}
int StudentWorld::init()
{
    levelComplete = false;
    levelData.clear();
    Level lev = (assetPath());
    string pLevelNum = "";
    pLevelNum = to_string(levelNumber);
    if (pLevelNum.length() == 1) {
        pLevelNum = "0" + pLevelNum;
    }
    string level_file = "level" + pLevelNum + ".txt"  ;
    Level::LoadResult result = lev.loadLevel(level_file);
    if (result == Level::load_fail_file_not_found) {
        if (DEBUG_MODE) cerr << "Could not find level01.txt data file" << endl; 
    }
    else if (result == Level::load_fail_bad_format) {
        if (DEBUG_MODE) cerr << "level01.txt is improperly formatted" << endl;
    }
    else if (result == Level::load_success)
    {
        if (DEBUG_MODE) cerr << "Successfully loaded level!" << endl;
        Level::GridEntry ge;
        for (int i = 0; i < GRID_HEIGHT; i++) { //row
            for (int j = 0; j < GRID_WIDTH; j++) { //column
                ge = lev.getContentsOf(j, i); 
                switch (ge) {
                case Level::empty:
                    break;
                case Level::peach:
                    peach = new Peach(this, j * 8, i * 8);
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " is where Peach starts" << endl;
                    break;
                case Level::block:
                    levelData.push_back(new NormalBlock(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " holds a regular block" << endl;
                    break;
                case Level::pipe:
                    levelData.push_back(new Pipe(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " holds a pipe" << endl;
                    break;
                case Level::flag:
                    levelData.push_back(new Flag(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " is where the flag is" << endl;
                    break;
                case Level::mario:
                    levelData.push_back(new Mario(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " is where Mario is" << endl;
                    break;
                case Level::flower_goodie_block:
                    levelData.push_back(new FlowerBlock(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " holds a flower block" << endl;
                    break;
                case Level::mushroom_goodie_block:
                    levelData.push_back(new MushroomBlock(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " holds a mushroom block" << endl;
                    break;
                case Level::star_goodie_block:
                    levelData.push_back(new StarBlock(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " holds a star block" << endl;
                    break;
                case Level::goomba:
                    levelData.push_back(new Goomba(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " starts with a goomba" << endl;
                    break;
                case Level::koopa:
                    levelData.push_back(new Koopa(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " starts with a koopa" << endl;
                    break;
                case Level::piranha:
                    levelData.push_back(new Piranha(this, j * 8, i * 8));
                    if (DEBUG_MODE) cerr << "Location " << i << ", " << j << " starts with a piranha" << endl;
                    break;
                }
            }
        }
    }
    return GWSTATUS_CONTINUE_GAME;
}

int StudentWorld::move()
{
    if (DEBUG_MODE) cout << "Tick! ----------------------------------------------------" << endl;
    string displayString = "Lives: " + to_string(getLives()) + "  Level: " + to_string(getLevel()) + "  Points: " + to_string(getScore()) + " ";
    if (this->getPeach()->getStarPower()) displayString += "StarPower! ";
    if (this->getPeach()->getShootPower()) displayString += "ShootPower! ";
    if (this->getPeach()->getJumpPower()) displayString += "JumpPower! ";
    setGameStatText(displayString);
    if (levelComplete) return GWSTATUS_FINISHED_LEVEL;
    // This code is here merely to allow the game to build, run, and terminate after you hit enter.
    // Notice that the return value GWSTATUS_PLAYER_DIED will cause our framework to end the current level.
    peach->doSomething();
    for (int i = 0; i < levelData.size(); i++) {
        levelData.at(i)->doSomething();
    }

    for (int i = 0; i < levelData.size(); i++) {
        if (levelData.at(i)->getIsDead()) {
            delete(levelData.at(i));
            levelData.erase(levelData.begin() + i);
        }
    }


    if (peach->getIsDead()) {
        playSound(SOUND_PLAYER_DIE);
        decLives();
        return GWSTATUS_PLAYER_DIED;
    }
    if (playerWon) {
        playSound(SOUND_GAME_OVER);
        return GWSTATUS_PLAYER_WON;
    }
    if (levelComplete) {
        playSound(SOUND_FINISHED_LEVEL);
        levelNumber++;
        return GWSTATUS_FINISHED_LEVEL;
    }

    return GWSTATUS_CONTINUE_GAME;
}

void StudentWorld::cleanUp()
{
    delete(peach);
    for (int i = levelData.size() - 1; i >= 0; i--) { //backwards to bypass the vector deletion problem
        delete(levelData.at(i));
    }
    return;
}

//if callBonk = true, then bonk the block it collides. (ex. peach jump to hit blocks)  If false, the don't bonk the block it collides.  (ex. checking if something is grounded or not)
bool StudentWorld::checkSolidCollision(int x, int y, bool callBonk, bool isPeach) { //true if given coordinate (x, y) is in some solid hitbox.
    bool foundCollision = false;
    for (int i = 0; i < levelData.size(); i++) {
        if (levelData.at(i)->getIsSolid()) {
            if (levelData.at(i)->getX() <= x && levelData.at(i)->getX() + SPRITE_WIDTH - 1 >= x && levelData.at(i)->getY() <= y && levelData.at(i)->getY() + SPRITE_HEIGHT - 1 >= y) {
                if (!callBonk) {
                    return true;
                }
                else {
                    levelData.at(i)->bonk(isPeach);
                    foundCollision = true;
                }
            }
        }
    }
    return foundCollision; 
}

//returns true if (x, y) is within Peach's hitbox, false otherwise
bool StudentWorld::checkPeachCollision(int x, int y) {
    if (peach->getX() <= x && peach->getX() + SPRITE_WIDTH - 1 >= x && peach->getY() <= y && peach->getY() + SPRITE_HEIGHT - 1 >= y) {
        return true;
    } 
    return false;
}

//returns true if (x, y) is in a damagable entity and damages the entity if damage is true
bool StudentWorld::checkEntityCollision(Actor* actor, int x, int y, bool damage, bool checkMulti, bool isPeach) {
    bool foundCollision = false;
    for (int i = 0; i < levelData.size(); i++) {
        if (levelData.at(i) != actor && !levelData.at(i)->getIsDead() && levelData.at(i)->getIsDamagable()) {
            if (levelData.at(i)->getX() <= x && levelData.at(i)->getX() + SPRITE_WIDTH - 1 >= x && levelData.at(i)->getY() <= y && levelData.at(i)->getY() + SPRITE_HEIGHT - 1 >= y) {
                if (damage) {
                    if (isPeach) {
                        levelData.at(i)->bonk(isPeach);
                    } else {
                        levelData.at(i)->takeDamage();
                    }
                }
                if (!checkMulti) {
                    return true;
                } else {
                    foundCollision = true;
                }
            }
        }
    }
    return foundCollision;
}