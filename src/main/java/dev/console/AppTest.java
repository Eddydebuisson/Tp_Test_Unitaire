package dev.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.exception.CalculException;
import dev.service.CalculService;

public class AppTest {

	private static final Logger LOG = LoggerFactory.getLogger(AppTest.class);

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
	public ExpectedException exceptionlevee = ExpectedException.none();
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();
	private App app;
	private CalculService calculService;

	@Before
	public void setUp() throws Exception {
		Scanner sc = new Scanner(System.in);
		this.calculService = mock(CalculService.class);
		this.app = new App(sc, calculService);
	}

	@Test
	public void testAfficherTitre() throws Exception {
		this.app.afficherTitre();
		String logConsole = systemOutRule.getLog();
		assertThat(logConsole).contains("**** Application Calculatrice ****");
	}

	@Test
	public void testEvaluer() throws Exception {
		LOG.info("Etant donné, un service CalculService qui retourne 35 à l'évaluation de l'expression 1+34");
		String expression = "1+34";
		when(calculService.additionner(expression)).thenReturn(35);
		LOG.info("Lorsque la méthode evaluer est invoquée");
		this.app.evaluer(expression);
		LOG.info("Alors le service est invoqué avec l'expression {}", expression);
		verify(calculService).additionner(expression);
		LOG.info("Alors dans la console, s'affiche 1+34=35");
		assertThat(systemOutRule.getLog()).contains("1+34=35");

	}

	@Test
	public void testEvaluerException() throws Exception {
		String expression = "aze";
		when(calculService.additionner(expression)).thenThrow(new CalculException());
		this.app.evaluer(expression);
		verify(calculService).additionner(expression);
		assertThat(systemOutRule.getLog()).contains("L'expression " + expression + " est invalide");
	}

	@Test
	public void testAurevoir() throws Exception {
		systemInMock.provideLines("fin");
		this.app.demarrer();
		assertThat(systemOutRule.getLog()).contains("Aurevoir :-(");
	}

	@Test
	public void testOperationSuccessive() throws Exception {
		when(calculService.additionner("1+2")).thenReturn(3);
		systemInMock.provideLines("1+2", "fin");
		this.app.demarrer();
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("1+2=3");
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("Aurevoir :-(");
	}

	@Test
	public void testOperationSuccessiveAvecException() throws Exception {
		when(calculService.additionner("AAAA")).thenThrow(new CalculException());
		systemInMock.provideLines("AAAA", "fin");
		this.app.demarrer();
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("L'expression AAAA est invalide");
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("Aurevoir :-(");
	}

	@Test
	public void testOperationSuccessiveDeux() throws Exception {
		when(calculService.additionner("1+2")).thenReturn(3);
		when(calculService.additionner("30+2")).thenReturn(32);
		systemInMock.provideLines("1+2", "30+2", "fin");
		this.app.demarrer();
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("1+2=3");
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("30+2=32");
		assertThat(systemOutRule.getLog()).contains("Veuillez saisir une expression :");
		assertThat(systemOutRule.getLog()).contains("Aurevoir :-(");
	}

}