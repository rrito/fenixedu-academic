<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.UploadCandidacyReview"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RatifyCandidacy"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddStudyPlan"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RejectCandidacyProcess"%><html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<jsp:include page="/phd/academicAdminOffice/alertMessagesNotifier.jsp?global=false" />

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>

		<fr:view schema="PhdIndividualProgramProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td style="vertical-align: top; padding-top: 1em;">
    	<bean:define id="process" name="process" />
		<bean:define id="candidacyProcess" name="process" property="candidacyProcess" />
		
    	<ul class="operations">
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=prepareEditPersonalInformation" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.editPersonalInformation"/>
				</html:link>
			</li>
			<phd:activityAvailable process="<%= process %>" activity="<%= AddStudyPlan.class %>">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=manageStudyPlan" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.studyPlanAndQualificationExams"/>
					</html:link>
				</li>
			</phd:activityAvailable>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=prepareEditQualificationsAndJobsInformation" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.editQualificationsAndJobsInformation"/>
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=prepareEditPhdIndividualProgramProcessInformation" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.editPhdIndividualProgramProcessInformation"/>
				</html:link>
			</li>
			<li>
				<html:link action="/payments.do?method=showOperations" target="_blank" paramId="personId" paramName="process" paramProperty="person.idInternal">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.payments"/>
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manageGuidingInformation"/>
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=manageAlerts" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manageAlerts"/>
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.alertMessages"/>
				</html:link>
			</li>
		</ul>
    </td>
  </tr>
</table>

<br/>
<strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
		<fr:view schema="PhdProgramCandidacyProcess.view" name="process" property="candidacyProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
			</fr:layout>
		</fr:view>
	</td>
  </tr>
</table>
<ul class="operations">
	<li style="display: inline;">
		<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyDocuments" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.manageCandidacyDocuments"/>
		</html:link>
	</li>
	<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= UploadCandidacyReview.class %>">
		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.manageCandidacyReview"/>
			</html:link>
		</li>
	</phd:activityAvailable>
	<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= RequestCandidacyReview.class %>">
		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=prepareRequestCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.request.candidacy.review"/>
		</html:link>
		</li>
	</phd:activityAvailable>
	<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= RejectCandidacyProcess.class %>" >
		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=prepareRejectCandidacyProcess" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.rejectCandidacyProcess"/>
			</html:link>
		</li>
	</phd:activityAvailable>
	<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= RatifyCandidacy.class %>" >
		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=prepareRatifyCandidacy" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.ratifyCandidacy"/>
			</html:link>
		</li>
	</phd:activityAvailable>
	<li style="display: inline;">
		<html:link action="/phdProgramCandidacyProcess.do?method=manageNotifications" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.notifications"/>
		</html:link>
	</li>
</ul>	


<%--  ### End Of Context Information  ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>