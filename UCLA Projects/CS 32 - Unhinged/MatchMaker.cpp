
#include "MatchMaker.h"
#include "PersonProfile.h"
#include "provided.h"
#include "RadixTree.h"
#include <map>
#include <vector>
#include <string>
#include <algorithm>

//DELETE
#include <iostream>


struct EmailCount;
bool compareEmails(EmailCount a, EmailCount b) {
	if (a.count > b.count) return true;
	if (a.count < b.count) return false;
	return a.email < b.email;
}

MatchMaker::MatchMaker(const MemberDatabase& mdb, const AttributeTranslator& at) {
	database = &mdb;
	translator = &at;
}
MatchMaker::~MatchMaker() {
	//delete(database);
	//delete(translator);
}


std::vector<EmailCount> MatchMaker::IdentifyRankedMatches(std::string email, int threshold) const {
	const PersonProfile* profile = database->GetMemberByEmail(email);
	if (profile->GetNumAttValPairs() == 0) {
		std::vector<EmailCount> empty;
		return empty;
	}
	else {
		std::vector<EmailCount> allEmails;
		std::vector<std::string> emailList;
		RadixTree<int> compatible;
		std::vector<AttValPair> compatibleInserted;
		std::map<std::string, int> emailMap;

		for (int i = 0; i < profile->GetNumAttValPairs(); i++) {
			AttValPair attVal;
			profile->GetAttVal(i, attVal);
			std::vector<AttValPair> temp = translator->FindCompatibleAttValPairs(attVal);
			for (int i = 0; i < temp.size(); i++) {
				if (compatible.search(temp[i].attribute + " " + temp[i].value) == nullptr) { //if attValPair isn't in tree already
					compatible.insert(temp[i].attribute + " " + temp[i].value, 0); //insert it
					compatibleInserted.push_back(temp[i]);
				} //if it is already in tree, don't add again.
			}
		}
		//at this point, the vector compatibleInserted should have every compatible attValPair exactly once
		for (int i = 0; i < compatibleInserted.size(); i++) {
			emailList = database->FindMatchingMembers(compatibleInserted.at(i));
			for (int j = 0; j < emailList.size(); j++) {
				emailMap[emailList.at(j)]++;
			}
		}

		for (auto it = emailMap.begin(); it != emailMap.end(); it++) {
			if (it->second >= threshold) allEmails.push_back(EmailCount(it->first, it->second));
		}

		//sort vector
		std::sort(allEmails.begin(), allEmails.end(), compareEmails);
		return allEmails;
	}
}
