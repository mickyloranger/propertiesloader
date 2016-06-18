# propertiesloader
This project demonstrate how to load properties file in Java.

# How it work
The application load the application.properties file and parse the content to Map<String, Map<String, String>>. This mean that application.properties file must have a well defined format.

# The format of application.properties file
```
#this is the first comment line
profileA:
	protocol=http
	secure=true
	
#this line is a comment line
profileB:
	protocol=ftp
	secure=fasle
```

# The output format
The result of the parsing of the properties file is a ```Map<String, Map<String, String>>```. The example of the previous section produces the following output: ```{profileA={protocol=http, secure=true}, profileB={protocol=ftp, secure=fasle}}``` 
