#include "AttributeTranslator.h"
#include "provided.h"
#include "RadixTree.h"
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
#include <iostream>



AttributeTranslator::AttributeTranslator() {
	//std::cout << "test" << std::endl;

}
AttributeTranslator::~AttributeTranslator() {
	
}
bool AttributeTranslator::Load(std::string filename) {
	std::ifstream infile;
	infile.open(filename);
	if (!infile) {
		std::cerr << "ERROR OPENING TRANSLATOR FILE" << std::endl;
		exit(1);
	} 


	std::string line;
	while (getline(infile, line)) {
		std::stringstream ss(line);
		std::string sAtt;
		std::string sVal;
		std::string cAtt;
		std::string cVal;
		for (int i = 0; i < 4; i++) {
			std::getline(ss, line, ',');
			switch (i) {
			case 0: sAtt = line; break;
			case 1: sVal = line; break;
			case 2: cAtt = line; break;
			case 3: cVal = line; break;
			}
		}
		AttValPair temp = AttValPair(cAtt, cVal);
		if (tree.search(sAtt + " " + sVal) == nullptr) {
			std::vector<AttValPair> storeVector;
			storeVector.push_back(temp);
			tree.insert(sAtt + " " + sVal, storeVector);
		}
		else {
			tree.search(sAtt + " " + sVal)->push_back(temp);
		}

	}
	infile.close();
	return true;

}

std::vector<AttValPair> AttributeTranslator::FindCompatibleAttValPairs(const AttValPair& source) const {
	std::vector<AttValPair> empty;
	std::vector<AttValPair>* toReturn;
	toReturn = tree.search(source.attribute + " " + source.value);
	if (toReturn == nullptr) return empty;
	return *toReturn; //can't just return value of toReturn because if toReturn is NULLPTR then there would be a memory access error
}
