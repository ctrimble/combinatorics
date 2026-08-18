# Java Combinatorics

A Java library for generating and iterating over combinations and permutations
of a domain, with a focus on fast iteration.

- **Combinations** and **permutations** are exposed as `Combinatoric`
  collections that extend `java.util.List`, so they can be iterated, indexed,
  and sub-listed (including sub-ranges that exceed `int` range via `long`
  APIs).
- Handles **repeated elements** in a domain correctly (e.g. `{1,1,2,2,3}`).
- Supports **counting** the number of combinations/permutations without
  materializing them.

## Requirements

- Java 17+
- Maven 3.9.12+

## Building from Source

```sh
git clone https://github.com/ctrimble/combinatorics.git
cd combinatorics
mvn clean install
```

## Maven Dependency

Add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.xiantrimble.combinatorics</groupId>
  <artifactId>combinatorics</artifactId>
  <version>0.3.0-SNAPSHOT</version>
</dependency>
```

## Usage

All entry points are static factory methods on the `CombinatoricFactory` class.

### Iterating Combinations

`Combinatoric<T>` extends `List<T[]>`, so each combination is a `T[]` of length
`k` which can be iterated directly (including with `for`-each loops):

```java
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.Combinatoric;
import java.util.Arrays;

int k = 6;
Integer[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

Combinatoric<Integer> combinations = CombinatoricFactory.createCombinations(k, domain);

System.out.println(combinations.longSize());

for (Integer[] combination : combinations) {
  System.out.println(Arrays.toString(combination));
}
```

### Iterating Permutations

Permutations work the same way as combinations. Each permutation is a `T[]` of
length `k`:

```java
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.Combinatoric;
import java.util.Arrays;

int k = 6;
Integer[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

Combinatoric<Integer> permutations = CombinatoricFactory.createPermutations(k, domain);

for (Integer[] permutation : permutations) {
  System.out.println(Arrays.toString(permutation));
}
```

### Indexing and Sub-ranges

Because `Combinatoric` extends `List`, collections can be indexed and sliced by
position. Large collections use the `long`-based APIs (`longSize`, `get(long)`,
`subList(long, long)`):

```java
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.Combinatoric;
import java.util.Arrays;

int k = 6;
Integer[] domain = new Integer[40];
for (int i = 0; i < domain.length; i++) {
  domain[i] = i + 1;
}

Combinatoric<Integer> combinations = CombinatoricFactory.createCombinations(k, domain);

// the 1,000,000th combination
System.out.println(Arrays.toString(combinations.get(1_000_000L)));

// the sub-collection covering positions 100..500
for (Integer[] combination : combinations.subList(100L, 500L)) {
  System.out.println(Arrays.toString(combination));
}
```

### Counting Combinations and Permutations

The `CombMathUtils` class computes the number of combinations (`c`) and
permutations (`p`) of a given length `k` for a domain, without iterating. The
counts are derived from the domain's multiplicity (the rank of each distinct
element), which is obtained from a `Domain` via
`CombinatoricFactory.createDomain` and `Domain.toMultiplicity`:

```java
import com.xiantrimble.combinatorics.CombMathUtils;
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.Domain;

int k = 6;
Integer[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

// c(k, m) — number of combinations
Domain<Integer> domainObj = CombinatoricFactory.createDomain(domain);
int[] multiplicity = domainObj.toMultiplicity();

long combinationCount = CombMathUtils.c(k, multiplicity);

// p(k, m) — number of permutations
long permutationCount = CombMathUtils.p(k, multiplicity);
```
