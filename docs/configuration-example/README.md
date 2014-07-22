## Configuration examples

### Option 1
With [pom.xml](option1/pom.xml) only.

### Option 2
With [pom.xml](option2/pom.xml) and [dbpatch.properties](dbpatch.properties) file.

### Option 3
Running without maven.


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
