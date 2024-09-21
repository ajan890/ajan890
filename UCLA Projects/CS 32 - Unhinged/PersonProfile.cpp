#include "PersonProfile.h"
#include <string>
#include <vector>



PersonProfile::PersonProfile(std::string name, std::string email) {
	m_name = name;
	m_email = email;
	m_numPairs = 0;
}
PersonProfile::~PersonProfile() {

	//for (int i = 0; i < attValVector.size(); i++) {
	//	delete(&attValVector.at(i));
	//}


	
	//idt i need to delete vector since it's strings
}

std::string PersonProfile::GetName() const { return m_name; }

std::string PersonProfile::GetEmail() const { return m_email; }

void PersonProfile::AddAttValPair(const AttValPair& attval) {
	std::string code = attval.attribute + std::to_string(m_numPairs);
	tree.insert(code, attval);
	attValVector.push_back(attval);
	keys.push_back(code);
	m_numPairs++;
}
int PersonProfile::GetNumAttValPairs() const {
	return m_numPairs;
}
bool PersonProfile::GetAttVal(int attribute_num, AttValPair& attval) const {
	AttValPair* temp = tree.search(keys.at(attribute_num));
	if (temp == nullptr) return false;
	attval = *temp;
	return true;
}