package dev.service;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.exception.CalculException;
public class CalculService {
	private static final Logger LOG = LoggerFactory.getLogger(CalculService.class);

	public int additionner(String expression) {

		// TODO Ajouter un log en niveau DEBUG affichant "Evaluation de
		// l'expression <expression>".
		// TODO par exemple "Evaluation de l'expression 1+4"
		LOG.debug("Evaluation de l'expression ".concat(expression));

		// ScriptEngineManager mgr = new ScriptEngineManager();
		// ScriptEngine engine = mgr.getEngineByName("JavaScript");
		// int result = (Integer) engine.eval(expression);
		int cal = 0;
		try {
			cal = Stream.of(expression.split("\\+")).mapToInt(Integer::parseInt).sum();

		} catch (RuntimeException e) {
			throw new CalculException();
		}
		return cal;
	}

}