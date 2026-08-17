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

All entry points are created through the `CombinatoricFactory`, obtained via the
`CombinatoricFactoryImpl` implementation.

### Iterating Combinations

`Combinatoric<T>` extends `List<T[]>`, so each combination is a `T[]` of length
`k` which can be iterated directly (including with `for`-each loops):

```java
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
import com.xiantrimble.combinatorics.Combinatoric;
import java.util.Arrays;

int k = 6;
int[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

CombinatoricFactory factory = new CombinatoricFactoryImpl();
Combinatoric<Integer> combinations = factory.createCombinations(k, domain);

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
import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
import com.xiantrimble.combinatorics.Combinatoric;
import java.util.Arrays;

int k = 6;
int[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

CombinatoricFactory factory = new CombinatoricFactoryImpl();
Combinatoric<Integer> permutations = factory.createPermutations(k, domain);

for (Integer[] permutation : permutations) {
  System.out.println(Arrays.toString(permutation));
}
```

### Indexing and Sub-ranges

Because `Combinatoric` extends `List`, collections can be indexed and sliced by
position. Large collections use the `long`-based APIs (`longSize`, `get(long)`,
`subList(long, long)`):

```java
Combinatoric<Integer> combinations = factory.createCombinations(k, domain);

// the 1,000,000th combination
System.out.println(Arrays.toString(combinations.get(1_000_000L)));

// the sub-collection covering positions 100..500
for (Integer[] combination : combinations.subList(100L, 500L)) {
  System.out.println(Arrays.toString(combination));
}
```

### Counting Combinations and Permutations

The `CombMathUtils` class computes the number of combinations (`c`) and
permutations (`p`) of a given length `k` for a domain, without iterating:

```java
import com.xiantrimble.combinatorics.CombMathUtils;

long k = 6;
int[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

// c(k, n) — number of combinations
System.out.println(CombMathUtils.c(6, 10));

// p(k, n) — number of permutations
System.out.println(CombMathUtils.p(6, 10));
```

The `c`/`p` overloads that accept a multiplicity array are the most useful when
a domain contains repeated elements. Obtain the multiplicity via
`CombinatoricFactory.createDomain`:

```java
import com.xiantrimble.combinatorics.CombMathUtils;
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;
import com.xiantrimble.combinatorics.Domain;

int k = 6;
int[] domain = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};

CombinatoricFactory factory = new CombinatoricFactoryImpl();
Domain<Integer> domainObj = factory.createDomain(k, domain);

int[] multiplicity = domainObj.toMultiplicity();

long combinationCount = CombMathUtils.c(k, multiplicity);
long permutationCount = CombMathUtils.p(k, multiplicity);
```
