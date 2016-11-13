[![Build Status](https://travis-ci.org/iweinzierl/spring-boot-logging.svg?branch=master)](https://travis-ci.org/iweinzierl/spring-boot-logging)

# spring-boot-logging
Library to ease logging for spring-boot applications.

## Configuration

### Maven Dependency
Add the following to your pom.xml:

    <dependency>
        <group>com.github.iweinzierl</group>
        <artifact>spring-boot-logging</artifact>
        <version>1.0-SNAPSHOT</version>
    </dependency>

### Gradle Dependency
Add the following to your build.gradle:

    compile('com.github.iweinzierl:spring-boot-logging:1.0-SNAPSHOT')

### Application Configuration
Just add the package `com.github.iweinzierl.springbootlogging` to Spring's component scan. E.g. by adding the following annotation to your main class:

    @SpringBootApplication
    @EnableAspectJAutoProxy
    @ComponentScan(basePackages = {"com.github.iweinzierl.springbootlogging", "your.base.package"})
    public class BackendApplication {
    ...
    }
    
### Logging Configuration
spring-boot-logging make use of slf4j logging API. Currently three types of logging are supported:

* logging of method invocations including the name of the invoked method and the passed method parameters
* logging of method results (return value)
* logging of method runtime in milliseconds

You can define on a method level which feature you want to use by configuring `LoggingOption`s at the `MethodLogging` annotation. See below for an example:

    @MethodLogging(options = {
        LoggingOption.METHOD_INVOCATION,
        LoggingOption.METHOD_INVOCATION_RESULT,
        LoggingOption.METHOD_RUNTIME})
    public String sayHello(String firstname, String lastname) {
        return "Hello " + firstname + " " + lastnae;
    }
    
The example results in the following three log statements when invoked with `firstname="Max"` and `lastname="Mustermann"`:

    sayHello(Max, Mustermann)
    sayHello took 4ms
    sayHello => Hello Max Mustermann
