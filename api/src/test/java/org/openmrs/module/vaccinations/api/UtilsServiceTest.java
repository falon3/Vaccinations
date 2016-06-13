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

import org.junit.Before;
import org.junit.Test;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.vaccinations.*;
import org.openmrs.module.vaccinations.enums.Excuses;
import org.openmrs.test.BaseModuleContextSensitiveTest;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests {@link {VaccinationsService}}.
 */
public class UtilsServiceTest extends BaseModuleContextSensitiveTest {

    private UtilsService utilsService;
    private VaccinationsService vaccinationsService;

    @Before
    public void before() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet("src/test/resources/VaccinationsTestData.xml");
        utilsService = Context.getService(UtilsService.class);
        vaccinationsService = Context.getService(VaccinationsService.class);
        Context.getUserService().setUserProperty(Context.getAuthenticatedUser(), "defaultLocation", "1");
        PersonName personName = new PersonName();
        personName.setFamilyName("Johnson");
        personName.setGivenName("Jonny");
        personName.setDateCreated(new Date());
        personName.setCreator(Context.getUserService().getUser(1));
        Context.getUserService().getUser(1).addName(personName);
    }

	@Test
	public void shouldSetupContext() throws Exception{
        assertNotNull(utilsService);
        assertNotNull(vaccinationsService);
	}

    @Test
    public void shouldReturnManufacturers(){
        logger.debug("Getting all manufacturers");
        List<Manufacturer> manufacturers = utilsService.getAllManufacturers(true);
        assertEquals(2, manufacturers.size());
    }

    @Test
    public void shouldReturnAuditLog(){
        logger.debug("Getting all audit logs");
        List<AuditLog> auditLogList = utilsService.getAuditLogByVaccinationId(1);
        assertEquals(2, auditLogList.size());
        assertEquals(3, auditLogList.get(0).getAuditLogLineItemList().size());
        assertEquals(Excuses.NoExcuse.toString(), auditLogList.get(0).getExcuse());
    }

    @Test
    public void shouldReturnEmptyAuditLog(){
        logger.debug("Getting all audit logs");
        List<AuditLog> auditLogList = utilsService.getAuditLogByVaccinationId(2);
        assertEquals(0, auditLogList.size());
        //assertEquals(3, auditLogList.get(0).getAuditLogLineItemList().size());
        //assertEquals(Excuses.NoExcuse.toString(), auditLogList.get(0).getExcuse());
    }

    @Test
    public void shouldCreateNewAuditLogRecordAndLineItems(){
        //Acquiring an unscheduled vaccination
        Date [] DateRange = {new Date(), new Date()};
        Vaccine vaccine = Context.getService(VaccinesService.class).getVaccineByUuid("d5f0f111-0a36-11e5-ba5b-005056be863d");
        Vaccination vaccination = vaccinationsService.vaccineToVaccination(vaccine, DateRange);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        vaccination.setPatient_id(3);
        vaccination.setDose(500.00);
        vaccination.setAdministered(true);
        vaccination.setScheduled_date(c.getTime());
        vaccination.setScheduled(true);
        vaccination.setAdministration_date(c.getTime());
        vaccination.setBody_site_administered("Nah");
        vaccination.setSide_administered_left(true);
        vaccination.setAdverse_reaction_observed(false);
        vaccination.setDosing_unit("162366");
        vaccination.setExpiry_date(c.getTime());
        vaccination.setLot_number("meh");
        vaccination.setManufacture_date(c.getTime());
        vaccination.setManufacturer("Vaccines Inc");
        vaccination.setDose_number(1);
        vaccination.setCreator(vaccine.getCreator());
        vaccination.setDateCreated(c.getTime());
        vaccination.setRoute("160240");


        SimpleVaccination newVac = new SimpleVaccination(vaccination);
        vaccination = vaccinationsService.saveOrUpdateVaccination(vaccination);
        assertFalse(vaccination.getAuditLogList().size() > 0);

        //Setting up adverse reaction
        AdverseReaction adverseReaction = new AdverseReaction();
        adverseReaction.setVaccination_id(vaccination.getId());
        adverseReaction.setGrade("OMG");
        adverseReaction.setAdverse_event("Blood Clots");
        adverseReaction.setDate(c.getTime());
        adverseReaction.setCreator(vaccine.getCreator());
        adverseReaction.setDateCreated(c.getTime());

        AdverseReaction adverseReaction1 = Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(adverseReaction);
        assertNotNull(adverseReaction1);
        //assertEquals(new Integer(4), adverseReaction1.getId());
        assertNull(vaccination.getAdverse_reaction());
        //vaccination.setAdverse_reaction(adverseReaction1);
        Vaccination oldVaccination = new Vaccination();
        vaccination = vaccinationsService.saveOrUpdateVaccination(vaccination);
        oldVaccination.setVaccination(vaccination);


        c.add(Calendar.DATE, 1);

        vaccination.setScheduled_date(c.getTime());
        vaccination.setDose(600.00);
        vaccination.setAdministered(false);
        vaccination.setScheduled(false);
        vaccination.setAdministration_date(c.getTime());
        vaccination.setBody_site_administered("Blah");
        vaccination.setSide_administered_left(false);
        vaccination.setAdverse_reaction_observed(true);
        vaccination.setDosing_unit("162382");
        vaccination.setExpiry_date(c.getTime());
        vaccination.setLot_number("another");
        vaccination.setManufacture_date(c.getTime());
        vaccination.setManufacturer("Vaccinations Inc");
        vaccination.setDose_number(2);
        vaccination.setRoute("160243");

        adverseReaction1.setGrade("GMO");
        Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(adverseReaction1);
        //vaccination1.setAdverse_reaction(adverseReaction1);
        Vaccination vaccination1 = vaccinationsService.saveOrUpdateVaccination(vaccination);

        utilsService.createAuditLogRecord(oldVaccination, vaccination1, Excuses.NoExcuse.getName(), "No reason provided");

        assertEquals(new Double(600), vaccination1.getDose());
        assertEquals(new Double(500), oldVaccination.getDose());

        //Ensuring that the Audit Log was properly created
        assertEquals(1, oldVaccination.getAuditLogList().size());
        assertTrue(oldVaccination.getAuditLogList().get(0).getAuditLogLineItemList().size() > 0);
        //Ensuring that all the
        for (AuditLogLineItem auditLogLineItem : oldVaccination.getAuditLogList().get(0).getAuditLogLineItemList()){
            logger.error("Comparing field " + auditLogLineItem.getField() + ":" + auditLogLineItem.getOriginal_value() + " and " + auditLogLineItem.getNew_value());
            assertTrue(!auditLogLineItem.getOriginal_value().equals(auditLogLineItem.getNew_value()));
        }
    }

    @Test
    public void shouldNotCreateNewAuditLogRecordAndLineItems(){
        //Acquiring an unscheduled vaccination
        Date [] DateRange = {new Date(), new Date()};
        Vaccine vaccine = Context.getService(VaccinesService.class).getVaccineByUuid("d5f0f111-0a36-11e5-ba5b-005056be863d");
        Vaccination vaccination = vaccinationsService.vaccineToVaccination(vaccine, DateRange);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        vaccination.setPatient_id(3);
        vaccination.setDose(500.00);
        vaccination.setAdministered(true);
        vaccination.setScheduled_date(c.getTime());
        vaccination.setScheduled(true);
        vaccination.setAdministration_date(c.getTime());
        vaccination.setBody_site_administered("Nah");
        vaccination.setSide_administered_left(true);
        vaccination.setAdverse_reaction_observed(false);
        vaccination.setDosing_unit("162366");
        vaccination.setExpiry_date(c.getTime());
        vaccination.setLot_number("meh");
        vaccination.setManufacture_date(c.getTime());
        vaccination.setManufacturer("Vaccines Inc");
        vaccination.setDose_number(1);
        vaccination.setCreator(vaccine.getCreator());
        vaccination.setDateCreated(c.getTime());


        SimpleVaccination newVac = new SimpleVaccination(vaccination);
        vaccination = vaccinationsService.saveOrUpdateVaccination(vaccination);
        assertFalse(vaccination.getAuditLogList().size() > 0);

        //Setting up adverse reaction
        AdverseReaction adverseReaction = new AdverseReaction();
        adverseReaction.setVaccination_id(vaccination.getId());
        adverseReaction.setGrade("OMG");
        adverseReaction.setAdverse_event("Blood Clots");
        adverseReaction.setDate(c.getTime());
        adverseReaction.setCreator(vaccine.getCreator());
        adverseReaction.setDateCreated(c.getTime());

        AdverseReaction adverseReaction1 = Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(adverseReaction);
        assertNotNull(adverseReaction1);
        //assertEquals(new Integer(3), adverseReaction1.getId());
        assertNull(vaccination.getAdverse_reaction());
        //vaccination.setAdverse_reaction(adverseReaction1);
        Vaccination oldVaccination = new Vaccination();
        vaccination = vaccinationsService.saveOrUpdateVaccination(vaccination);
        oldVaccination.setVaccination(vaccination);


        /*c.add(Calendar.DATE, 1);

        vaccination.setScheduled_date(c.getTime());
        vaccination.setDose(600.00);
        vaccination.setAdministered(false);
        vaccination.setScheduled(false);
        vaccination.setAdministration_date(c.getTime());
        vaccination.setBody_site_administered("Blah");
        vaccination.setSide_administered_left(false);
        vaccination.setAdverse_reaction_observed(true);
        vaccination.setDosing_unit("mg");
        vaccination.setExpiry_date(c.getTime());
        vaccination.setLot_number("another");
        vaccination.setManufacture_date(c.getTime());
        vaccination.setManufacturer("Vaccinations Inc");
        vaccination.setDose_number(2);*/

        adverseReaction1.setGrade("GMO");
        Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(adverseReaction1);
        //vaccination1.setAdverse_reaction(adverseReaction1);
        Vaccination vaccination1 = vaccinationsService.saveOrUpdateVaccination(vaccination);

        utilsService.createAuditLogRecord(oldVaccination, vaccination1, Excuses.NoExcuse.getName(), "No reason provided");

        //assertEquals(new Double(600), vaccination1.getDose());
        //assertEquals(new Double(500), oldVaccination.getDose());

        assertEquals(0, oldVaccination.getAuditLogList().size());
        //assertTrue(vaccination1.getAuditLogList().size() > 0);
        //assertEquals(Excuses.NoExcuse.getName(), vaccination1.getAuditLogList().get(0).getExcuse());
        //assertEquals(0, vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().size());

        /*
        assertEquals("Scheduled Date", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(0).getField());
        assertEquals("Fri Aug 14 18:34:45 MDT 2015", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(0).getOriginal_value());
        assertEquals("Fri Aug 14 18:34:45 MDT 2015", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(0).getNew_value());

        assertEquals("Administration Date", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(1).getField());
        assertEquals("Manufacture Date", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(2).getField());
        assertEquals("Expiry Date", vaccination1.getAuditLogList().get(0).getAuditLogLineItemList().get(3).getField());
        */
    }
}
