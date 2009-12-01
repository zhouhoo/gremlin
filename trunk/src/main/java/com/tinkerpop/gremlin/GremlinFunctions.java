package com.tinkerpop.gremlin;

import com.tinkerpop.gremlin.functions.*;
import com.tinkerpop.gremlin.statements.EvaluationErrorException;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @version 0.1
 */
public class GremlinFunctions implements Functions {

    public static final String NAMESPACE_PREFIX = "g";

    private static Set<String> namespaces = new HashSet<String>();
    private static final Map<String, Function> functionMap = new HashMap<String, Function>();

    static {
        namespaces.add(NAMESPACE_PREFIX);
        // variables
        functionMap.put(AssignFunction.FUNCTION_NAME, new AssignFunction());
        functionMap.put(UnassignFunction.FUNCTION_NAME, new UnassignFunction());
        // graphs
        functionMap.put(IdFunction.FUNCTION_NAME, new IdFunction());
        // sets
        functionMap.put(AsSetFunction.FUNCTION_NAME, new AsSetFunction());
        functionMap.put(UnionFunction.FUNCTION_NAME, new UnionFunction());
        functionMap.put(IntersectFunction.FUNCTION_NAME, new IntersectFunction());
        functionMap.put(DifferenceFunction.FUNCTION_NAME, new DifferenceFunction());
        functionMap.put(RetainFunction.FUNCTION_NAME, new RetainFunction());
        functionMap.put(ExceptFunction.FUNCTION_NAME, new ExceptFunction());
        // lists
        functionMap.put(AsListFunction.FUNCTION_NAME, new AsListFunction());
        functionMap.put(AppendFunction.FUNCTION_NAME, new AppendFunction());
        functionMap.put(RemoveFunction.FUNCTION_NAME, new RemoveFunction());
        // maps
        functionMap.put(AsMapFunction.FUNCTION_NAME, new AsMapFunction());
        functionMap.put(KeysFunction.FUNCTION_NAME, new KeysFunction());
        functionMap.put(ValuesFunction.FUNCTION_NAME, new ValuesFunction());
        // lists and maps
        functionMap.put(GetFunction.FUNCTION_NAME, new GetFunction());
        // numbers
        functionMap.put(RandomRealFunction.FUNCTION_NAME, new RandomRealFunction());
        functionMap.put(RandomNaturalFunction.FUNCTION_NAME, new RandomNaturalFunction());
        // flow control
        functionMap.put(HaltFunction.FUNCTION_NAME, new HaltFunction());
        functionMap.put(ContinueFunction.FUNCTION_NAME, new ContinueFunction());
        // utilities
        functionMap.put(TypeFunction.FUNCTION_NAME, new TypeFunction());
    }

    public Function getFunction(String namespace, String name, Object[] parameters) {
        Function function = functionMap.get(name);
        if (null != function)
            return function;


        throw new EvaluationErrorException("Function " + namespace + ":" + name + " does not exist.");
    }

    public Set getUsedNamespaces() {
        return namespaces;
    }
}