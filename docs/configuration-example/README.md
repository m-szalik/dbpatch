## Configuration examples

### Option 1
With [option1/pom.xml](option1/pom.xml) only.
```xml
 <plugin>
     <groupId>org.jsoftware</groupId>
     <artifactId>dbpatch</artifactId>
     <configuration>
         <conf>
             <driverClass>com.mysql.jdbc.Driver</driverClass>
             <jdbcUri>jdbc:mysql://localhost/dbpatch_test</jdbcUri>
             <username>dbpatch</username>     <!-- your database user -->
             <password>${password}</password> <!-- set by pom property "password" -->
             <patchDirs>../example-sqls</patchDirs> <!-- sql scripts location -->
         </conf>
     </configuration>
     <dependencies>
         <!-- required jdbc driver here -->
     </dependencies>
 </plugin>
```

### Option 2
With [option2/pom.xml](option2/pom.xml) and [dbpatch.properties](dbpatch.properties) file.
```xml
 <plugin>
     <groupId>org.jsoftware</groupId>
     <artifactId>dbpatch</artifactId>
     <configuration>
         <configFile>../dbpatch.properties</configFile><!-- where to find config file -->
         <selectedConfiguration>mydb</selectedConfiguration> <!-- selected profile - can be set by -Dmaven.dbpatch.configuration=XXX -->
     </configuration>
     <dependencies>
          <!-- required jdbc driver here -->
      </dependencies>
 </plugin>
```

```properties
mydb.driverClass=com.mysql.jdbc.Driver
mydb.jdbcUri=jdbc:mysql://localhost/dbpatch_test
mydb.username=dbpatch
mydb.password=secret
mydb.patchDirs=../example-sqls
```

### Option 3
Run as standalone java application with GUI.  
Configuration [dbpatch.properties](dbpatch.properties) file is required.  
``java -jar option3/dbpatch.jar``


### Examples requirements
To run this example without any changes you need:
 1. MySQL database on localhost port 3306
 1. Data base **dbpatch_test** created
 1. User **dbpatch** with password **secret** with full access to database **dbpatch_test**

To setup your database execute this script as superuser (root):
```SQL
CREATE SCHEMA `dbpatch_test`;
CREATE USER 'dbpatch'@'localhost' IDENTIFIED BY 'secret';
GRANT ALL PRIVILEGES ON dbpatch_test.* TO 'dbpatch'@'localhost';
FLUSH PRIVILEGES;
```
Test your connection with `mysql -u dbpatch -p dbpatch_test`
