package io.realm.examples.performance.sugar_orm;

import android.util.Log;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.realm.examples.performance.PerformanceTest;
import io.realm.examples.performance.PerformanceTestException;

public class SugarORMTests extends PerformanceTest {

    public static final String TAG = SugarORMTests.class.getName();

    public SugarORMTests() {
        testName = "SugarORM";
    }

    public void clearDatabase() throws PerformanceTestException {
        SugarEmployee.deleteAll(SugarEmployee.class);
    }

    public void testBootstrap() throws PerformanceTestException {
        SugarEmployee employee = new SugarEmployee("Foo0",25,0);
        employee.save();
        employee.delete();
        Select outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo0"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(0));
        outcome.list().size();
    }

    public void testBatchInserts() throws PerformanceTestException {
        //SugarORM Batch support has been deprecated and did not work when we tried it.
        testInsertPerTransaction();
    }

    public void testInsertPerTransaction() throws PerformanceTestException {
        for (int row = 0; row < getNumInserts(); row++) {
            SugarEmployee employee
                    = new SugarEmployee(getEmployeeName(row),
                    getEmployeeAge(row),
                    getEmployeeHiredStatus(row));
            employee.save();
        }
    }

    public void verifyInserts() throws PerformanceTestException {
        List<SugarEmployee> list = SugarEmployee.listAll(SugarEmployee.class);
        if(list.size() < getNumInserts()) {
            throw new PerformanceTestException("Sugar ORM failed to insert all of the records");
        }
    }

    public void testQueries() throws PerformanceTestException {
        Select outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo1"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(1));
        loopResults(outcome);

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo3"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(1));
        loopResults(outcome);

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo2"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(0));
        loopResults(outcome);

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo330"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(0));
        loopResults(outcome);
    }

    private void loopResults(Select results) throws PerformanceTestException{
        int iterations = 0;
        for (Object e : results.list()) {
            SugarEmployee emp = (SugarEmployee) e;
            emp.getId();
            iterations++;
        }
        if(iterations < getNumInserts()) {
            throw new PerformanceTestException("GreenDAO does not complete the iterations over the queried results");
        }
    }

    public void testCounts() throws PerformanceTestException {
        Select outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo1"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(1));
        outcome.list().size();

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo3"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(1));
        outcome.list().size();

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo2"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(0));
        outcome.list().size();

        outcome = Select.from(SugarEmployee.class)
                .where(Condition.prop("name").eq("Foo330"),
                        Condition.prop("age").gt(20).lt(50),
                        Condition.prop("hired").eq(0));
        outcome.list().size();
    }
}
