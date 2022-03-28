SR Score Board
==============

This is a library for managing scores between matches.




## Questions

1. Is update score a difference (ex. add 2 points) or absolute (ex. now score is 2:3)? 
2. Can an update be negative ex. due to a previous mistake?
3. Can there be multiple matches with the same configuration (home & away team)?
4. Can a team play with itself (away == home)?

## Assumptions

1. Team is distinguishable by its name = for simplicity of code
2. Team cannot play with itself
