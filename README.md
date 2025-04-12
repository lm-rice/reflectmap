# ReflectMap
ReflectMap is a high-performance Java object mapping library leveraging reflection to achieve exceptional speed. 
It is faster than traditional reflection by orders of magnitude and, with a warmed-up JVM, outpaces even direct setter/getter calls.

## Features
- **Speed:** Outperforms manual setter/getter mapping logic by 20% with a warmed-up JVM.
- **Zero GC:** Allocates memory only once per type pair, ensuring zero garbage collection after first invocation.
- **Lightweight:** No compile-time dependencies: just drop it in and go.
- **Modern JVM Integration:** Utilizes advanced JVM features, including dynamic code generation, to maximize runtime performance.
- **Multiple Modes:** Choose between annotation-based mapping or direct ModelMapper-style invocations.

## Installation
Add ReflectMap to your project via Maven or Gradle.

**Maven:**
```xml
<dependency>
  <groupId>com.reflectmap</groupId>
  <artifactId>reflectmap</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'com.reflectmap:reflectmap:1.0.0'
```

Or even download the JAR yourself, if you'd like.
