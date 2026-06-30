package com.aidar.foundations.di;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class MyContainerTest {

    // Example classes the container will build & wire.
    // Must be public so getConstructors() (public ctors only) sees them.

    public static class Engine {           // no dependencies (recursion base case)
    }

    public static class Car {              // depends on Engine
        final Engine engine;
        public Car(Engine engine) {
            this.engine = engine;
        }
    }

    public static class Garage {           // depends on Car -> Engine (deep chain)
        final Car car;
        public Garage(Car car) {
            this.car = car;
        }
    }

    @Test
    void resolvesClassWithNoDependencies() {
        MyContainer container = new MyContainer();
        Engine engine = container.resolve(Engine.class);
        assertNotNull(engine);
    }

    @Test
    void injectsConstructorDependency() {
        MyContainer container = new MyContainer();
        Car car = container.resolve(Car.class);
        assertNotNull(car);
        assertNotNull(car.engine);          // Engine was created and injected
    }

    @Test
    void resolvesDeepDependencyChain() {
        MyContainer container = new MyContainer();
        Garage garage = container.resolve(Garage.class);   // Garage -> Car -> Engine
        assertNotNull(garage);
        assertNotNull(garage.car);
        assertNotNull(garage.car.engine);
    }

    @Test
    void sameTypeResolvesToSingleton() {
        MyContainer container = new MyContainer();
        Engine a = container.resolve(Engine.class);
        Engine b = container.resolve(Engine.class);
        assertSame(a, b);                   // same instance (cached) == singleton scope
    }

    @Test
    void sharedDependencyIsTheSameInstance() {
        MyContainer container = new MyContainer();
        Car car = container.resolve(Car.class);
        Engine engine = container.resolve(Engine.class);
        assertSame(car.engine, engine);     // Car's engine is the one shared singleton
    }
}
