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
import org.openmrs.User;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class AdverseReaction extends BaseOpenmrsObject implements Serializable {

	//private static final long serialVersionUID = 1L;

    public AdverseReaction() {
    }

    public AdverseReaction(Integer id, Date date, String adverse_event, String grade, int vaccination_id, User creator, Date dateCreated, User changedBy, Date dateChanged, Boolean retired, Date dateRetired, User retiredBy, String retireReason) {
        this.id = id;
        this.date = date;
        this.adverse_event = adverse_event;
        this.grade = grade;
        this.vaccination_id = vaccination_id;
        this.creator = creator;
        this.dateCreated = dateCreated;
        this.changedBy = changedBy;
        this.dateChanged = dateChanged;
        this.retired = retired;
        this.dateRetired = dateRetired;
        this.retiredBy = retiredBy;
        this.retireReason = retireReason;
    }

    private Integer id;
    private Date date;
    private String adverse_event;
    private String grade;
    private int vaccination_id;


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public Date getDateRetired() {
        return dateRetired;
    }

    public void setDateRetired(Date dateRetired) {
        this.dateRetired = dateRetired;
    }

    public User getRetiredBy() {
        return retiredBy;
    }

    public void setRetiredBy(User retiredBy) {
        this.retiredBy = retiredBy;
    }

    public String getRetireReason() {
        return retireReason;
    }

    public void setRetireReason(String retireReason) {
        this.retireReason = retireReason;
    }

    private User creator;
    private Date dateCreated;
    private User changedBy;
    private Date dateChanged;
    private Boolean retired;
    private Date dateRetired;
    private User retiredBy;
    private String retireReason;

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