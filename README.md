# HID Door Survey Service

You have just created a simple Spring Boot project in Groovy incorporating the
Actuator. This includes everything you need to run the application. In this
case, that's a simple JSON endpoint.

In this project you get:


You can build and run this sample using Gradle (>=2.3):

```
$ gradle run
```

If you want to run the application outside of Gradle, then first build the JARs
and then use the `java` command:

```
$ gradle build
$ java -jar build/libs/*.jar
```

Then access the app via a browser (or curl) on http://localhost:8080.

TODO: Fetch both/all doors in parallel.

