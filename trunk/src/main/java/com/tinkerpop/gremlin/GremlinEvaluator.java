package com.tinkerpop.gremlin;

import com.tinkerpop.gremlin.statements.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @version 0.1
 */
public class GremlinEvaluator {

    private Statement currentStatement;
    private XPathEvaluator xPathEvaluator;

    public GremlinEvaluator() {
        this.xPathEvaluator = new XPathEvaluator();
        this.currentStatement = null;
    }

    public List evaluate(String line) throws SyntaxException, EvaluationException {
        line = line.trim();
        if (line.length() > 0) {
            try {
                if (null == this.currentStatement) {
                    this.currentStatement = StatementGenerator.generateStatement(line, xPathEvaluator);
                    if (this.currentStatement instanceof ScriptStatement) {
                        ((ScriptStatement) this.currentStatement).setGremlinEvaluator(this);
                    }
                    this.currentStatement.compileTokens(line);
                } else {
                    this.currentStatement.compileTokens(line);
                }

                if (this.currentStatement.isComplete()) {
                    Statement temp = this.currentStatement;
                    this.currentStatement = null;
                    return temp.evaluate();
                } else {
                    return null;
                }
            } catch (EvaluationException e) {
                this.currentStatement = null;
                throw e;
            } catch (SyntaxException e) {
                this.currentStatement = null;
                throw e;
            } catch (Exception e) {
                this.currentStatement = null;
                throw new SyntaxException(e.getMessage());
            }
        }
        return null;
    }

    public List evaluate(InputStream input) throws IOException, SyntaxException, EvaluationException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        List result = null;
        while ((line = reader.readLine()) != null) {
            result = this.evaluate(line);
        }
        return result;
    }

    public void setVariable(String variable, Object value) {
        this.xPathEvaluator.setVariable(variable, value);
    }

    public Object getVariable(String variable) {
        return this.xPathEvaluator.getVariable(variable);
    }

    public void removeVariable(String variable) {
        this.xPathEvaluator.removeVariable(variable);
    }

    protected boolean inStatement() {
        return null != this.currentStatement;
    }

    protected int getDepth() {
        return this.xPathEvaluator.getDepth();
    }

}
