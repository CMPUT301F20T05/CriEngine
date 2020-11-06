# CMPUT 301 FALL 2020 TEAM 05 PROJECT

Scrum Board: https://github.com/CMPUT301F20T05/CriEngine/projects/1

Wiki Link: https://github.com/CMPUT301F20T05/CriEngine/wiki

----

## Workflow
### Branch Creation
- Create new branch off of latest changes from master
- Name the branch `feature/<brief description of new feature delimited by dashes>`

### Code
- Write neat code, reduce copy pasted code but leave refactoring to last
- Once functionality is captured and code is satisfactory:
  - Write Junit 5 tests
  - Write JavaDoc for your code

### PR Creation
- If adding a WIP, add the `WIP` and `DO NOT MERGE` labels
- Request reviews from any two members of the team

## User Test Account Login Credentials
- To log into an existing account, please use one of the following two accounts:
  - Account 1:
    - Username: user1@email.com
    - Password: password
  - Account 2:
    - Username: user2@email.com
    - Password: password
    
## Potential Test Issues
- If your network connection is slow or disconnected, the intent tests will fail. This is because they need to connect to the database within a certain timeframe in order to pass.
- To avoid tests from failing due to network issues, ensure you have the emulator started prior to starting the test.
  - In addition, try logging in manually to a test account to test the response time for the database.
