/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoSection;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteExam;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteTimetable;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IExam;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 *
 *
 */
public class ExecutionCourseSiteComponentBuilder {

	private static ExecutionCourseSiteComponentBuilder instance = null;

	public ExecutionCourseSiteComponentBuilder() {
	}

	public static ExecutionCourseSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new ExecutionCourseSiteComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(
		ISiteComponent component,
		ISite site,
		ISiteComponent commonComponent,
		Integer sectionIndex)
		throws FenixServiceException {

		// updateSite(executionYearName, executionPeriodName, executionCourseName);

		if (component instanceof InfoSiteCommon) {
			return getInfoSiteCommon((InfoSiteCommon) component, site);
		} else if (component instanceof InfoSiteFirstPage) {
			return getInfoSiteFirstPage((InfoSiteFirstPage) component, site);

		} else if (component instanceof InfoSiteAnnouncement) {
			return getInfoSiteAnnouncement(
				(InfoSiteAnnouncement) component,
				site);
		} else if (component instanceof InfoSiteObjectives) {
			return getInfoSiteObjectives((InfoSiteObjectives) component, site);
		} else if (component instanceof InfoSiteProgram) {
			return getInfoSiteProgram((InfoSiteProgram) component, site);

		} else if (component instanceof InfoEvaluation) {
			return getInfoEvaluation((InfoEvaluation) component, site);
		} else if (component instanceof InfoSiteBibliography) {
			return getInfoSiteBibliography(
				(InfoSiteBibliography) component,
				site);
		} else if (component instanceof InfoSiteAssociatedCurricularCourses) {
			return getInfoSiteAssociatedCurricularCourses(
				(InfoSiteAssociatedCurricularCourses) component,
				site);
		} else if (component instanceof InfoSiteTimetable) {
			return getInfoSiteTimetable((InfoSiteTimetable) component, site);
		} else if (component instanceof InfoSiteShifts) {
			return getInfoSiteShifts((InfoSiteShifts) component, site);

		} else if (component instanceof InfoSiteSection) {
			return getInfoSiteSection(
				(InfoSiteSection) component,
				site,
				(InfoSiteCommon) commonComponent,
				sectionIndex);
		} else if (component instanceof InfoSiteExam) {
			return getInfoSiteExam((InfoSiteExam) component, site);
		}
		return null;
	}

