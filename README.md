# Usage 

this program expects a property file in `${HOME}/Desktop/test.properties`.

then trigger the refresh endpoint with: 

```shell
curl -d{} -H"content-type: application/json" http://localhost:8080/actuator/refresh
```