package jolt.example.samples.app.tests;

import com.badlogic.gdx.utils.Array;
import jolt.example.samples.app.tests.shapes.BoxShapeTest;
import jolt.example.samples.app.tests.vehicle.TankTest;
import jolt.example.samples.app.tests.vehicle.VehicleConstraintTest;

public class Tests {
    private Array<Tests.JoltTestInstancer> tests;

    public Tests() {
        tests = Tests.setup();
    }

    private static Array<JoltTestInstancer> setup() {
        Array<JoltTestInstancer> tests = new Array<>();
        tests.add(new JoltTestInstancer(BoxShapeTest.class, BoxShapeTest::new, "Shapes", "Box Shape"));
        tests.add(new JoltTestInstancer(VehicleConstraintTest.class, VehicleConstraintTest::new, "Vehicle", "Car (VehicleConstraint)"));
        tests.add(new JoltTestInstancer(TankTest.class, TankTest::new, "Vehicle", "Tank (VehicleConstraint)"));
        return tests;
    }

    public Test getTest(Class type) {
        for(JoltTestInstancer test : tests) {
            if(test.type == type) {
                return test.newInstance();
            }
        }
        return null;
    }

    public TestGroup getAllTests() {
        TestGroup root = new TestGroup();
        root.name = "Tests";
        Array<TestGroup> allTests = root.children;
        for(JoltTestInstancer testInstancer : tests) {
            TestGroup parent = null;
            int length = testInstancer.groups.length;
            for(int i = 0; i < length; i++) {
                boolean isLast = length-1 == i;
                String group = testInstancer.groups[i];
                if(parent == null) {
                    parent = findGroup(group, allTests);
                    if(parent == null) {
                        parent = new TestGroup();
                        parent.name = group;
                        allTests.add(parent);
                    }
                }
                else {
                    TestGroup child = findGroup(group, parent.children);
                    if(child == null) {
                        child = new TestGroup();
                        child.name = group;
                        parent.addChild(child);
                    }
                    parent = child;
                }

                if(isLast) {
                    parent.testClass = testInstancer.type;
                }
            }
        }
        return root;
    }

    static TestGroup findGroup(String name, Array<TestGroup> allTests) {
        for(TestGroup testGroup : allTests) {
            String groupName = testGroup.getName();

            if(groupName.equals(name)) {
                return testGroup;
            }
        }
        return null;
    }

    public static class JoltTestInstancer {
        private Class<Test> type;
        private NewTest<?> instancer;
        String [] groups;

         public <T extends Test> JoltTestInstancer(Class<T> type, NewTest<T> instancer, String ... groups) {
            this.type = (Class<Test>)type;
            this.instancer = instancer;
            this.groups = groups;
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