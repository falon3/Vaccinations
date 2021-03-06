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
package org.openmrs.module.vaccinations;

//For debugging purposes
/*import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;*/


import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

/**
 * It is a model class. It should extend {@link BaseOpenmrsObject}. But it doesn't because BaseOpenmrsObject's behaviour is erratic.
 */
public class SimpleVaccine implements Serializable {

	//private static final long serialVersionUID = 1L;

	//For debugging purposes
	//protected final Log log = LogFactory.getLog(this.getClass());

	private Integer id;
	private String name;
	private String indication_name;
	private Double dose;
	private Integer dose_number;
	private String dosing_unit;
	private String route;
	private boolean scheduled;
    private Integer numeric_indication;
    private String body_site_administered;
    private boolean side_administered_left;
	private String uuid;

    public SimpleVaccine() {
    }

    public SimpleVaccine(Vaccine vaccine){
        if (vaccine != null) {
            this.id = vaccine.getId();
            this.name = vaccine.getName();
            this.indication_name = vaccine.getIndication_name();
            this.dose = vaccine.getDose();
            this.dose_number = vaccine.getDose_number();
            this.dosing_unit = vaccine.getDosing_unit();
            this.route = vaccine.getRoute();
            this.scheduled = vaccine.getScheduled();
            this.numeric_indication = vaccine.getNumeric_indication();
            this.body_site_administered = vaccine.getBody_site_administered();
            if (vaccine.getSide_administered_left() != null)
                this.side_administered_left = vaccine.getSide_administered_left();
            this.uuid = vaccine.getUuid();
			/*log.warn("OLD UUID: " + vaccine.getUuid());
			log.warn("NEW UUID: " + this.getUuid());*/
        }else{

        }

    }

    public String getBody_site_administered() {
        return body_site_administered;
    }

    public void setBody_site_administered(String body_site_administered) {
        this.body_site_administered = body_site_administered;
    }

    public boolean getSide_administered_left() {
        return side_administered_left;
    }

    public void setSide_administered_left(boolean side_administered_left) {
        this.side_administered_left = side_administered_left;
    }

    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getNumeric_indication() {
		return numeric_indication;
	}

	public void setNumeric_indication(Integer numeric_indication) {
		this.numeric_indication = numeric_indication;
	}

	public boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndication_name() {
		return indication_name;
	}

	public void setIndication_name(String indication_name) {
		this.indication_name = indication_name;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Integer getDose_number() {
		return dose_number;
	}

	public void setDose_number(Integer dose_number) {
		this.dose_number = dose_number;
	}

	public String getDosing_unit() {
		return dosing_unit;
	}

	public void setDosing_unit(String dosing_unit) {
		this.dosing_unit = dosing_unit;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	

	public Integer getId() {
		return id;
	}
	

	public void setId(Integer id) {
		this.id = id;
	}
}