#include "MemberDatabase.h"
#include "PersonProfile.h"
#include "RadixTree.h"
#include <sstream>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>



MemberDatabase::MemberDatabase() {

}
MemberDatabase::~MemberDatabase() {
	for (int i = 0; i < profilesVector.size(); i++) {
		delete(profilesVector.at(i));
	}
}
bool MemberDatabase::LoadDatabase(std::string filename) {
	std::ifstream infile;
	infile.open(filename);
	if (!infile) {
		std::cerr << "ERROR OPENING TRANSLATOR FILE" << std::endl;
		exit(1);
	}


	std::string line;
	std::string name;
	std::string email;
	while (getline(infile, line)) {
		if (line.empty()) continue; //ignore blank lines

		name = line;
		getline(infile, line);
		email = line;
		getline(infile, line);
		PersonProfile* profile = new PersonProfile(name, email);
		std::string att;
		std::string val;
		try {
			int numAtt = std::stoi(line);
			for (int i = 0; i < numAtt; i++) {
				getline(infile, line);
				att = line.substr(0, line.find_first_of(','));
				val = line.substr(line.find_first_of(',') + 1);
				profile->AddAttValPair(AttValPair(att, val));
				//add email to tree associating it to the attValPair
				if (emails.search(att + " " + val) == nullptr) {
					std::vector<std::string> a;
					a.push_back(email);
					emails.insert(att + " " + val, a);
				}
				else {
					emails.search(att + " " + val)->push_back(email);
				}
			}
			profiles.insert(email, profile); //add person's profile to the tree
			profilesVector.push_back(profile);
		}
		catch (std::invalid_argument) {
			std::cout << "error" << std::endl;
			exit(1);
		}
	}
	infile.close();
	return true;
}

const PersonProfile* MemberDatabase::GetMemberByEmail(std::string email) const {
	PersonProfile** profile = profiles.search(email);
	if (profile == nullptr) return nullptr;
	return *profile;
}

std::vector<std::string> MemberDatabase::FindMatchingMembers(const AttValPair& input) const {
	if (emails.search(input.attribute + " " + input.value) == nullptr) {
		std::vector<std::string> empty;
		return empty;
	}
	return *emails.search(input.attribute + " " + input.value);
}