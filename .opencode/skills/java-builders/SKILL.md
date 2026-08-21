---
name: java-builders
description: Use when generating or consuming fluent Builder APIs for Java POJOs via the PojoBuilder (@GeneratePojoBuilder) annotation processor — configuring Maven setup, generation directives, meta-annotations, or the with(Consumer<B>) context-carving pattern.
---

# Creating Fluent Builder APIs.

## Using Annotation Processors

The [PojoBuilder](https://github.com/mkarneim/pojobuilder) project is a great annotation processor for generating builders on the fly. It is a compile-time code generator that produces a fluent builder class for a POJO (Plain Old Java Object), so the generated class offers a fluent `with*` interface plus a `build()` method that constructs the POJO. No runtime dependency is added (PojoBuilder's `@GeneratePojoBuilder` annotation has `CLASS` retention), so it is declared `provided` in this project's Maven setup.

### Maven Dependency

Add the `provided`-scoped dependency to the module that needs generated builders:

```xml
<dependency>
   <groupId>net.karneim</groupId>
   <artifactId>pojobuilder</artifactId>
   <version>4.3.1</version>
   <scope>provided</scope>
</dependency>
```

In modern versions of the maven-compiler-plugin (3.x or later), you need to put the definition in the compiler's `<annotationProcessorPaths/>` block:

```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version> <!-- Use a recent version -->
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>net.karneim</groupId>
                        <artifactId>pojobuilder</artifactId>
                        <version>4.3.1</version>
                    </path>
                 </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

The compile phase auto-detects and activates PojoBuilder, and generated sources appear under `${project.build.directory}/generated-sources/annotations`.

Other plugins need to be aware of this location for source files.  You will need to configure `${project.build.directory}/generated-sources/annotations` in the project sources using the `build-helper-maven-plugin`:

```
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <version>3.5.0</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>add-source</goal>
      </goals>
      <configuration>
        <sources>
          <source>${project.build.directory}/generated-sources/annotations</source>
        </sources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Triggering Generation

You can trigger code generation by annotating one of the following:

- **A constructor** — annotate one of the POJO's constructors with `@GeneratePojoBuilder`. The generated builder is named `<Type>Builder` and is built via that constructor. The constructor must be public or accessible to the generated builder, and its parameter names must match the POJO's property names exactly (or be mapped via `@java.beans.ConstructorProperties`).

   ```java
   public class Contact {
       private final String surname;
       private final String firstname;
       private String email;

       @GeneratePojoBuilder
       public Contact(String surname, String firstname) {
           this.surname = surname;
           this.firstname = firstname;
       }
       // getters/setters
   }
   ```

- **The POJO class** — if the POJO has no specific constructor, only a public default constructor, or exactly one constructor, annotate the class itself. The builder is built from the POJO's getter/setter properties.

   ```java
   @GeneratePojoBuilder
   public class User {
       private String name;
       private char[] password;
       // getters/setters
   }
   ```

- **A factory method** — if you cannot or prefer not to modify the POJO, annotate a `public static` factory method. Its parameter names must match the POJO's property names (or be mapped via `@FactoryProperties`). Use the `withName` and `intoPackage` directives to name and locate the generated builder.

   ```java
   public class UrlFactory {
       @GeneratePojoBuilder(withName = "UrlBuilder", intoPackage = "samples")
       public static URL createUrl(
           String protocol, String host, int port, String file, URLStreamHandler handler)
           throws MalformedURLException {
           return new URL(protocol, host, port, file, handler);
       }
   }
   ```

- **A record** (PojoBuilder 4.3+, Java 17) — annotate a record directly.

   ```java
   @GeneratePojoBuilder
   public record MyRecord(int x, int y, String blah) {}
   ```

### Directives

The `@GeneratePojoBuilder` annotation attributes configure code generation. Useful ones:

- **`withName=<String>`** — pattern for the builder's name; `*` is replaced with the POJO's simple name. Default `*Builder`, so `Fluent*Builder` yields `FluentContactBuilder` for `Contact`.
- **`withConstructor=<Visibility>`** — visibility of the builder's constructor. Default `Visibility.PUBLIC`.
- **`intoPackage=<String>`** — package of the generated builder; `*` is replaced with the POJO's package. Default `*`.
- **`withBaseclass=<Class>`** — base class of the generated builder. Default `Object.class`.
- **`includeProperties=<String[]>` / `excludeProperties=<String[]>`** — which POJO properties are included/excluded, matching property patterns. Mandatory properties (constructor/factory args) are never excluded. Defaults `*` (include) and empty (exclude).
- **`withGenerationGap=<boolean>`** — generate two classes: the generated one plus a hand-writable template that extends it (move the template out of the generated-sources folder to avoid overwrite). Default `false`.
- **`withCopyMethod=<boolean>`** — generate a copy method that initializes the builder from an existing POJO instance. Default `false`.
- **`withOptionalProperties=<Class>`** — also generate optional-based setters using the given `Optional` type (e.g. `java.util.Optional`). Default `Void.class` (none).
- **`withSetterNamePattern=<String>`** — pattern for setter method names; `*` is replaced with the property name. Default `with*`.
- **`withValidator=<Class>`** — validator class with a `validate` method that throws on failure, applied to the built POJO.
- **`withFactoryMethod=<String>`** — add a static factory method on the builder that creates a builder instance; `*` is replaced with the POJO's simple name. Default `""` (none).

### Meta-Annotations

Since version 3, PojoBuilder supports meta-annotations: place `@GeneratePojoBuilder` on another annotation and it is inherited. This lets you share common directives in one place and combine them with other libraries that support meta-annotations. A local `@GeneratePojoBuilder` on the POJO overrides the meta-annotation's defaults.

```java
@GeneratePojoBuilder(withName = "Fluent*Builder")
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface AppPojo {
}

@AppPojo
public class Contact {
    public String name;
}
```

### Usage

A generated builder is used like this:

```java
Contact james = new ContactBuilder()
      .withSurname("Bond")
      .withFirstname("James")
      .withEmail("007@secretservice.org")
      .build();
```

Builders are especially useful for constructing test data by setting only the relevant properties.

### Advanced: Carving Out a Context with `with(Consumer<Builder>)`

A flat builder is sometimes not enough when an object needs nested or conditional construction that spans several `with*` calls and then continues the outer chain. You can enable this by giving the generated builder an abstract base class that defines a `with` method. The method takes a `Consumer<Builder>` — where `Builder` is the builder's own type, using the curiously recurring generic pattern — runs it against `this`, and returns the builder so the fluent API can "carve out some context" for more complex construction:

```java
import java.util.function.Consumer;

public abstract class AbstractBuilder<B> {
      @SuppressWarnings("unchecked")
     public B with(Consumer<B> configure) {
         configure.accept((B) this);
         return this;
     }
}
```

Attach the base class to a generated builder with the `withBaseclass` directive:

```java
@GeneratePojoBuilder(withBaseclass = AbstractBuilder.class)
public class Contact {
    private final String surname;
    private final String firstname;
    private String email;
     // constructor and accessors
}
```

The fluent API can now group complex or conditional work inside a consumer while staying in the same context:

```java
Contact james = new ContactBuilder()
      .withSurname("Bond")
      .with(spy -> {
          spy.withFirstname("James");
          spy.withEmail("007@secretservice.org");
      })
      .build();
```

**Note:** `withBaseclass` is a `Class` literal, so PojoBuilder emits the supertype in raw form (`extends AbstractBuilder`) and the self-type is erased. To keep the `Consumer`'s `Builder` type precise, hand-write a self-referential abstract base (`abstract class AbstractContactBuilder extends AbstractBuilder<AbstractContactBuilder>` that also declares the `with*` setters) and generate the concrete builder as its subclass via `withGenerationGap`.
