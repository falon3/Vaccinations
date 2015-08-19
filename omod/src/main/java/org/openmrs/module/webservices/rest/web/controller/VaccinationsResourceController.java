/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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
package org.openmrs.module.webservices.rest.web.controller;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.openmrs.api.context.Context;
import org.openmrs.module.vaccinations.*;
import org.openmrs.module.vaccinations.api.AdverseReactionsService;
import org.openmrs.module.vaccinations.api.UtilsService;
import org.openmrs.module.vaccinations.api.VaccinationsService;
import org.openmrs.module.vaccinations.api.VaccinesService;
import org.openmrs.module.vaccinations.enums.*;
import org.openmrs.module.vaccinations.util.Constants;
import org.openmrs.module.vaccinations.util.Serializers.RouteMapEnumSerializer;
import org.openmrs.module.webservices.rest.web.RestConstants;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.ModelMap;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController; //To potentially remove
import org.springframework.web.servlet.ModelAndView;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

//For debugging
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_2 + "/vaccinationsmodule")
public class VaccinationsResourceController {// extends MainResourceController {

    //private AdverseReactionsService adverseReactionsService = Context.getService(AdverseReactionsService.class);
    //private VaccinationsService vaccinationsService = Context.getService(VaccinationsService.class);
    //private VaccinesService vaccinesService = Context.getService(VaccinesService.class);
    protected final Log log = LogFactory.getLog(this.getClass());
    private SessionFactory sessionFactory;
	/*@Override
	public String getNamespace() {
		return  RestConstants.VERSION_2 + "/vaccinations/";
	}*/

	@RequestMapping(value = "/vaccines/unscheduled", method = RequestMethod.GET)
	@ResponseBody
	public List<SimpleVaccine> getUnscheduledVaccinesSimple() {
		return Context.getService(VaccinesService.class).getUnscheduledVaccinesSimple(false);
	}

	@RequestMapping(value = "/vaccines/template", method = RequestMethod.POST)
	@ResponseBody
	public SimpleVaccination generateSimpleVaccinationTemplate(@RequestBody SimpleVaccine simpleVaccine) {
		SimpleVaccination simpleVaccination = new SimpleVaccination();
		simpleVaccination.setSimpleVaccine(simpleVaccine);
		return simpleVaccination;
	}

    //Legacy functionality
	@RequestMapping(value = "/vaccinations/patient/{patientId}", method = RequestMethod.GET)
	@ResponseBody
	public List<SimpleVaccination> getVaccinations(@PathVariable int patientId) {
        List<SimpleVaccination> simpleVaccinations = Context.getService(VaccinationsService.class).combineVaccinesAndVaccinationsByPatientIdSimple(patientId);
        //Context.clearSession();
		return simpleVaccinations;
	}

