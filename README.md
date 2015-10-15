# Deveo Jenkins Notification Plugin

Use this plugin to create build events to Deveo from your Jenkins builds. See the [official documentation](https://wiki.jenkins-ci.org/display/JENKINS/Deveo+Plugin) for more information.

## Prerequisites

Download and install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).

Install Maven:

    $ brew install maven

Define the path to your Java 7 compiler in your `~/.m2/settings.xml`:

```xml
<settings>
  <profiles>
    <profile>
      <id>compiler</id>
        <properties>
          <JAVA_1_7_HOME>/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home</JAVA_1_7_HOME>
        </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>compiler</activeProfile>
  </activeProfiles>
</settings>
```

## Useful commands

To build the thing

    $ mvn install

To generate .hpi package

    $ mvn hpi:hpi

To run the plugin in a local Jenkins instance

    $ mvn hpi:run

To run tests

    $ mvn -D test=DeveoTestSuite

To deploy it

    $ mvn -B release:prepare release:perform
