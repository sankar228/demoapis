package com.tmo.demo.demo.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.diff.SchemaDiff;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {

    @Value("classpath:crypto.graphql")
    Resource resource;

    private GraphQL graphQL;

    public GraphQL getGraphQL() {
        return graphQL;
    }

    @Autowired
    private AllCryptoDataFetcher allCryptoDataFetcher;
    @Autowired
    private CryptoDataFetcher cryptoDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        File schemafile = resource.getFile();

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemafile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

        graphQL = GraphQL.newGraphQL(schema).build();

    }

    private RuntimeWiring buildRuntimeWiring() {

        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typewiring -> typewiring
                        .dataFetcher("allCrypto", allCryptoDataFetcher)
                        .dataFetcher("crypto", cryptoDataFetcher))
                .build();
    }
}
