package dev.service;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.exception.CalculException;
public class CalculService {
	private static final Logger LOG = LoggerFactory.getLogger(CalculService.class);

	public int additionner(String expression) {

		LOG.debug("Evaluation de l'expression ".concat(expression));

		int cal = 0;
		try {
			cal = Stream.of(expression.split("\\+")).mapToInt(Integer::parseInt).sum();

		} catch (RuntimeException e) {
			throw new CalculException();
		}
		return cal;
	}

}