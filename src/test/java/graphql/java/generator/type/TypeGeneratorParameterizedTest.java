package graphql.java.generator.type;

import graphql.java.generator.*;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TypeGeneratorParameterizedTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorParameterizedTest.class);
    
    ITypeGenerator generator = DefaultBuildContext.reflectionContext;

    private Class<?> clazz;
    private String expectedName;
    private int expectedNumFields;
    private boolean expectedToSucceed;
    
    @Before
    public void before() {
        DefaultBuildContext.defaultTypeRepository.clear();
    }
    
    public TypeGeneratorParameterizedTest(Class<?> clazz, String expectedName,
            int expectedNumFields, boolean expectedToSucceed) {
        this.clazz = clazz;
        this.expectedName = expectedName;
        this.expectedNumFields = expectedNumFields;
        this.expectedToSucceed = expectedToSucceed;
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        @SuppressWarnings("serial")
        ArrayList<Object[]> list = new ArrayList<Object[]>() {{
            add(new Object[] {InterfaceImpl.class, "InterfaceImpl", 2, true});
            add(new Object[] {InterfaceParent.class, "InterfaceParent", 1, true});
            add(new Object[] {InterfaceChild.class, "InterfaceChild", 2, true});
            add(new Object[] {ClassWithLists.class, "ClassWithLists", 3, true});
            add(new Object[] {RecursiveClass.class, "RecursiveClass", 2, false});
            add(new Object[] {RootFieldClass.class, "RootFieldClass", 3, false});
            add(new Object[] {ShadowChild.class, "ShadowChild", 1, true});
            add(new Object[] {ShadowParent.class, "ShadowParent", 1, true});
            add(new Object[] {SimpleObject.class, "SimpleObject", 4, true});
        }};
        return list;
    }
    
    @Test
    public void testBenchmark() {
        logger.debug("testBenchmark {} {}", clazz, expectedName);
        long startTime = System.currentTimeMillis();
        
        final int numRuns = 0;
        for (int x = 0; x < numRuns; ++x) {
            DefaultBuildContext.defaultTypeRepository.clear();
            basicsOutput();
        }
        
        long endTime = System.currentTimeMillis();
        logger.error("testBenchmark {} {} took {}", clazz, expectedName, endTime - startTime);
    }
    
    @Test
    public void testBasicsOutput() {
        logger.debug("testBasicsOutput {} {}", clazz, expectedName);
        basicsOutput();
    }
    
    public void basicsOutput() {
        GraphQLType type = generator.getOutputType(clazz);
        Assert.assertThat(type,
                instanceOf(GraphQLObjectType.class));
        GraphQLObjectType objectType = (GraphQLObjectType) type;
        Assert.assertThat(objectType.getName(),
                containsString(expectedName));
        Assert.assertThat(objectType.getDescription(),
                containsString("Autogenerated f"));
        Assert.assertThat(objectType.getFieldDefinitions(),
                notNullValue());
        for (GraphQLFieldDefinition field : objectType.getFieldDefinitions()) {
            Assert.assertThat(field,
                    notNullValue());
            Assert.assertThat(field.getDescription(),
                    containsString("Autogenerated f"));
        }
        Assert.assertThat(objectType.getFieldDefinitions().size(),
                is(expectedNumFields));
    }
    
    @Test
    public void testBasicsInput() {
        logger.debug("testBasicsInput {} {}", clazz, expectedName);
        basicsInput();
    }
    
    public void basicsInput() {
        if (expectedToSucceed == false) {
            try {
                generator.getInputType(clazz);
                fail("Should have caught exception");
            }
            catch (Exception e) {
                Assert.assertThat(e, instanceOf(RuntimeException.class));
            }
            return;
        }
        
        GraphQLType type = generator.getInputType(clazz);
        Assert.assertThat(type,
                instanceOf(GraphQLInputObjectType.class));
        GraphQLInputObjectType objectType = (GraphQLInputObjectType) type;
        Assert.assertThat(objectType.getName(),
                containsString(expectedName));
        Assert.assertThat(objectType.getDescription(),
                containsString("Autogenerated f"));
        Assert.assertThat(objectType.getFields(),
                notNullValue());
        for (GraphQLInputObjectField field : objectType.getFields()) {
            Assert.assertThat(field,
                    notNullValue());
            Assert.assertThat(field.getDescription(),
                    containsString("Autogenerated f"));
        }
        Assert.assertThat(objectType.getFields().size(),
                is(expectedNumFields));
    }
}
