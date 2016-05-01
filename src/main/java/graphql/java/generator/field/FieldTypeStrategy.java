package graphql.java.generator.field;

import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.strategies.Strategy;
import graphql.schema.GraphQLType;

public interface FieldTypeStrategy extends Strategy {
    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @return
     */
    GraphQLType getTypeOfField(Object object, TypeKind typeKind);
}
