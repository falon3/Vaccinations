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
import org.openmrs.module.vaccinations.*;
import org.openmrs.module.vaccinations.enums.Excuses;
import org.openmrs.module.vaccinations.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(ManufacturersService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface UtilsService extends OpenmrsService {

	@Transactional(readOnly =  true)
	@Authorized( {PrivilegeConstants.VIEW_VACCINES} )
	public List<Manufacturer> getAllManufacturers(Boolean includeRetired) throws APIException;

    @Transactional(readOnly =  true)
    @Authorized( {PrivilegeConstants.VIEW_AUDIT_LOG} )
    public List<AuditLog> getAuditLogByVaccinationId(int vaccinationId) throws APIException;

    @Transactional(readOnly =  true)
    @Authorized( {PrivilegeConstants.VIEW_AUDIT_LOG} )
    public List<AuditLogLineItem> getAuditLogLineItems(int auditLogId) throws APIException;

    @Transactional(readOnly =  true)
    public AuditLog saveOrUpdateAuditLog(AuditLog auditLog) throws APIException;

    @Transactional(readOnly =  true)
    public AuditLogLineItem saveOrUpdateAuditLogLineItem(AuditLogLineItem auditLogLineItem) throws APIException;

    @Transactional(readOnly =  true)
    public AuditLog createAuditLogRecord(Vaccination oldVaccination, Vaccination simpleVaccination, String excuse, String reason) throws APIException;

    @Transactional(readOnly =  true)
    public AuditLog createAuditLogRecord(Vaccination oldVaccination, Vaccination simpleVaccination, String excuse, String reason, boolean unadminister) throws APIException;
}