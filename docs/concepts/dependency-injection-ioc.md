
# Dependency Injection, IoC, and how Spring does it

> What Spring does "magically" under `@Component`/`@Autowired`. Understanding DI/IoC/singleton + reflection = understanding the core of Spring. In a Spring interview this is question #1.

Related: hashmap-internals (the bean cache), Java-Class-Initialization-Thread-Safety (singletons).

---

## Dependency

If class A uses class B — B is a **dependency** of A. The key question: **who creates B?**

- **Without DI:** A itself does `new B()` inside → tight coupling, you can't substitute it (a mock in a test, another implementation), hard to test.
- **With DI:** B is **given** to A from the outside → loose coupling, testability, wiring in one place.

```java
class UserService {
    private final UserRepository repo;
    UserService(UserRepository repo) { this.repo = repo; }  // the dependency comes from outside
}
```

## Three kinds of injection

1. **Constructor (✅):** via constructor parameters. The object is immediately valid; fields are `final`; you can't forget a dependency (it won't compile); all dependencies are visible.
2. **Setter:** via setters — the object may be half-built (null), not final.
3. **Field (`@Autowired` on a field):** convenient, but you can't make it final, it hides dependencies, and it isn't testable without the container. Bad practice.

## IoC (Inversion of Control)

"Control" = who creates objects and in what order. Usually — your `main`. With IoC — the **container**: you declare the classes, it builds the graph itself. The Hollywood principle: "don't call us — we'll call you".

**DI ⊂ IoC:** DI is a special case of IoC (specifically the creation/supply of dependencies is inverted).

## Singleton (a container scope)

The container creates **one** instance of a type and reuses it for everyone (cached by type). `resolve(X)` twice → the **same** object (`a == b`). The class itself is ordinary (not the GoF pattern with `static`) → it stays testable. Spring's default scope.

🔑 A singleton with **mutable** state + multithreading = a race condition (thread-safety-race-conditions). That's why Spring beans are usually **stateless**.

## How the container builds an object — reflection

The container doesn't know your classes in advance → it creates them at runtime via the **Reflection API**:
- `type.getConstructors()[0]` — the constructor;
- `constructor.getParameterTypes()` — the dependency types;
- `constructor.newInstance(deps...)` — create the instance.

The `resolve` algorithm (recursive, with a singleton cache):
```
resolve(type):
  if in cache → return
  ctor = type.getConstructors()[0]
  deps = [ resolve(p) for each p in ctor.getParameterTypes() ]   // recursion
  instance = ctor.newInstance(deps)
  cache[type] = instance; return
```
The recursion's base case is a no-argument constructor. Reflection exceptions (`ReflectiveOperationException`) are wrapped in a `RuntimeException`.

## Pitfalls

- **Circular dependency** (A↔B) → infinite recursion / StackOverflow. Spring detects it (or resolves it via setter/proxy).
- **Multiple constructors** → you need to pick one (an annotation/the only one).
- **Interfaces** → you need an interface→implementation binding (bean registration).
- Reflection is slower than a direct call → the domain of frameworks, not hot loops.

## Interview traps

- "DI vs IoC?" → IoC is the principle, DI is its special case (supplying dependencies).
- "Why is constructor injection better than field?" → final, the contract is visible, testability, no half-built objects.
- "Is a singleton Spring bean thread-safe?" → only if stateless; mutable state → a race.
- "How does Spring create beans?" → reflection: reads the constructor, recursively builds the dependencies, newInstance.
- "Circular dependency?" → Spring detects it; a constructor cycle isn't resolvable without a proxy.

## Connected

- thread-safety-race-conditions
- hashmap-internals
- generic-arrays

## Where it appeared

`java-foundations` kata #11: `MyContainer` — a constructor-injection IoC container built on reflection, recursive resolution, a singleton cache in a custom `MyHashMap`. The final kata (a bridge to Spring).
