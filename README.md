SR Score Board
==============

This is a library for managing scores between matches.




## Questions

1. Is score update a diff (ex. add +2 points) or absolute (ex. now score is 2:3)? 
2. Can an update be negative ex. due to a previous mistake?
3. Can there be multiple matches with the same configuration (home & away team)?
4. Can a team play with itself (away == home)?
5. Can score be negative?
6. Should we leave finished matches or can we dispose them?
7. Can you update a finished match?

## Assumptions

1. Score is always absolute
2. Score cannot be negative
3. Home or Away teams cannot be playing any other match
4. -,,-
5. Score cannot be negative
6. Finished matches are flagged
7. Team is distinguishable by its name = for simplicity of code
8. Team cannot play with itself
9. Score cannot be negative
10. For test purpose it doesn't need to be multithreaded
