/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.vaccinations.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.vaccinations.SimpleVaccination;
import org.openmrs.module.vaccinations.Vaccination;
import org.openmrs.module.vaccinations.Vaccine;
import org.openmrs.module.vaccinations.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(VaccinationsService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface VaccinationsService extends OpenmrsService {

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public List<Vaccination> listVaccinationsByPatientId(Integer patientId) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public Vaccination getVaccinationByVaccinationId(int vaccination_id) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.MANAGE_VACCINATIONS, PrivilegeConstants.MANAGE_ADVERSE_REACTIONS, PrivilegeConstants.MANAGE_VACCINES} )
	public Vaccination saveOrUpdateVaccination(Vaccination vaccination) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public List<SimpleVaccination> listSimpleVaccinationsByPatientId(Integer patientId) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public List<Vaccination> combineVaccinesAndVaccinationsByPatientId(Integer patientId) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public List<SimpleVaccination> combineVaccinesAndVaccinationsByPatientIdSimple(Integer patientId) throws APIException;

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINATIONS, PrivilegeConstants.VIEW_VACCINES, PrivilegeConstants.VIEW_ADVERSE_REACTIONS} )
	public Vaccination getVaccinationByUuid(String uuid) throws APIException;

	public Date [] calculateDateRange(Date patientBDay, Vaccine vaccine) throws APIException;

    public Date getPtBDay(Integer patientId) throws APIException;

	public List<SimpleVaccination> simplifyVaccinations(List<Vaccination> vaccines) throws APIException;

	public Vaccination vaccineToVaccination(Vaccine vaccine, Date [] calculatedDateRange) throws APIException;
}