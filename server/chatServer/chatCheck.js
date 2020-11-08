
var messageGetAttribute = ["user1", "user2", "newest", "timestamp"];
var messageAttribute = ["sender", "recipient", "timestamp", "content"];


/* Check if the get message query is valid.
 * Parameters:
 *   - query: a json object get from the get message request query.
 */
module.exports.checkMessageQuery= function(query) {
    const keys = Object.keys(query);
    for (var key of keys){
        if( !messageGetAttribute.includes(key) ){
            throw key + " is not a valid key";
        }
    }
};



/* Check if the add message is valid.
 * Parameters:
 *   - message: a json object represents the message.
 */
module.exports.checkMessage= function(message) {
    const keys = Object.keys(message);
    for (var key of keys){
        if( !messageAttribute.includes(key) ){
            throw key + " is not a valid key";
        }
    }

    if (typeof(message.content) !== "string"){
        throw "Message should be a string, not a " + typeof(message.content) + ".";
    }
};
