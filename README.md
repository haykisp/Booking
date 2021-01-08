## `Requirements`

### `Java`
_Install Java 8 and setup system variables (JAVA_HOME, PATH)_


### `Maven`
_Install Maven and setup system Variables (MAVEN_HOME, M2_HOME, PATH)_

### `Google Chrome and Chromedriver`
_Need Google Chrome 78
If need to use other version need to replace `src/main/java/chromedriver.exe`
with a compatible version of chromedriver_

### `Run and Report`
_To run the tests from cmd need to open the root directory of the project and run the following command_
`mvn clean install`

After successful run reports will be generated automatically in `target/surefire-reports` folder
`target/surefire-reports/emailable-report.html` and `target/surefire-reports/index.html`

