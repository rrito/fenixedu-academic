/*
 * Created on Jan 10, 2005
 */
package ServidorApresentacao.Action.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoAttendsSummary;
import DataBeans.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoForReadStudentsWithAttendsByExecutionCourse;
import DataBeans.InfoFrequenta;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoShift;
import DataBeans.InfoStudentGroup;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.AttendacyStateSelectionType;
import Util.EnrolmentEvaluationType;
import Util.TipoAula;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Andre Fernandes / Joao Brito
 */
public class DownloadStudentsWithAttendsByExecutionCourseListAction extends FenixAction {
    private static final String SEPARATOR = "\t";

    private static final String NEWLINE = "\n";

    private static final String NULL = "--";

    private static final String NOT_AVAILABLE = "N/A";

    private static final String STUDENT_NUMBER = "N�mero";

    private static final String NUMBER_OF_ENROLLMENTS = "N�mero total de Inscri��es";

    private static final String ATTENDACY_TYPE = "Tipo de Inscri��o";

    private static final String ATTENDACY_TYPE_NORMAL = "Normal";

    private static final String ATTENDACY_TYPE_NOT_ENROLLED = "N�o Inscrito";

    private static final String ATTENDACY_TYPE_IMPROVEMENT = "Melhoria";

    private static final String COURSE = "Curso";

    private static final String NAME = "Nome";

    private static final String GROUP = "Agrupamento: ";

    private static final String EMAIL = "E-Mail";

    private static final String SHIFT = "Turno ";

    private static final String THEORETICAL = "Te�rico";

    private static final String LABORATORIAL = "Laboratorial";

    private static final String PRACTICAL = "Pr�tico";

    private static final String THEO_PRACTICAL = "Te�rico-Pr�tico";

    private static final String SUMMARY = "Resumo:";

    private static final String NUMBER_ENROLLMENTS = "N�mero de inscri��es";

    private static final String NUMBER_STUDENTS = "N�mero de alunos";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        HttpSession session = request.getSession(false);
        Integer executionCourseID = null;
        List coursesIDs = null;
        List enrollmentTypeList = null;
        List shiftIDs = null;
        try {
            executionCourseID = new Integer(request.getParameter("objectCode"));

        } catch (NumberFormatException ex) {
            // ok, we don't want to view a shift's student list
        }

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        String checkedCoursesIds[] = request.getParameterValues("coursesIDs");
        String enrollmentType[] = request.getParameterValues("enrollmentType");
        String checkedShiftIds[] = request.getParameterValues("shiftIDs");

        enrollmentTypeList = new ArrayList();
        for (int i = 0; i < enrollmentType.length; i++) {
            if (enrollmentType[i].equals(AttendacyStateSelectionType.ALL.toString())) {
                enrollmentTypeList = null;
                break;
            } else {
                enrollmentTypeList.add(new AttendacyStateSelectionType(enrollmentType[i]));
            }
        }

        coursesIDs = new ArrayList();
        for (int i = 0; i < checkedCoursesIds.length; i++) {
            if (checkedCoursesIds[i].equals("0")) {
                coursesIDs = null;
                break;
            } else {
                Integer courseID = new Integer(Integer.parseInt(checkedCoursesIds[i]));
                coursesIDs.add(courseID);
            }
        }

        shiftIDs = new ArrayList();
        for (int i = 0; i < checkedShiftIds.length; i++) {
            if (checkedShiftIds[i].equals("0")) {
                shiftIDs = null;
                break;
            } else {
                Integer shiftID = new Integer(Integer.parseInt(checkedShiftIds[i]));
                shiftIDs.add(shiftID);
            }
        }

        Object args[] = { executionCourseID, coursesIDs, enrollmentTypeList, shiftIDs };
        TeacherAdministrationSiteView siteView = null;

        InfoForReadStudentsWithAttendsByExecutionCourse infoDTO;

        siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentsWithAttendsByExecutionCourse", args);

        infoDTO = (InfoForReadStudentsWithAttendsByExecutionCourse) siteView.getComponent();

        Collections.sort(infoDTO.getInfoAttends(), new BeanComparator("infoAttends.aluno.number"));

        String fileContents = new String();

        // building the table header
        fileContents += STUDENT_NUMBER + SEPARATOR;
        fileContents += NUMBER_OF_ENROLLMENTS + SEPARATOR;
        fileContents += ATTENDACY_TYPE + SEPARATOR;
        fileContents += COURSE + SEPARATOR;
        fileContents += NAME + SEPARATOR;

        List groupProperties = infoDTO.getInfoGroupProperties();
        if (groupProperties != null && !groupProperties.isEmpty()) {
            Iterator gpIterator = groupProperties.iterator();
            while (gpIterator.hasNext()) {
                InfoGroupProperties gp = (InfoGroupProperties) gpIterator.next();
                fileContents += GROUP + gp.getName() + SEPARATOR;
            }
        }

        fileContents += EMAIL + SEPARATOR;

        List classTypes = infoDTO.getClassTypes();
        if (classTypes != null && !classTypes.isEmpty()) {
            Iterator ctIterator = classTypes.iterator();
            while (ctIterator.hasNext()) {
                TipoAula classType = (TipoAula) ctIterator.next();

                String classTypeString = new String();
                if (classType.equals(new TipoAula(TipoAula.TEORICA))) {
                    classTypeString = THEORETICAL;
                } else if (classType.equals(new TipoAula(TipoAula.PRATICA))) {
                    classTypeString = PRACTICAL;
                } else if (classType.equals(new TipoAula(TipoAula.LABORATORIAL))) {
                    classTypeString = LABORATORIAL;
                } else if (classType.equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
                    classTypeString = THEO_PRACTICAL;
                }

                fileContents += SHIFT + classTypeString + SEPARATOR;
            }
        }