    @RequestMapping(value = "/vaccinations/enums/patient/{patientId}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> getVaccinationsAndEnums(@PathVariable int patientId) {
        List<Object> objects = new ArrayList<Object>();
        List<SimpleVaccination> simpleVaccinations = Context.getService(VaccinationsService.class).combineVaccinesAndVaccinationsByPatientIdSimple(patientId);
        List<Manufacturer> manufacturers = Context.getService(UtilsService.class).getAllManufacturers(false);

//        List<RouteMapping> routeMappingList = new ArrayList<RouteMapping>();
//
//        ArrayList<String> bodySitesArrayList = new ArrayList<String>();
//        bodySitesArrayList.add(BodySites.Thigh.getName());
//        bodySitesArrayList.add(BodySites.Buttock.getName());
//        bodySitesArrayList.add(BodySites.Deltoid.getName());
//
//        RouteMapping routeMapping = new RouteMapping();
//        routeMapping.setConceptId(Routes.Intramuscular.getConceptId());
//        routeMapping.setBodySitesList(bodySitesArrayList);
//
//        routeMappingList.add(routeMapping);

        objects.add(simpleVaccinations);
        objects.add(new Routes[] {Routes.Oral, Routes.Intramuscular, Routes.Subcutaneous, Routes.Intranasal, Routes.Transdermal, Routes.Intradermal});
        objects.add(new DosingUnits[] {DosingUnits.International, DosingUnits.Ampule, DosingUnits.Drop, DosingUnits.Ounce, DosingUnits.Gram, DosingUnits.Milligram, DosingUnits.Milliequivalent, DosingUnits.Microgram, DosingUnits.Milliliter, DosingUnits.Tablet, DosingUnits.Unit, DosingUnits.Vial});
        objects.add(new BodySites[] {BodySites.Thigh, BodySites.Buttock, BodySites.UpperArmDeltoid, BodySites.UpperArmTricep, BodySites.OuterForearm, BodySites.InnerForearm, BodySites.Mouth, BodySites.Nostril, BodySites.LowerLeg, BodySites.Abdomen, BodySites.Chest, BodySites.Back});
        objects.add(manufacturers);
        objects.add(new Excuses[] {Excuses.Expired, Excuses.OutOfStock, Excuses.WrongVaccine, Excuses.NoExcuse});
        objects.add(new RouteMapEnum[] {RouteMapEnum.Intradermal, RouteMapEnum.Intramuscular, RouteMapEnum.Oral, RouteMapEnum.Intranasal, RouteMapEnum.Subcutaneous, RouteMapEnum.Transdermal});
        //Context.clearSession();
        return objects;
    }

	@RequestMapping(value = "/vaccinations/patient/{patientId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleVaccination saveVaccination(@RequestBody SimpleVaccination simpleVaccination, @PathVariable int patientId) throws Exception {
		try {
			simpleVaccination.setPatient_id(patientId);
            return new SimpleVaccination(Context.getService(VaccinationsService.class).saveOrUpdateVaccination(new Vaccination(simpleVaccination)));
		}catch (Exception ex){
			throw ex;
		}
	}

	@RequestMapping(value = "/vaccinations/{vaccinationId}/patient/{patientId}", method = RequestMethod.PUT)
	@ResponseBody
	public SimpleVaccination updateVaccination(
			@RequestBody SimpleVaccination simpleVaccination, @PathVariable int vaccinationId, @PathVariable int patientId) throws Exception {
		try
		{
			simpleVaccination.setPatient_id(patientId);

			//If an object already exists in the session, save using that object
			Vaccination oldVaccination = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(vaccinationId);

			Vaccination oldLogVaccination = new Vaccination(new SimpleVaccination(oldVaccination));
			Vaccination newLogVaccination = new Vaccination(simpleVaccination);
			Context.getService(UtilsService.class).createAuditLogRecord(oldLogVaccination, newLogVaccination, simpleVaccination.getExcuse(), simpleVaccination.getReason());

            if (oldVaccination != null) {
				oldVaccination = new Vaccination(simpleVaccination);
				return new SimpleVaccination(Context.getService(VaccinationsService.class).saveOrUpdateVaccination(oldVaccination));
            } else {
                return new SimpleVaccination(Context.getService(VaccinationsService.class).saveOrUpdateVaccination(new Vaccination(simpleVaccination)));
			}
		}catch (Exception ex){
			throw ex;
		}
	}

    /*
    *
    */
	@RequestMapping(value = "/vaccinations/{vaccinationId}/patient/{patientId}", method = RequestMethod.DELETE)
	@ResponseBody
	public SimpleVaccination deleteVaccination(@PathVariable int vaccinationId, @PathVariable int patientId) {

		Vaccination vaccination1 = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(vaccinationId);
		Vaccination oldLogVaccination = vaccination1;
		vaccination1.setRetired(true);
		vaccination1.setRetiredBy(Context.getAuthenticatedUser());
		vaccination1.setDateRetired(new Date());
		vaccination1.setRetireReason("Deleted");
		SimpleVaccination simpleVaccination2 = new SimpleVaccination(Context.getService(VaccinationsService.class).saveOrUpdateVaccination(vaccination1));

		Vaccination newLogVaccination = new Vaccination(simpleVaccination2);
		//Context.getService(UtilsService.class).createAuditLogRecord(oldLogVaccination, newLogVaccination); //All this should be is an logUnadminister method call

		//check if vaccination is based on a scheduled vaccine
		if (vaccination1.getScheduled()){

			//returns a new vaccination template
			//simpleVaccination2 = new SimpleVaccination(Context.getService(VaccinationsService.class).vaccineToVaccination(vaccination1.getVaccine(), Context.getService(VaccinationsService.class).calculateScheduledDate(patientId,vaccination1.getVaccine())));
            //simpleVaccination2.setId(vaccinationId);
            //simpleVaccination2.setUuid(vaccination1.getUuid());

		}else{
			//returns a retired vaccination body
		}
		return simpleVaccination2;
	}

