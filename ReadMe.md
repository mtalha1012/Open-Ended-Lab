Design choices: \
1. getInt(), getDouble() and other helper functions defined to facilitate user input
2. Custom NotFoundException defined to be used when an entity is not found in the database instead of custom exceptions for each error
3. Exception handling done in constructor to prevent exceeding recipes of a chef
4. 