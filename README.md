# radio-towers

[![Build Status](https://travis-ci.org/nlindblad/radio-towers.svg?branch=master)](https://travis-ci.org/nlindblad/radio-towers)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/89f80845697d47c9b304b75427675970)](https://www.codacy.com/app/niklas/radio-towers?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nlindblad/radio-towers&amp;utm_campaign=Badge_Grade)

## Install development environment

Ensure you have the Java 8 SDK and Maven installed.

### Mac OS X

1. Install Java 8 SDK (instructions [here](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html))

2. Install Maven (instructions [here](http://maven.apache.org/install.html)) or simply use Homebrew:

    ```$ brew install maven```

### Ubuntu

1. Install Java 8 SDK (instructions [here](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04))

2. Install Maven:

    ```$ sudo apt-get install maven```

## Run the project

The easiest way to run the application is to invoke Maven and create a packaged Jar file. This will also run all the unit tests as part of the build:

    $ mvn package
    $ java -classpath target/radio-towers-1.0-SNAPSHOT.jar info.lindblad.radio.App

Example output:

    2/3
    4 5

By default the application attempts to read the island configuration from a file named `input.txt` in the current directory.

It is also possible to visualise the given island configuration using the `--visualise` flag:

    $ java -classpath target/radio-towers-1.0-SNAPSHOT.jar info.lindblad.radio.App --visualise

Example output:

```
*   *   *   *   x   x   x   x   x   x
*   *   *   *   *   *   *   x   R2  x
*   *   *   *   *   *   *   x   x   x
T2  *   *   *   *   *   *   x   x   x
*   *   T1  T4  *   *   R3  x   x   x
*   *   *   *   *   *   *   x   x   x
*   *   *   *   *   *   *   x   x   x
*   T3  *   *   *   *   *   x   x   x
R1  *   *   *   x   x   x   x   x   x
*   *   *   *   x   x   x   x   x   x

2/3
4 5
```

If you want to read from standard input instead of the default `input.txt`, use the `--stdin` flag:

    $ java -classpath target/radio-towers-1.0-SNAPSHOT.jar info.lindblad.radio.App --stdin < other-case.txt

Example output:

```
x   x   x   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   *   T3  *   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   R4  x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
*   *   *   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   *   *   *
*   T4  *   x   x   x   R2  x   x   x   x   *   T1  *   x   x   x   x   R3  x   x   x   *   T5  *
*   *   *   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   *   *   *
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   R1  x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   *   T2  *   x   x   x   x   x   x   x   x   x   x   x
x   x   x   x   x   x   x   x   x   x   x   *   *   *   x   x   x   x   x   x   x   x   x   x   x

0/4
1 6
```

### Run the tests separately

Run the tests using Maven:

    $ mvn test
