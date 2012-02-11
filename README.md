# Java Combinatorics

  A combinatorics library for Java, with a focus on fast iteration.

## Building from Source

  To build this project, you will need [Maven 3.0](http://maven.apache.org/), Java 1.6 or higher installed, and [my parent pom](https://github.com/ctrimble/parent-pom) installed in your local repository.

> git checkout git://github.com/ctrimble/combinatorics.git combinatorics
> cd combinatorics
> mvn clean install

## Maven Dependency

  To use this project, add the following dependency to your project.

> <dependency>
>   <groupId>com.github.ctrimble.combinatorics</groupId>
>   <artifactId>combinatorics</artifactId>
>   <version>1.0.0-SNAPSHOT</version>
> <dependency>

## Examples

### Counting Combinations with Duplicates

  How many strings of length 5 can you make from the letters in MISSISSIPPI?

> import com.github.ctrimble.combinatorics.CombMathUtilImpl;
>
> ...
>
> int k = 5;
> // S=4, I=4, P=2, M=1
> long count = new CombMathUtilImpl().c(k, 4, 4, 2, 1);

