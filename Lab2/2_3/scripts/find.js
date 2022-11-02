function checkPalindrome(string) {

    // find the length of a string
    const len = string.length;

    // loop through half of the string
    for (let i = 0; i < len / 2; i++) {

        // check if first and last string are same
        if (string[i] !== string[len - 1 - i]) {
            return false;
        }
    }
    return true;
}
function find() {
      

    cursor = db.phones.find();
    
    while ( cursor.hasNext() ) {
        x = cursor.next()
        if(checkPalindrome(x._id.toString())){
            printjson(x);
        }
        
    }
}