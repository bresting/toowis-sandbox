# tomcat-encrypt-jdbc

1. mvn clean package
2. copy tomcat-encrypt-jdbc.jar to {TOMCAT_HOME}\lib
3. context.xml factory값을 아래와 같이 설정

```
<Resource name="jdbc/someDatasource"
          auth="Container"
          factory="com.toowis.tomcat.jdbc.EncryptedDataSourceFactoryDbcp"
          driverClassName="oracle.jdbc.driver.OracleDriver"
          type="javax.sql.DataSource"
          username="DB-USER-ID"
          password="e3b0673b4c8327bd690514f3d5bc0ce7"
          url="jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.0.100)(PORT = 1543)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = DB_SERVICE_NAME)))"
          validationQuery="SELECT 1 FROM DUAL"
/>

```

password 만들기

```
java -cp toowis-tomcat-encrypt-jdbc.jar com.toowis.tomcat.Encryptor test key
f57fffe039b9dd191bb3f698b1ee0c5c

java -cp toowis-tomcat-encrypt-jdbc.jar com.toowis.tomcat.Encryptor test
86f89d468ccb5f9341dbe87a8e0d7de2
```

datasource.secret.key 적용

```
-Ddatasource.secret.key="somekey"
```