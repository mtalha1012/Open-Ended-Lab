Design choices: \
1. getInt(), getDouble() and other helper functions defined to facilitate user input
2. Custom NotFoundException defined to be used when an entity is not found in the database instead of custom exceptions for each error
3. Exception handling done in constructor to prevent exceeding recipes of a chef
4. Added attribute maxRecipes in Chef to keep track of recipes for junior and senior chef
5. Made Chef abstract to prevent initialization as each chef must be senior or junior
6. Added GLOBAL_ID to automatically assign ids to new chefs
7. Used rate() method to assign rating to entities easily