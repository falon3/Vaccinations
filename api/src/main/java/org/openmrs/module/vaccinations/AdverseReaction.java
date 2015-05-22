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

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.BaseOpenmrsMetadata;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class AdverseReaction extends BaseOpenmrsObject implements Serializable {

	//private static final long serialVersionUID = 1L;

    private Integer id;
    private Date date;
    private String adverse_event;
    private String grade;
    private int vaccination_id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAdverse_event() {
        return adverse_event;
    }

    public void setAdverse_event(String adverse_event) {
        this.adverse_event = adverse_event;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

    public int getVaccination_id() {
        return vaccination_id;
    }

    public void setVaccination_id(int vaccination_id) {
        this.vaccination_id = vaccination_id;
    }
}