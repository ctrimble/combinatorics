# Java Combinatorics

  A combinatorics library for Java, with a focus on fast iteration.

## Building from Source

  To build this project, you will need [Maven 3.0](http://maven.apache.org/), Java 1.6 or higher installed, and [my parent pom](https://github.com/ctrimble/parent-pom) installed in your local repository.

        git checkout git://github.com/ctrimble/combinatorics.git combinatorics
        cd combinatorics
        mvn clean install

## Maven Dependency

  To use this project, add the following dependency to your project.

        <dependency>
          <groupId>com.github.ctrimble.combinatorics</groupId>
          <artifactId>combinatorics</artifactId>
          <version>1.0.0-SNAPSHOT</version>
        <dependency>

## Examples

### Counting Combinations with Duplicates

  How many unique six packs can you make from 6 cans of Dr. Pepper, 4 cans of Coke, 4 cans of 7UP, and 2 cans of Barqs?

        import com.github.ctrimble.combinatorics.CombMathUtilImpl;
        
        ...
        
        int k = 5;
        long number of six packs = new CombMathUtilImpl().c(k, 6, 4, 4, 2);

