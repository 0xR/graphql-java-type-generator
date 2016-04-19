package graphql.java.generator.field.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.field.FieldArgumentsStrategy;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInputType;

public class FieldArguments_Reflection implements FieldArgumentsStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            FieldArguments_Reflection.class);
    
    @Override
    public List<GraphQLArgument> getFieldArguments(Object object, BuildContext currentContext) {
        if (object instanceof Method) {
            Method method = (Method) object;
            return getFieldArgumentsFromMethod(method, currentContext);
        }

        return null;
    }

    protected List<GraphQLArgument> getFieldArgumentsFromMethod(Method method, BuildContext currentContext) {
        List<GraphQLArgument> fieldArgs = new ArrayList<GraphQLArgument>();
        Class<?>[] params = method.getParameterTypes();
        for (int index = 0; index < params.length; ++index) {
            Class<?> param = params[index];
            GraphQLInputType type = currentContext.getInputType(param);
            String name = param.getCanonicalName();
            String description = "Autogenerated arg from param [" + param.getCanonicalName() + "]";
            logger.debug("Argument at index [{}] is [{}] with name [{}]",
                    index, description, name);
            GraphQLArgument arg = GraphQLArgument.newArgument()
                    .type(type)
                    .name(name)
                    .description(description)
                    .build();
            fieldArgs.add(arg);
        }
        return fieldArgs;
    }
}
