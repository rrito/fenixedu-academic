package ServidorAplicacao.strategy.enrolment.degree;

import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLARQ;
import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLERCI;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyFactory implements IEnrolmentStrategyFactory {

	private static EnrolmentStrategyFactory instance = null;

	private EnrolmentStrategyFactory() {
	}

	public static synchronized EnrolmentStrategyFactory getInstance() {
		if (instance == null) {
			instance = new EnrolmentStrategyFactory();
		}
		return instance;
	}

	public static synchronized void resetInstance() {
		if (instance != null) {
			instance = null;
		}
	}



	public IEnrolmentStrategy getEnrolmentStrategyInstance(EnrolmentContext enrolmentContext) {
		
		IEnrolmentStrategy strategyInstance = null;

		if (enrolmentContext.getStudent() == null)
			throw new IllegalArgumentException("Must initialize student in context!");

		if (enrolmentContext.getSemester() == null)
			throw new IllegalArgumentException("Must initialize semester in context!");
			
		if (enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled() == null)
			throw new IllegalArgumentException("Must initialize FinalCurricularCoursesScopesSpanToBeEnrolled in context!");

		if (enrolmentContext.getStudentActiveCurricularPlan() == null)
			throw new IllegalArgumentException("Must initialize StudentActiveCurricularPlan in context!");

		String degree = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegree().getSigla();
		String degreeCurricularPlan = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getName();
		// FIXME: David-Ricardo: O nome do plano curricular e estratégias tem de ser alterados 
		if ( (degree.equals("LERCI")) && degreeCurricularPlan.equals("LERCI-2003")) {
			strategyInstance = new EnrolmentStrategyLERCI();
			strategyInstance.setEnrolmentContext(enrolmentContext);
		} else if ( (degree.equals("LARQ")) && degreeCurricularPlan.equals("LARQ-2003")) {
			strategyInstance = new EnrolmentStrategyLARQ();
			strategyInstance.setEnrolmentContext(enrolmentContext);
		}else{
			throw new IllegalArgumentException("Degree or DegreeCurricularPlan invalid!");
		}
		return strategyInstance;
	}

}