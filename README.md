# Java Combinatorics

  A combinatorics library for Java, with a focus on fast iteration.
  [![Build Status](https://secure.travis-ci.org/ctrimble/combinatorics.png?branch=master)](https://travis-ci.org/ctrimble/combinatorics)
  [![Analytics](https://ga-beacon.appspot.com/UA-36594186-1/ctrimble/combinatorics)](https://github.com/igrigorik/ga-beacon)

## Status

  This project is now working for forward iteration.  Upcoming improvements:
  - backward iteration for permutations (working for combinations.)
  - inlining of combinatoric engine handlers with ASM.

## Mailing List

  If you are using this project, it would be great to hear from you.  I have set up a user mailing list on [Librelist](http://librelist.com/browser/combinatorics/)
  for any questions or comments.  Simply email combinatorics@librelist.com to sign up.

## Building from Source

  To build this project, you will need [Maven 3.0](http://maven.apache.org/) and Java 1.6 or higher installed.

        git checkout git://github.com/ctrimble/combinatorics.git combinatorics
        cd combinatorics
        mvn clean install

## Maven Dependency

  To use this project, add the following dependency to your project.

        <dependency>
          <groupId>com.xiantrimble.combinatorics</groupId>
          <artifactId>combinatorics</artifactId>
          <version>0.2.0</version>
        <dependency>
        
## Installing the CLI

  I have included a homebrew formula for installing the CLI for this project.  You can install the cli by
  first installing my tap:
  
    brew tap ctrimble/homebrew-tap
    
  and then install the comb command:
  
    brew install --HEAD comb
        
## Some Basic Examples

### Counting Combinations and Permutations

  This package provides a math utility class for computing the number of permutations or combinations
  for a given domain and length (k).  In the following example, we will count the number of
  permutations and combinations of length 6 for the set {1,1,1,1,2,2,2,3,3,4}:
  
        import com.xiantrimble.combinatorics.CombinatoricFactory;
        import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
        import com.xiantrimble.combinatorics.CombMathUtils;
        ...
        int k = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};

        // create a factory and get the utilities.
        CombinatoricFactory factory = new CombinatoricFactoryImpl();
        CombMathUtils utils = factory.getMathUtils();
        
        // the math utils operate on the multiplicity of the domain, not the actual domain.
        int[] domainMultiplicity = factory.createGroupedDomain(k, domain).toMultiplicity();
        
        // the number of combinations of length k
        int combinationCount = utils.c(k, domainMultiplicity);
        
        // the number of permutations of length k
        int permutationCount = utils.p(k, domainMultiplicity);

### Iterating Combinations and Permutations

  Combinations and Permutations in this package are represented by the Combinatoric interface.  This
  interface extends from java.util.List and adds methods to get the length and domain for the collection.
  It also adds access to a long size, since combinations and permutations are often larger than an int
  can represent.  Here is an example of iterating all combinations and permutations of length 6 for the
  set {1,1,1,2,2,2,3,3,4}:

        import com.xiantrimble.combinatorics.CombinatoricFactory;
        import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
        import com.xiantrimble.combinatorics.Combinatoric;
        ...
        int k = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};

        // create a factory and get the utilities.
        CombinatoricFactory factory = new CombinatoricFactoryImpl();
        Combinatoric<Integer> combinations = factory.createCombinations(k,  domain);

        for( Integer[] combination : combinations ) {
          System.out.println(Arrays.toString(combination));
        }
        
        Combinatoric<Integer> permutations = factory.createPermutations(k,  domain);

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
        int k = 6;
        int[] domain = {1,1,1,1,2,2,2,3,3,4};
        
	    CombinatoricFactory factory = new CombinatoricFactoryImpl();
	    CombinatoricEngine<Integer> permutationsEngine = factory.createPermutationsEngine(k,  domain);
	    
	    final List<List<Integer>> result = new ArrayList<List<Integer>>();
	    final Integer[] state = new Integer[k];
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
 


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/ctrimble/combinatorics/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

