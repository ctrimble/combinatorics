# Java Combinatorics

  A combinatorics library for Java, with a focus on fast iteration.
  [![Build Status](https://secure.travis-ci.org/ctrimble/combinatorics.png?branch=master)](https://travis-ci.org/ctrimble/combinatorics)
  [![githalytics.com alpha](https://cruel-carlota.pagodabox.com/7c662a63fdd203d3ed95c0e01662fbf6 "githalytics.com")](http://githalytics.com/ctrimble/combinatorics)

## Building from Source

  To build this project, you will need [Maven 3.0](http://maven.apache.org/), Java 1.6 or higher installed, and [my parent pom](https://github.com/ctrimble/parent-pom) installed in your local repository.

        git checkout git://github.com/ctrimble/combinatorics.git combinatorics
        cd combinatorics
        mvn clean install

## Maven Dependency

  To use this project, add the following dependency to your project.

        <dependency>
          <groupId>com.xiantrimble.combinatorics</groupId>
          <artifactId>combinatorics</artifactId>
          <version>0.1.0-SNAPSHOT</version>
        <dependency>
        
## Status

  This project is now working, but will be repackaged soon.  The repackaging will
  only affect imports and the group id.

## Some Basic Examples

### Counting Combinations and Permutations

  This package provides a math utility class for computing the number of permutations or combinations
  for a given domain and rank (length).  In the following example, we will count the number of
  permutations and combinations of length 6 for the set {1,1,1,1,2,2,2,3,3,4}:
  
        import com.xiantrimble.combinatorics.CombinatoricFactory;
        import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
        import com.xiantrimble.combinatorics.CombMathUtils;
        ...
        int rank = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};

        // create a factory and get the utilities.
        CombinatoricFactory factory = new CombinatoricFactoryImpl();
        CombMathUtils utils = factory.getMathUtils();
        
        // the math utils operate on ranks of the domain, not the actual domain.
        int[] domainRanks = factory.createMultiset(rank, domain).getRankArray();
        
        // the number of combinations of length 6
        int combinationCount = utils.c(rank, domainRanks);
        
        // the number of permutations of length 6
        int permutationCount = utils.p(rank, domainRanks);

### Iterating Combinations and Permutations

  Combinations and Permutations in this package are represented by the Combinatoric interface.  This
  interface extends from java.util.List and adds methods to get the rank and domain for the collection.
  It also adds access to a long size, since combinations and permutations are often larger than an int
  can represent.  Here is an example of iterating all combinations and permutations of length 6 for the
  set {1,1,1,2,2,2,3,3,4}:

        import com.xiantrimble.combinatorics.CombinatoricFactory;
        import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
        import com.xiantrimble.combinatorics.Combinatoric;
        ...
        int rank = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};

        // create a factory and get the utilities.
        CombinatoricFactory factory = new CombinatoricFactoryImpl();
        Combinatoric<Integer> combinations = factory.createCombinations(6,  domain);

        for( Integer[] combination : combinations ) {
          System.out.println(Arrays.toString(combination));
        }
        
        Combinatoric<Integer> permutations = factory.createPermutations(6,  domain);

        for( Integer[] permutation : permutations ) {
          System.out.println(Arrays.toString(permutation));
        }
        
### Inverting Control with Combination and Permutation Engines

  This package provides combination and permutation engines, which invert the iteration process.  Instead
  of requesting the next combination/permutation from an iterator, an engine is created to drive the iteration
  process.  As it iterates, the engine calls back to a handler with change events (swap, replace, init) and then
  calls evaluate() when the next valid state has been reached.
        
        import java.util.ArrayList;
        import java.util.List;
        import com.xiantrimble.combinatorics.CombinatoricFactory;
        import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
        import com.xiantrimble.combinatorics.CombinatoricEngine;
        ...
        int rank = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};
        
	    CombinatoricFactory factory = new CombinatoricFactoryImpl();
	    CombinatoricEngine<Integer> permutationsEngine = factory.createPermutationsEngine(rank,  domain);
	    
	    final List<List<Integer>> result = new ArrayList<List<Integer>>();
	    final Integer[] state = new Integer[rank];
	    permutationsEngine.setHandler(new AbstractCombinatoricHandler<Integer>() {
	      @Override
	      public void evaluate() {
	        result.add(Arrays.asList(Arrays.copyOf(state, state.length))); 
	      }
	
	      @Override
	      public void init(Integer[] newState ) {
	        for( int i = 0; i < newState.length; i++ ) {
	          state[i] = newState[i];
	        }
	      }
	
	      @Override
	      public void swap(Integer newA, int ai, Integer newB, int bi) {
	        state[ai] = newA;
	        state[bi] = newB;
	      }
	
	      @Override
	      public void replace(Integer newValue, Integer oldValue, int i) {
	        state[i] = newValue;
	      }
	    });
	    permutationsEngine.execute();
	
	    return result;
 
