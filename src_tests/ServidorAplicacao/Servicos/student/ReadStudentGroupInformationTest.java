/*
 * Created on 24/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.student;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadStudentGroupInformationTest extends TestCaseReadServices {
	
	/**
  	* @param testName
  	*/
  	public ReadStudentGroupInformationTest(String testName) {
	 	super(testName);
	}
	
	public static void main(java.lang.String[] args) {
	   junit.textui.TestRunner.run(suite());
	 }

	/* (non-Javadoc)
  	* @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
  	*/
  	protected String getNameOfServiceToBeTested() {
	 	return "ReadStudentGroupInformation";
	}
       
	
   	public static Test suite() 
   	{
	 	TestSuite suite = new TestSuite(ReadStudentGroupInformationTest.class);
		return suite;
   	}

 	protected void setUp() 
 	{
		super.setUp();
 	}

 	protected void tearDown() 
 	{
	 	super.tearDown();
 	}



	/* (non-Javadoc)
  	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
  	*/
 	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	 	return null;
 	}

 	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
	 	return null;
 	}

 	/* (non-Javadoc)
  	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
  	*/
 	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	 ISuportePersistente persistentSupport = null;
	 IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	 ITurnoPersistente persistentShift = null;
	 
	 ITurno shift = null;
	 IDisciplinaExecucao executionCourse = null;
	 	 

	 try {
		persistentSupport = SuportePersistenteOJB.getInstance();
			
	   persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();		
	   persistentShift = persistentSupport.getITurnoPersistente();
	   persistentSupport.iniciarTransaccao();
		
	   executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","MEEC");
	  	
	   shift = persistentShift.readByNameAndExecutionCourse("turno_po_teorico",executionCourse);

	   persistentSupport.confirmarTransaccao();

	 } catch (ExcepcaoPersistencia e) {
		 System.out.println("failed setting up the test data");
	   }
	   Object[] args = {new Integer(1),"nameB", Cloner.copyIShift2InfoShift(shift).getIdInternal(), Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse).getIdInternal()};
	   return args;
 	}
	

	/* (non-Javadoc)
 	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
 	*/
	protected int getNumberOfItemsToRetrieve() {
	 	return 1;
	 }
        
 	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
 	protected Object getObjectToCompare() {
		return null;
	}

}