        fileContents += NEWLINE;

        // building each line
        List attends = infoDTO.getInfoAttends();
        Iterator attendsIterator = attends.iterator();
        while (attendsIterator.hasNext()) {
            InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups attendacy = (InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups) attendsIterator
                    .next();
            InfoFrequenta infoAttends = attendacy.getInfoAttends();

            // student number
            fileContents += infoAttends.getAluno().getNumber() + SEPARATOR;

            // number of enrollments
            fileContents += (attendacy.getNumberOfEnrollments().intValue() == 0 ? NULL : attendacy
                    .getNumberOfEnrollments().toString())
                    + SEPARATOR;

            // attendacy type (normal, improvement, not enrolled)
            String attendacyType = "";
            if (infoAttends.getInfoEnrolment() != null) {
                if (infoAttends.getInfoEnrolment().getEnrolmentEvaluationType().equals(
                        EnrolmentEvaluationType.NORMAL_OBJ)) {
                    attendacyType = ATTENDACY_TYPE_NORMAL;
                } else if (infoAttends.getInfoEnrolment().getEnrolmentEvaluationType().equals(
                        EnrolmentEvaluationType.IMPROVEMENT_OBJ)) {
                    attendacyType = ATTENDACY_TYPE_IMPROVEMENT;
                }
            } else
                attendacyType = ATTENDACY_TYPE_NOT_ENROLLED;

            fileContents += attendacyType + SEPARATOR;

            // course name
            InfoDegreeCurricularPlan infoDCP = attendacy.getAttendingStudentInfoDCP();
            InfoDegree degree = infoDCP.getInfoDegree();

            String courseName = degree.getSigla();
            Date dataInicio = infoDCP.getInitialDate();
            Calendar dataInicioCalendar = new GregorianCalendar();
            dataInicioCalendar.setTime(dataInicio);
            int year = dataInicioCalendar.get(Calendar.YEAR);

            fileContents += courseName + " " + year + SEPARATOR;

            // student name
            String shortName = infoAttends.getAluno().getInfoPerson().getNome();
            String[] names = shortName.split(" ");
            String firstName = names[0];
            String lastName = names[names.length - 1];
            shortName = firstName + " " + lastName;
            fileContents += shortName + SEPARATOR;

            // student groups
            if (groupProperties != null && !groupProperties.isEmpty()) {
                Iterator gpIterator = groupProperties.iterator();
                Map studentGroups = attendacy.getInfoStudentGroups();
                while (gpIterator.hasNext()) {
                    InfoGroupProperties gp = (InfoGroupProperties) gpIterator.next();
                    String groupNumber = "";
                    InfoStudentGroup infoStudentGroup = (InfoStudentGroup) studentGroups.get(gp
                            .getName());

                    if (infoStudentGroup != null)
                        groupNumber = infoStudentGroup.getGroupNumber().toString();
                    else
                        groupNumber = NOT_AVAILABLE;

                    fileContents += groupNumber + SEPARATOR;
                }
            }

            // student e-mail
            fileContents += infoAttends.getAluno().getInfoPerson().getEmail() + SEPARATOR;

            // student shifts
            if (classTypes != null && !classTypes.isEmpty()) {
                Iterator ctIterator = classTypes.iterator();
                Map studentShifts = attendacy.getInfoShifts();
                while (ctIterator.hasNext()) {
                    TipoAula classType = (TipoAula) ctIterator.next();
                    String shiftNameString = "";
                    InfoShift shift = (InfoShift) studentShifts.get(classType.getSiglaTipoAula());

                    if (shift != null)
                        shiftNameString = shift.getNome();
                    else
                        shiftNameString = NOT_AVAILABLE;

                    fileContents += shiftNameString + SEPARATOR;
                }
            }

            fileContents += NEWLINE;

        }

        fileContents += NEWLINE;
        fileContents += SUMMARY + NEWLINE;

        // statistics table
        InfoAttendsSummary infoAttendsSummary = infoDTO.getInfoAttendsSummary();
        List keys = infoAttendsSummary.getNumberOfEnrollments();
        Iterator keysIterator = keys.iterator();

        fileContents += NUMBER_ENROLLMENTS + SEPARATOR + NUMBER_STUDENTS + NEWLINE;
        while (keysIterator.hasNext()) {
            Integer key = (Integer) keysIterator.next();
            fileContents += key + SEPARATOR + infoAttendsSummary.getEnrollmentDistribution().get(key)
                    + NEWLINE;
        }

        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("plain/text");
            StringBuffer fileName = new StringBuffer();
            Calendar now = new GregorianCalendar();
            fileName.append("listaDeAlunos_");
            fileName.append(infoDTO.getInfoExecutionCourse().getSigla()).append("_").append(
                    now.get(Calendar.DAY_OF_MONTH));
            fileName.append("-").append(now.get(Calendar.MONTH)).append("-").append(
                    now.get(Calendar.YEAR));
            fileName.append(".csv");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            writer.print(fileContents);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e1) {
            throw new FenixActionException();
        }

        return null;
    }

}
