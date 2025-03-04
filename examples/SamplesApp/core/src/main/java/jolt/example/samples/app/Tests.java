package jolt.example.samples.app;

import com.badlogic.gdx.utils.Array;
import jolt.example.samples.app.tests.BoxShapeTest;
import jolt.example.samples.app.tests.vehicle.VehicleConstraintTest;
import jolt.example.samples.app.tests.vehicle.VehicleTest;

public class Tests {
    private Array<Tests.JoltTestInstancer> tests;

    public Tests() {
        tests = Tests.getTests();
    }

    public Test getTest(Class type) {
        for(JoltTestInstancer test : tests) {
            if(test.type == type) {
                return test.newInstance();
            }
        }
        return null;
    }

    public Array<Class<?>> getAllTests() {
        Array<Class<?>> allTests = new Array<>();
        for(JoltTestInstancer test : tests) {
            allTests.add(test.type);
        }
        return allTests;
    }

    public static Array<JoltTestInstancer> getTests() {
        Array<JoltTestInstancer> tests = new Array<>();
        tests.add(new JoltTestInstancer(BoxShapeTest.class, BoxShapeTest::new));
        tests.add(new JoltTestInstancer(VehicleConstraintTest.class, VehicleConstraintTest::new));
        return tests;
    }

    public static class JoltTestInstancer {
        private Class<?> type;
        private NewTest<?> instancer;
         public <T extends Test> JoltTestInstancer(Class<T> type, NewTest<T> instancer) {
            this.type = type;
            this.instancer = instancer;
        }

        public Test newInstance() {
            return (Test)instancer.newTest();
        }

        public String getName() {
            return type.getSimpleName();
        }
    }

    @FunctionalInterface
    private interface NewTest<T> {
        T newTest();
    }
}