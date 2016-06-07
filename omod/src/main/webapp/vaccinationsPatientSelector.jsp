<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Vaccinations" otherwise="/login.htm" redirect="/index.htm" />

<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<script type="text/javascript" src="js/bootstrap.js"></script>

<%@ include file="/WEB-INF/template/header.jsp"%>


<h1><openmrs:message code="vaccinations.vaccinationsmenuitem"/></h1>
<h2><openmrs:message code="Patient.search"/></h2>	

<br />

<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|hideAddNewPatient=true|postURL=vaccinationsPage.form|showIncludeVoided=false|viewType=shortEdit" />

<%@ include file="/WEB-INF/template/footer.jsp"%>