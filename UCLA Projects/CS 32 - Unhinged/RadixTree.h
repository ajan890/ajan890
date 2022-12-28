#ifndef RADIXTREE
#define RADIXTREE

#include <string>
#include <vector>
#include <list>

const int MAX_ARRAY_SIZE = 128;

template <typename ValueType>
class RadixTree {
public:
    RadixTree();
    ~RadixTree();
    void insert(std::string key, const ValueType& value);
    ValueType* search(std::string key) const;
private:



    struct Node {
        std::string m_key;
        ValueType m_value;
        bool m_canEnd;
        Node* children[MAX_ARRAY_SIZE];
        std::string childrenStrings[MAX_ARRAY_SIZE];
        Node() {
            m_canEnd = false;
            for (int i = 0; i < MAX_ARRAY_SIZE; i++) {
                children[i] = nullptr;
            }
        }
        ~Node() {
            for (int i = 0; i < MAX_ARRAY_SIZE; i++) {
                delete children[i];
            }
        }
        void insertRecursive(Node* root, std::string key, const ValueType& value) {
            if (key.empty()) { //if the key is empty, then the entirety of the key fit into the previous node or is empty to begin with
                root->m_canEnd = true;
                root->m_value = value;
                return;
            }
            int current = key[0]; //first letter of key
            if (root->children[current] == nullptr) { //if the root does not contain the current first letter, then add a new node from the root
                root->children[current] = new Node;
                root->childrenStrings[current] = key;
                insertRecursive(root->children[current], "", value); //add end node
                return;
            }

            //if at least one letter is shared, find out how many letters the key shares with current key in node
            std::string shared = root->childrenStrings[current];
            int matchLength = 0;
            int maxLength;
            if (shared.length() < key.length()) {
                maxLength = shared.length();
            }
            else maxLength = key.length();
            //matchLength is length of string in common with both key and current node key
            while (matchLength < maxLength && static_cast<int>(shared[matchLength]) == key[matchLength]) {
                matchLength++;
            }

            if (matchLength == key.length()) { //if shared string contains key 
                Node* temp = new Node;
                temp->children[static_cast<int>(shared[matchLength])] = root->children[current];
                temp->childrenStrings[static_cast<int>(static_cast<int>(shared[matchLength]))] = shared.substr(matchLength); //create new node for ending part of current node
                root->children[current] = temp;
                root->childrenStrings[current] = key;
                insertRecursive(root->children[current], "", value); //add end node
            }
            else if (matchLength == shared.length()) { //if key contains the shared string (shared part + more)
                insertRecursive(root->children[current], key.substr(matchLength), value); //insert new node for leftover part
            }
            else { //split node; part of key matched part of node
                Node* temp = new Node;
                temp->children[static_cast<int>(shared[matchLength])] = root->children[current];
                temp->childrenStrings[static_cast<int>(shared[matchLength])] = shared.substr(matchLength);
                root->children[current] = temp;
                root->childrenStrings[current] = key.substr(0, matchLength);
                insertRecursive(root->children[current], key.substr(matchLength), value);
            }
        }
        Node* searchRecursive(Node* root, std::string key) {
            if (key.empty()) { //if empty, then return nullptr default
                if (root->m_canEnd) return root;
                return nullptr;
            }
            int current = key[0];
            if (root->children[current] == nullptr) { //key first letter does not exist in tree, return nullptr
                return nullptr;
            }
            //find matching part length with node
            std::string shared = root->childrenStrings[current];
            int length;
            if (shared.length() < key.length()) {
                length = shared.length();
            }
            else length = key.length();
            //if key does not match node string, return nullptr
            if (shared.substr(0, length) != key.substr(0, length)) {
                return nullptr;
            }
            //if part of key matches entire node, search next node
            if (shared.length() == length) {
                return searchRecursive(root->children[current], key.substr(shared.length()));
            }
            else {  //if key ends before node + others
                return nullptr;
            }
        }
    };

    Node* root;
};


//RADIXTREE IMPLEMENTATION
template<typename ValueType>
RadixTree<ValueType>::RadixTree() {
    root = new Node();
}

template<typename ValueType>
RadixTree<ValueType>::~RadixTree() {
    delete root;
}

template<typename ValueType>
void RadixTree<ValueType>::insert(std::string key, const ValueType& value) {
    root->insertRecursive(root, key, value);
}

template<typename ValueType>
ValueType* RadixTree<ValueType>::search(std::string key) const {
    Node* result = nullptr;
    result = root->searchRecursive(root, key);
    if (result == nullptr) {
        return nullptr;
    }
    else {
        return (&result->m_value);
    }
}

#endif


