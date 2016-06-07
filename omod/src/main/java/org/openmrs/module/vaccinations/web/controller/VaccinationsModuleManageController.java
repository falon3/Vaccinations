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
package org.openmrs.module.vaccinations.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.activelist.Allergy;
import org.openmrs.api.context.Context;
import org.openmrs.module.vaccinations.Vaccine;
import org.openmrs.module.vaccinations.api.VaccinationsService;
import org.openmrs.module.vaccinations.api.VaccinesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The main controller.
 */
@Controller
public class  VaccinationsModuleManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/vaccinations/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
		//model.put("vaccines", Context.getService(VaccinesService.class).getAllVaccines(false));
		//model.put("vaccinations", Context.getService(VaccinationsService.class).combineVaccinesAndVaccinationsByPatientId(3));
	}

    @RequestMapping(value = "/module/vaccinations/vaccinationsPatientSelector", method = RequestMethod.GET)
    public void vitalsPatientSelector(ModelMap model) {
        model.addAttribute("user", Context.getAuthenticatedUser());
    }

    @RequestMapping(value = "/module/vaccinations/portlets/vaccinationsPortlet", method = RequestMethod.GET)
    public void vaccinationsPortletController(ModelMap model) {
        model.addAttribute("user", Context.getAuthenticatedUser());
    }

    @RequestMapping(value = "/module/vaccinations/vaccinationsPage", method = RequestMethod.GET)
    public void vaccinationsPageController(@RequestParam(required = true, value = "patientId") String patientId,
                                           @RequestParam(required = false, value = "retroactive") String retroactive, ModelMap model) {
        model.addAttribute("patientId", patientId);
        model.addAttribute("user", Context.getAuthenticatedUser());
        model.addAttribute("DO_NOT_INCLUDE_JQUERY", true);
        model.addAttribute("retroactive", retroactive);
        model.addAttribute("patientId", patientId);

        //get patient information
        Patient person = Context.getPatientService().getPatient(Integer.parseInt(patientId));
        PersonName personName = person.getPersonName();
        String patientName = "";
        if (personName.getGivenName() != null)
            patientName += personName.getGivenName();
        if (personName.getMiddleName() != null)
            patientName += " " + personName.getMiddleName();
        if (personName.getFamilyName() != null)
            patientName += " " + personName.getFamilyName();
        model.addAttribute("patientName", patientName);
        model.addAttribute("patientGender",person.getGender());
        //format patient birthdate
        String pattern = "dd MMM, yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date birthdate = person.getBirthdate();
        Date now = new Date();
        String birthdayout = format.format(person.getBirthdate());
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(birthdate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(now);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if(diffYear > 0){
            if(diffYear == 1) {
                model.addAttribute("patientAge",diffYear+" Year");
            }else{
                model.addAttribute("patientAge",diffYear+" Years");
            }
        }else{
            if(diffMonth == 1) {
                model.addAttribute("patientAge", diffMonth + " Month");
            }else{
                model.addAttribute("patientAge", diffMonth + " Months");
            }
        }
        model.addAttribute("patientBirthDate",birthdayout);

        model.addAttribute("user", Context.getAuthenticatedUser());
    }

}