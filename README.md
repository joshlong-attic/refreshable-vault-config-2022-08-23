# Usage 

this program expects a property file in `${HOME}/Desktop/test.properties`.

then trigger the refresh endpoint with: 

```shell
curl -d{} -H"content-type: application/json" http://localhost:8080/actuator/refresh
```

## Formatting 

This demonstration uses the Spring Javaformat Maven plugin. It can format your Java code in a consistent way, allowing you 
to focus on the stuff that matters, not things like tabs vs spaces, etc. 

Use it thusly to format the code: 

```shell 
mvn spring-javaformat:apply
```

It'll stop the build if it detects that the source code has not been formatted correctly.