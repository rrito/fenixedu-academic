package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private DomainReference<PhdProgram> program;

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<Degree> degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    private String email;

    private String captcha;

    private DomainReference<PhdProgramPublicCandidacyHashCode> candidacyHashCode;

    private DomainReference<PhdProgramFocusArea> focusArea;

    private List<QualificationBean> qualifications;

    private List<PhdCandidacyRefereeBean> candidacyReferees;

    private boolean generateCandidacyDebt = true;

    public PhdProgramCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
    }

    public LocalDate getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public PhdProgram getProgram() {
	return (this.program != null) ? this.program.getObject() : null;
    }

    public void setProgram(PhdProgram program) {
	this.program = (program != null) ? new DomainReference<PhdProgram>(program) : null;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public Person getOrCreatePersonFromBean() {
	if (!getPersonBean().hasPerson()) {
	    Person person = new Person(getPersonBean());
	    getPersonBean().setPerson(person);
	    return person;
	}

	if (getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)) {
	    return getPersonBean().getPerson();
	}

	return getPersonBean().getPerson().edit(personBean);
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public ExecutionDegree getExecutionDegree() {
	return hasDegree() ? null : getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
		getExecutionYear().getAcademicInterval());
    }

    public String getThesisTitle() {
	return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
	this.thesisTitle = thesisTitle;
    }

    public PhdIndividualProgramCollaborationType getCollaborationType() {
	return collaborationType;
    }

    public void setCollaborationType(PhdIndividualProgramCollaborationType collaborationType) {
	this.collaborationType = collaborationType;
    }

    public String getOtherCollaborationType() {
	return otherCollaborationType;
    }

    public void setOtherCollaborationType(String otherCollaborationType) {
	this.otherCollaborationType = otherCollaborationType;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getCaptcha() {
	return captcha;
    }

    public void setCaptcha(String captcha) {
	this.captcha = captcha;
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyHashCode() {
	return (this.candidacyHashCode != null) ? this.candidacyHashCode.getObject() : null;
    }

    public void setCandidacyHashCode(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
	this.candidacyHashCode = (candidacyHashCode != null) ? new DomainReference<PhdProgramPublicCandidacyHashCode>(
		candidacyHashCode) : null;
    }

    public PhdProgramFocusArea getFocusArea() {
	return (this.focusArea != null) ? this.focusArea.getObject() : null;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
	this.focusArea = (focusArea != null) ? new DomainReference<PhdProgramFocusArea>(focusArea) : null;
    }

    public List<QualificationBean> getQualifications() {
	return qualifications;
    }

    public void setQualifications(List<QualificationBean> qualifications) {
	this.qualifications = qualifications;
    }

    public void addQualification(final QualificationBean qualification) {
	this.qualifications.add(qualification);
    }

    public void removeQualification(int index) {
	this.qualifications.remove(index);
    }

    public List<PhdCandidacyRefereeBean> getCandidacyReferees() {
	return candidacyReferees;
    }

    public void setCandidacyReferees(List<PhdCandidacyRefereeBean> candidacyReferees) {
	this.candidacyReferees = candidacyReferees;
    }

    public void addCandidacyReferee(PhdCandidacyRefereeBean phdCandidacyRefereeBean) {
	this.candidacyReferees.add(phdCandidacyRefereeBean);
    }

    public void removeCandidacyReferee(int index) {
	this.candidacyReferees.remove(index);
    }

    public void clearPerson() {
	getPersonBean().setPerson(null);
    }

    public boolean generateCandidacyDebt() {
	return generateCandidacyDebt;
    }

    public void setGenerateCandidacyDebt(boolean generateCandidacyDebt) {
	this.generateCandidacyDebt = generateCandidacyDebt;
    }

}
