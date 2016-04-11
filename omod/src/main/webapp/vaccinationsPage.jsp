<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Vaccinations" otherwise="/login.htm" redirect="/index.htm" />

<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />

<c:choose>
	<c:when test="${retroactive != 'true'}">
		<a href="../vaccinations/vaccinationsPage.form?patientId=${patientId}&retroactive=true">Enable retroactive data entry</a>
	</c:when>
	<c:otherwise>
		<a href="../vaccinations/vaccinationsPage.form?patientId=${patientId}&retroactive=false">Disable retroactive data entry</a>
	</c:otherwise>
</c:choose>
<br/>
<a href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patientId}" ><h1 align="center">${patientName}</h1></a>

<style>
table.al { border-radius: 4px; border: 1px solid #ddd; border-collapse: inherit; }
table.al td { border-left: 1px solid #ddd;padding-left; 10px;text-align: top; }
table.al td:first-child { border-left: none;text-align: top; }
</style>

<div style="margin: auto;padding-left:5%;">
<table style="padding: 10px;" class="legend-wrapper">
      <tr><td style="text-align: right;">Gender : </td><td> ${patientGender} </td></tr>
      <tr><td style="text-align: right;">Age : </td><td> ${patientAge} </td></tr>
      <tr><td style="text-align: right;">Birthdate : </td><td> ${patientBirthDate} </td></tr>
</table>
</div>

<openmrs:portlet id="Vaccinations" moduleId="vaccinations" url="vaccinationsPortlet" />

<%@ include file="/WEB-INF/template/footer.jsp"%>