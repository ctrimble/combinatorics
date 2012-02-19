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

  How many unique six packs can you make from 6 cans of Dr. Pepper, 4 cans of Coke, 4 cans of 7UP, and 2 cans of Barq's?

        import com.github.ctrimble.combinatorics.CombMathUtilsImpl;
        
        ...
        
        int k = 6;
        long number of six packs = new CombMathUtilsImpl().c(k, 6, 4, 4, 2);

### Iterating Combinations with Duplicates

  Iterate the unique six packs that can be made from 6 cans of Dr. Pepper, 4 cans of Coke, 4 cans of 7UP and 2 cans of Barq's.

        import com.github.ctrimble.combinatorics.CombMathUtilsImpl;
        import com.github.ctrimble.combinatorics.Combination;
        
        ...

        public static String DP = "Dr. Pepper";
        public static String COKE = "Coke";
        public static String SEVEN_UP = "7UP";
        public static String BARQS = "Barq's";
        
        ...
        
        String[] domain = {DP, DP, DP, DP, DP, DP, COKE, COKE, COKE, COKE, SEVEN_UP, SEVEN_UP, SEVEN_UP, SEVEN_UP, BARQS, BARQS};
        CombMathUtilsImpl mathUtils = new CombMathUtilImpl();
        
        Combination combination = new Combination(6, domain, mathUtils);
        for( String[] sixPack : combination ) {
          System.out.println(Arrays.toString(sixPack));
        }

