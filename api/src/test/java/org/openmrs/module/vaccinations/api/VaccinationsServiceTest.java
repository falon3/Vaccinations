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

import static org.junit.Assert.*;


import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.vaccinations.*;
import org.openmrs.module.vaccinations.enums.Excuses;
import org.openmrs.test.BaseModuleContextSensitiveTest;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Tests {@link {VaccinationsService}}.
 */
public class  VaccinationsServiceTest extends BaseModuleContextSensitiveTest {

    private VaccinationsService vaccinationsService;
    private SessionFactory sessionFactory;

    @Before
    public void before() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet("src/test/resources/VaccinationsTestData.xml");
        vaccinationsService = Context.getService(VaccinationsService.class);
        Context.getUserService().setUserProperty(Context.getAuthenticatedUser(), "defaultLocation", "1");
    }

    @Test
    public void shouldSetupContext() throws Exception{
        assertNotNull(vaccinationsService);
    }

    @Test
    public void shouldReturnVaccinationByUuid(){
        logger.debug("Looking for Vaccination by Uuid");
        Vaccination vaccination = vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c28c22");
        assertNotNull(vaccination);
        assertEquals(new Integer(1), vaccination.getId());
    }

    //This test will not fail if more than one unscheduled vaccination is added to the test data file
    @Test
    public void shouldReturnListPatientVaccinations(){
        logger.debug("Looking for Patient Vaccinations");

        //Acquiring an unscheduled vaccination
        Vaccination vaccination = vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11");
        assertNotNull(vaccination);
        assertEquals(new Integer(2), vaccination.getId());

        List<Vaccination> vaccinationList = vaccinationsService.listVaccinationsByPatientId(3);
        assertTrue(vaccinationList.contains(vaccination));
    }

    //This test check that audit logs are retrieved as well
    @Test
    public void shouldReturnListPatientVaccinationsAndAuditLogs(){
        logger.debug("Looking for Patient Vaccinations");

        //Acquiring an unscheduled vaccination
        Vaccination vaccination = vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11");
        assertNotNull(vaccination);
        assertEquals(new Integer(2), vaccination.getId());

        List<Vaccination> vaccinationList = vaccinationsService.listVaccinationsByPatientId(3);
        assertTrue(vaccinationList.contains(vaccination));

        assertEquals(2, vaccinationList.get(0).getAuditLogList().size());
    }

    //This test check that empty audit logs are retrieved as well
    @Test
    public void shouldReturnSimplePatientVaccinationAndEmptyAuditLog(){
        logger.debug("Looking for Patient Vaccinations");

        //Acquiring an unscheduled vaccination
        Vaccination vaccination = vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11");
        assertNotNull(vaccination);
        assertEquals(new Integer(2), vaccination.getId());
        assertEquals(0, vaccination.getAuditLogList().size());

        SimpleVaccination simpleVaccination = new SimpleVaccination(vaccination);
        assertEquals(0, simpleVaccination.getAuditLogList().size());
    }

    //This test will not fail if more than one unscheduled vaccination is added to the test data file
    @Test
    public void shouldReturnListOfSimplePatientVaccinations(){
        logger.debug("Looking for Simple Patient Vaccinations");

        //Acquiring an unscheduled vaccination
        SimpleVaccination simpleVaccination = new SimpleVaccination(vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11"));
        assertNotNull(simpleVaccination);
        assertEquals(new Integer(2), simpleVaccination.getId());

        List<SimpleVaccination> vaccinationList = vaccinationsService.listSimpleVaccinationsByPatientId(3);
        assertTrue(vaccinationList.size() > 0);
    }

    //This test will not fail if more than one unscheduled vaccination is added to the test data file
    @Test
    public void shouldSaveOrUpdatePatientVaccination(){
        logger.warn("Updating Patient Vaccinations");

        //Acquiring an unscheduled vaccination
        SimpleVaccination simpleVaccination = new SimpleVaccination(vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11"));
        assertNotNull(simpleVaccination);
        assertEquals(new Integer(2), simpleVaccination.getId());
        assertEquals(new String("AMREF"), simpleVaccination.getClinic_location());
        assertEquals(new String("Hank Williams"), simpleVaccination.getAdministered_by());
        assertTrue(simpleVaccination.getSide_administered_left());

        simpleVaccination.setDose(500.00);
        simpleVaccination.setSide_administered_left(false);

        Vaccination vaccination = new Vaccination(simpleVaccination);
        assertEquals(new String("6304a894-7806-44ad-97c6-0d1e04c18c11"), vaccination.getUuid());
        assertEquals(new Integer(2), vaccination.getId());


        vaccinationsService.saveOrUpdateVaccination(vaccination);

        Vaccination vaccination1 = vaccinationsService.getVaccinationByUuid("6304a894-7806-44ad-97c6-0d1e04c18c11");
        assertEquals(new Double(500.00), vaccination1.getDose());
        assertNotNull(vaccination.getClinic_location());
        assertEquals(new String("AMREF"), vaccination1.getClinic_location().getName());
        assertFalse(simpleVaccination.getSide_administered_left());
    }

    //This test will not fail if more than one unscheduled vaccination is added to the test data file
    @Test
    public void shouldSavePatientVaccination(){
        logger.warn("Saving Patient Vaccinations");
        Date[] calculatedDateRange = {new Date(), new Date()};

        //Acquiring an unscheduled vaccination
        Vaccine vaccine = Context.getService(VaccinesService.class).getVaccineByUuid("d5f0f111-0a36-11e5-ba5b-005056be863d");
        Vaccination vaccination = vaccinationsService.vaccineToVaccination(vaccine, calculatedDateRange);

        //TODO: Ensure that adverse reaction is automatically assigned on constructor call
        vaccination.setPatient_id(3);
        vaccination.setDose(500.00);
        vaccination.setAdministered(true);
        vaccination.setScheduled(false);
        vaccination.setAdministration_date(new Date());
        vaccination.setBody_site_administered("Nah");
        vaccination.setSide_administered_left(true);
        vaccination.setAdverse_reaction_observed(false);
        vaccination.setDosing_unit("bla");
        vaccination.setExpiry_date(new Date());
        vaccination.setLot_number("meh");
        vaccination.setManufacture_date(new Date());
        vaccination.setManufacturer("Vaccines Inc");
        vaccination.setDose_number(1);
        vaccination.setCreator(vaccine.getCreator());
        vaccination.setDateCreated(new Date());

        Vaccination vaccination1 = vaccinationsService.saveOrUpdateVaccination(vaccination);

        Vaccination vaccination2 = vaccinationsService.getVaccinationByUuid(vaccination1.getUuid());
        assertEquals(new Double(500.00), vaccination2.getDose());
        assertTrue(vaccination2.getSide_administered_left());
        //assertEquals(new Integer(2), vaccination.getId());
    }

    @Test
    public void shouldCalculateDateRangeForVaccination(){
        logger.error("Testing calculated date ranges");
        Date fakeBirthday = new Date(0);
        Calendar cal = Calendar.getInstance();
        try {
            fakeBirthday = new SimpleDateFormat("dd-mm-yyyy").parse("01-01-2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(fakeBirthday);
        logger.error("start first should be:"+ cal.getTime().toString());
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(fakeBirthday);

        Vaccine vaccine = Context.getService(VaccinesService.class).getVaccineByUuid("d5f0b023-0a36-11e5-ba5b-005056be863d");
        Date[] calculatedDateRange = vaccinationsService.calculateDateRange(fakeBirthday, vaccine);

        cal.add(Calendar.DATE, vaccine.getMin_age()); //start date
        cal1.add(Calendar.DATE, vaccine.getMax_age()); //end date

//        logger.error("start should be:"+ cal.getTime().toString());
//        logger.error("start actually:" + calculatedDateRange[0].toString());
//        logger.error("end should be:" + cal1.getTime().toString());
//        logger.error("end actually:" + calculatedDateRange[1].toString());

        assertTrue(cal.getTime().equals(calculatedDateRange[0]));
        assertTrue(cal1.getTime().equals(calculatedDateRange[1]));
    }
}