	/**
	 * @param exam
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteExam(InfoSiteExam component, ISite site) {
		
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		List exams = executionCourse.getAssociatedExams();
		List infoExams = new ArrayList();
		Iterator iter = exams.iterator();
		while (iter.hasNext()){
			IExam exam = (IExam) iter.next();
			infoExams.add(Cloner.copyIExam2InfoExam(exam));
		}
		component.setInfoExams(infoExams);
		return component;
	}

	/**
	 * @param common
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteCommon(
		InfoSiteCommon component,
		ISite site)
		throws FenixServiceException {

		ISuportePersistente sp;
		List allSections = null;
		List infoSectionsList = null;
		List infoCurricularCourseScopeList = null;
		List infoCurricularCourseList = null;
		try {
			// read sections	

			sp = SuportePersistenteOJB.getInstance();
			allSections = sp.getIPersistentSection().readBySite(site);

			// build the result of this service
			Iterator iterator = allSections.iterator();
			infoSectionsList = new ArrayList(allSections.size());

			while (iterator.hasNext())
				infoSectionsList.add(
					Cloner.copyISection2InfoSection(
						(ISection) iterator.next()));

			Collections.sort(infoSectionsList);

			// read degrees

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			infoCurricularCourseList = readCurricularCourses(executionCourse);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		component.setAssociatedDegrees(infoCurricularCourseList);
		component.setTitle(site.getExecutionCourse().getNome());
		component.setMail(site.getMail());
		component.setSections(infoSectionsList);
		InfoExecutionCourse executionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				site.getExecutionCourse());
		component.setExecutionCourse(executionCourse);
		return component;
	}

	/**
	 * @param section
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteSection(
		InfoSiteSection component,
		ISite site,
		InfoSiteCommon commonComponent,
		Integer sectionIndex)
		throws FenixServiceException {

		List sections = commonComponent.getSections();
		InfoSection infoSection =
			(InfoSection) sections.get(sectionIndex.intValue());
		component.setSection(infoSection);
		List itemsList = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();

			ISection section = Cloner.copyInfoSection2ISection(infoSection);

			itemsList = persistentItem.readAllItemsBySection(section);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		List infoItemsList = new ArrayList(itemsList.size());
		Iterator iter = itemsList.iterator();

		while (iter.hasNext())
			infoItemsList.add(Cloner.copyIItem2InfoItem((IItem) iter.next()));

		Collections.sort(infoItemsList);
		component.setItems(infoItemsList);

		return component;
	}

	/**
	 * @param shifts
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteShifts(
		InfoSiteShifts component,
		ISite site)
		throws FenixServiceException {
		List shiftsWithAssociatedClassesAndLessons = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao disciplinaExecucao = site.getExecutionCourse();
			List shifts =
				sp.getITurnoPersistente().readByExecutionCourse(
					disciplinaExecucao);

			if (shifts == null || shifts.isEmpty()) {

			} else {

				for (int i = 0; i < shifts.size(); i++) {
					ITurno shift = (ITurno) shifts.get(i);
					InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons =
						new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
							Cloner.copyShift2InfoShift(shift),
							null,
							null);

					List lessons =
						sp.getITurnoAulaPersistente().readByShift(shift);
					List infoLessons = new ArrayList();
					List classesShifts =
						sp.getITurmaTurnoPersistente().readClassesWithShift(
							shift);
					List infoClasses = new ArrayList();

					for (int j = 0; j < lessons.size(); j++)
						infoLessons.add(
							Cloner.copyILesson2InfoLesson(
								(IAula) lessons.get(j)));

					shiftWithAssociatedClassesAndLessons.setInfoLessons(
						infoLessons);

					for (int j = 0; j < classesShifts.size(); j++)
						infoClasses.add(
							Cloner.copyClass2InfoClass(
								((ITurmaTurno) classesShifts.get(j))
									.getTurma()));

					shiftWithAssociatedClassesAndLessons.setInfoClasses(
						infoClasses);

					shiftsWithAssociatedClassesAndLessons.add(
						shiftWithAssociatedClassesAndLessons);
				}
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		component.setShifts(shiftsWithAssociatedClassesAndLessons);
		component.setInfoExecutionPeriodName(site.getExecutionCourse().getExecutionPeriod().getName());
		component.setInfoExecutionYearName(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
		return component;
	}

	/**
	 * @param timetable
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteTimetable(
		InfoSiteTimetable component,
		ISite site)
		throws FenixServiceException {
		ArrayList infoLessonList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			List aulas =
				sp.getIAulaPersistente().readByExecutionCourse(executionCourse);

			Iterator iterator = aulas.iterator();
			infoLessonList = new ArrayList();
			while (iterator.hasNext()) {
				IAula elem = (IAula) iterator.next();
				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
				infoLessonList.add(infoLesson);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
		component.setLessons(infoLessonList);
		return component;
	}

	/**
	 * @param courses
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteAssociatedCurricularCourses(
		InfoSiteAssociatedCurricularCourses component,
		ISite site)
		throws FenixServiceException {
		List infoCurricularCourseList = new ArrayList();
		List infoCurricularCourseScopeList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			infoCurricularCourseList = readCurricularCourses(executionCourse);
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException(ex);
			newEx.fillInStackTrace();
			throw newEx;
		}

		component.setAssociatedCurricularCourses(infoCurricularCourseList);
		return component;
	}

	private List readCurricularCourses(IDisciplinaExecucao executionCourse) {
		List infoCurricularCourseScopeList;
		List infoCurricularCourseList = new ArrayList();
		if (executionCourse.getAssociatedCurricularCourses() != null)
			for (int i = 0;
				i < executionCourse.getAssociatedCurricularCourses().size();
				i++) {
				ICurricularCourse curricularCourse =
					(ICurricularCourse) executionCourse
						.getAssociatedCurricularCourses()
						.get(
						i);
				InfoCurricularCourse infoCurricularCourse =
					Cloner.copyCurricularCourse2InfoCurricularCourse(
						curricularCourse);
				infoCurricularCourseScopeList = new ArrayList();
				for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
					ICurricularCourseScope curricularCourseScope =
						(ICurricularCourseScope) curricularCourse
							.getScopes()
							.get(
							j);
					InfoCurricularCourseScope infoCurricularCourseScope =
						Cloner
							.copyICurricularCourseScope2InfoCurricularCourseScope(
							curricularCourseScope);
					infoCurricularCourseScopeList.add(
						infoCurricularCourseScope);
				}

				infoCurricularCourse.setInfoScopes(
					infoCurricularCourseScopeList);
				infoCurricularCourseList.add(infoCurricularCourse);

			}
		return infoCurricularCourseList;
	}

	/**
	 * @param bibliography
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteBibliography(
		InfoSiteBibliography component,
		ISite site)
		throws FenixServiceException {
		List references = null;
		List infoBibRefs = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentBibliographicReference persistentBibliographicReference =
				persistentBibliographicReference =
					sp.getIPersistentBibliographicReference();

			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			references =
				persistentBibliographicReference.readBibliographicReference(
					executionCourse);

			Iterator iterator = references.iterator();
			infoBibRefs = new ArrayList();
			while (iterator.hasNext()) {
				IBibliographicReference bibRef =
					(IBibliographicReference) iterator.next();

				InfoBibliographicReference infoBibRef =
					Cloner
						.copyIBibliographicReference2InfoBibliographicReference(
						bibRef);
				infoBibRefs.add(infoBibRef);

			}
			if (!infoBibRefs.isEmpty()) {
				component.setBibliographicReferences(infoBibRefs);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return component;
	}

	/**
	 * @param evaluation
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoEvaluation(
		InfoEvaluation component,
		ISite site)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = site.getExecutionCourse();
			IEvaluation evaluation =
				sp.getIPersistentEvaluation().readByExecutionCourse(
					executionCourse);
			if (evaluation != null) {
				component = Cloner.copyIEvaluation2InfoEvaluation(evaluation);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param program
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteProgram(
		InfoSiteProgram component,
		ISite site)
		throws FenixServiceException {
		try {

			ICurriculum curriculum = readCurriculum(site);

			InfoCurriculum infoCurriculum = null;
			if (curriculum != null) {
				infoCurriculum =
					Cloner.copyICurriculum2InfoCurriculum(curriculum);
				component.setProgram(infoCurriculum.getProgram());
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param objectives
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteObjectives(
		InfoSiteObjectives component,
		ISite site)
		throws FenixServiceException {
		try {

			ICurriculum curriculum = readCurriculum(site);

			InfoCurriculum infoCurriculum = null;
			if (curriculum != null) {
				infoCurriculum =
					Cloner.copyICurriculum2InfoCurriculum(curriculum);
				component.setGeneralObjectives(
					infoCurriculum.getGeneralObjectives());
				component.setOperacionalObjectives(
					infoCurriculum.getOperacionalObjectives());
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	private ICurriculum readCurriculum(ISite site)
		throws ExcepcaoPersistencia {
		ICurriculum curriculum = null;
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();

		ISuportePersistente sp;

		sp = SuportePersistenteOJB.getInstance();

		curriculum =
			sp.getIPersistentCurriculum().readCurriculumByExecutionCourse(
				executionCourse);
		return curriculum;
	}

	/**
	 * @param page
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteFirstPage(
		InfoSiteFirstPage component,
		ISite site)
		throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport =
				SuportePersistenteOJB.getInstance();

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			InfoAnnouncement infoAnnouncement =
				readLastAnnouncement(persistentSupport, executionCourse);

			List responsibleInfoTeachersList =
				readResponsibleTeachers(persistentSupport, executionCourse);

			List lecturingInfoTeachersList =
				readLecturingTeachers(persistentSupport, executionCourse);

			//set all the required information to the component	
			component.setLastAnnouncement(infoAnnouncement);
			component.setAlternativeSite(site.getAlternativeSite());
			component.setInitialStatement(site.getInitialStatement());
			component.setIntroduction(site.getIntroduction());
			component.setSiteIdInternal(site.getIdInternal());
			if (!responsibleInfoTeachersList.isEmpty()) {
				component.setResponsibleTeachers(responsibleInfoTeachersList);
			}
			lecturingInfoTeachersList.removeAll(responsibleInfoTeachersList);
			if (!lecturingInfoTeachersList.isEmpty()) {
				component.setLecturingTeachers(lecturingInfoTeachersList);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	private InfoSiteAnnouncement getInfoSiteAnnouncement(
		InfoSiteAnnouncement component,
		ISite site)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			List announcementsList =
				sp.getIPersistentAnnouncement().readAnnouncementsBySite(site);
			List infoAnnouncementsList = new ArrayList();

			if (announcementsList != null
				&& announcementsList.isEmpty() == false) {
				Iterator iterAnnouncements = announcementsList.iterator();
				while (iterAnnouncements.hasNext()) {
					IAnnouncement announcement =
						(IAnnouncement) iterAnnouncements.next();
					infoAnnouncementsList.add(
						Cloner.copyIAnnouncement2InfoAnnouncement(
							announcement));
				}
			}

			component.setAnnouncements(infoAnnouncementsList);
			return component;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private List readLecturingTeachers(
		ISuportePersistente persistentSupport,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		List domainLecturingTeachersList = null;
		IPersistentProfessorship persistentProfessorship =
			persistentSupport.getIPersistentProfessorship();
		domainLecturingTeachersList =
			persistentProfessorship.readByExecutionCourse(executionCourse);

		List lecturingInfoTeachersList = new ArrayList();
		if (domainLecturingTeachersList != null) {

			Iterator iter = domainLecturingTeachersList.iterator();
			while (iter.hasNext()) {
				IProfessorship professorship = (IProfessorship) iter.next();
				ITeacher teacher = professorship.getTeacher();
				InfoTeacher infoTeacher =
					Cloner.copyITeacher2InfoTeacher(teacher);
				lecturingInfoTeachersList.add(infoTeacher);
			}
		}
		return lecturingInfoTeachersList;
	}

	private List readResponsibleTeachers(
		ISuportePersistente persistentSupport,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		List responsibleDomainTeachersList = null;
		IPersistentTeacher persistentTeacher =
			persistentSupport.getIPersistentTeacher();
		IPersistentResponsibleFor persistentResponsibleFor =
			persistentSupport.getIPersistentResponsibleFor();
		responsibleDomainTeachersList =
			persistentResponsibleFor.readByExecutionCourse(executionCourse);

		List responsibleInfoTeachersList = new ArrayList();
		if (responsibleDomainTeachersList != null) {
			Iterator iter = responsibleDomainTeachersList.iterator();
			while (iter.hasNext()) {
				IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
				ITeacher teacher = responsibleFor.getTeacher();
				InfoTeacher infoTeacher =
					Cloner.copyITeacher2InfoTeacher(teacher);
				responsibleInfoTeachersList.add(infoTeacher);
			}

		}
		return responsibleInfoTeachersList;
	}

	private InfoAnnouncement readLastAnnouncement(
		ISuportePersistente persistentSupport,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		ISite site =
			persistentSupport.getIPersistentSite().readByExecutionCourse(
				executionCourse);
		IAnnouncement announcement =
			persistentSupport
				.getIPersistentAnnouncement()
				.readLastAnnouncementForSite(
				site);
		InfoAnnouncement infoAnnouncement = null;
		if (announcement != null)
			infoAnnouncement =
				Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
		return infoAnnouncement;
	}
}