	@RequestMapping(value = "/adversereactions/patient/{patientId}/vaccinations/{vaccinationId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleVaccination saveAdverseReaction(
			@RequestBody SimpleAdverseReaction simpleAdverseReaction, @PathVariable int patientId, @PathVariable int vaccinationId) {
		simpleAdverseReaction.setVaccination_id(vaccinationId);
        AdverseReaction adverseReaction = Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(new AdverseReaction(simpleAdverseReaction));

        Vaccination vaccination = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(vaccinationId);
        vaccination.setAdverse_reaction(adverseReaction);
        vaccination.setAdverse_reaction_observed(true);
		return new SimpleVaccination(Context.getService(VaccinationsService.class).saveOrUpdateVaccination(vaccination));
    }

    @RequestMapping(value = "/adversereactions/{adverseReactionId}/patient/{patientId}", method = RequestMethod.PUT)
	@ResponseBody
	public SimpleVaccination updateAdverseReaction(
			@RequestBody SimpleAdverseReaction simpleAdverseReaction, @PathVariable int adverseReactionId, @PathVariable int patientId) {

		Vaccination oldLogVaccination = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(simpleAdverseReaction.getVaccination_id());
        //Vaccination oldLogVaccination = new Vaccination(new SimpleVaccination(oldVaccination));

		simpleAdverseReaction = new SimpleAdverseReaction(Context.getService(AdverseReactionsService.class).saveOrUpdateAdverseReaction(new AdverseReaction(simpleAdverseReaction)));
		Vaccination newLogVaccination = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(simpleAdverseReaction.getVaccination_id());
		//Context.getService(UtilsService.class).createAuditLogRecord(oldLogVaccination, newLogVaccination, excuse, reason);

        return new SimpleVaccination(newLogVaccination);
	}

	@RequestMapping(value = "/adversereactions/{adverseReactionId}/patient/{patientId}", method = RequestMethod.DELETE)
	@ResponseBody
	public SimpleVaccination deleteAdverseReaction(@PathVariable int adverseReactionId, @PathVariable int patientId) {
		//Lookup adverse reaction
		AdverseReaction adverseReaction = Context.getService(AdverseReactionsService.class).getAdverseReactionByAdverseReactionId(adverseReactionId);

		//Lookup vaccination by id from adverse reaction
		Vaccination vaccination1 = Context.getService(VaccinationsService.class).getVaccinationByVaccinationId(adverseReaction.getVaccination_id());
		Vaccination oldLogVaccination = vaccination1;

		//set adverse reaction retired to true and retirer to authenticated user
		adverseReaction.setRetired(true);
		adverseReaction.setRetiredBy(Context.getAuthenticatedUser());
		adverseReaction.setDateRetired(new Date());
		adverseReaction.setRetireReason("Deleted");
		//set vaccination's adverse reaction to the adjusted adverse reaction.
		vaccination1.setAdverse_reaction(adverseReaction);

        vaccination1.setAdverse_reaction_observed(false);
		//save or update vaccination
		Vaccination newLogVaccination = Context.getService(VaccinationsService.class).saveOrUpdateVaccination(vaccination1);
		//Context.getService(UtilsService.class).createAuditLogRecord(oldLogVaccination, newLogVaccination, excuse, reason, unadminister);

		return new SimpleVaccination(newLogVaccination);
	}
}

