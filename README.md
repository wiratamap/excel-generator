### Excel Generator Project

## Pre-Requisites
1. Java 21 Installed
2. Maven 3.5.x++ Installed

## Service Details
1. excel-generator-service -> To generate xml file to excel via REST API
2. file-watcher-service -> Watch specific folder and send the xml file to excel-generator-service via rest API
3. service-discovery -> To supervise 2 above services so can communicate with each other

## How to run in Local
1. open `file-watcher-service/src/main/resources/application.properties` and specify `file.incoming-path` you wish which folder to listen
2. open `excel-generator-service/src/main/resources/application.properties` and specify `file.outcoming-path` you wish the excel is generated
3. xml example file already prepared on `file-watcher-service/src/main/resources/templates/*`. Or you can use your xml files
4. Run sequentially:
   1. `mvn spring-boot:run` on `service-discovery`
   2. `mvn spring-boot:run` on `excel-generator-service`
   3. `mvn spring-boot:run` on `file-watcher-service`
