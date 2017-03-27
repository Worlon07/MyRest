# One more Rest implementation on Spring Framework
## Introducing
This project contains 2 modules:
1. **MyRestCore** - Rest implementation core.
2. **MyTestApplication** - Simple rest application based on MyRestCore
 
## How to build it

```
Building MyRest requires Maven.
```

Clone the repository and update.

Build the project with Maven:

```
$ mvn clean package
```

## How to run it

```
Running MyTestApplication requires Java 8
```   
   
Download the latest release from github, build the project and just run the jar.
   
```
java -jar MyTestApplication-0.1.0-SNAPSHOT.jar
```   

## How to test it

Run the `MyTestApplication`.

Open the following links:

```
http://localhost:8080/books
http://localhost:8080/books/1
http://localhost:8080/books/2
http://localhost:8080/books/count
```


