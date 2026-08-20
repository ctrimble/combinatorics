---
name: java-immutable-list
description: Use when implementing an immutable List<E> wrapper — extending AbstractList<E> over a random-access backing list or AbstractSequentialList<E> over a sequential one — and protecting the backing collection from mutation via a protected constructor and builder.
---

# Structuring Immutable List Wrapper

## Wrapping Random Access Lists

To implement a List<E> that is itself is a wrapper
around a random access List<E>, we just need to extend AbstractList<E>, where E is the element type, and then implement the `get(E)` and `size()` methods.

## Wrapping Sequential Lists

To implement a List<E> that is itself is a wrapper
around a sequential List<E>, we just need to extend AbstractSequentialList<E>, where E is the element type, and then implement the `listIterator()` and `size()` methods.

## Static Certainty

Since you generally want to make certain the backing collection does not change, it is often a good idea to have a protected constructor that takes the backing collection and then a builder that constructs that collection.
