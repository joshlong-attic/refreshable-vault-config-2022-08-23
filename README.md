# Usage 

this program expects a property file in `${HOME}/Desktop/test.properties` containing values like this: 

```properties 
spring.datasource.url=jdbc:postgresql://localhost:5430/a
spring.datasource.password=a
spring.datasource.username=a
# spring.datasource.url=jdbc:postgresql://localhost:5431/b
# spring.datasource.password=b
# spring.datasource.username=b
```

You'll need two databases to see the refresh work. Use `docker compose up` in the root directory to spin up two PostgreSQL instances.

Ensure that you have the same table, `customer`, in both. Insert different records to verify that things are working. 

```sql 
create table customer(
    id serial primary key, 
    name varchar(255) not null
);
```

You can connect to the `a` PostgreSQL instance using `psql` thusly:

```shell 
PGPASSWORD=a psql -U a -h localhost -p 5430 a 
```

You can connect to the `b` PostgreSQL instance using `psql` thusly:

```shell 
PGPASSWORD=b psql -U b -h localhost -p 5431 b 
```

In the `a` database, run: 

```sql 
insert into customer(name) values('afitz');
```

In the `b` database, run:

```sql 
insert into customer(name) values('rwang');
```

Load the application and then hit the `/customers` endpoint to see values in the first database. Uncomment the configuration for the alternate database, `b`, and then comment out the configuration for the first database, `a`. 

Trigger the refresh endpoint: 

```shell
curl -d{} -H"content-type: application/json" http://localhost:8080/actuator/refresh
```

The app should now be pointing to the alternate `DataSource`. Verify as much by refreshing the `http://localhost:8080/customers` endpoint

## Formatting 

This demonstration uses the Spring Javaformat Maven plugin. It can format your Java code in a consistent way, allowing you 
to focus on the stuff that matters, not things like tabs vs spaces, etc. 

Use it thusly to format the code: 

```shell 
mvn spring-javaformat:apply
```

It'll stop the build if it detects that the source code has not been formatted correctly.