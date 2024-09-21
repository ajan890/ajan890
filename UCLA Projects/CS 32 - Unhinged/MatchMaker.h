#include <string>
#include <vector>
#include "MemberDatabase.h"
#include "AttributeTranslator.h"
#include "provided.h"

class MatchMaker {
public:
	MatchMaker(const MemberDatabase& mdb, const AttributeTranslator& at);
	~MatchMaker();
	std::vector<EmailCount> IdentifyRankedMatches(std::string email, int threshold) const;

private:
	const MemberDatabase* database;
	const AttributeTranslator* translator;
};
bool compareEmails(EmailCount a, EmailCount b);
