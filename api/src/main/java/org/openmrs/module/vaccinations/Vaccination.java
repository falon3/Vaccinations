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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.context.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.module.vaccinations.api.UtilsService;
import org.openmrs.module.vaccinations.util.Constants;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class Vaccination extends BaseOpenmrsObject implements Serializable {

	//private static final long serialVersionUID = 1L;
	protected final Log log = LogFactory.getLog(this.getClass());

	private Integer id;
    private Date scheduled_date;

    public Date[] getCalculatedDateRange() {
        return calculatedDateRange;
    }

    public void setCalculatedDateRange(Date[] calculatedDateRange) {
        this.calculatedDateRange = calculatedDateRange;
    }

    private Date [] calculatedDateRange;
	private String name;
	private String indication_name;
	private Double dose;
	private String dosing_unit;
	private String route;
    private boolean scheduled;

    private Vaccine vaccine;
    private AdverseReaction adverse_reaction;

    private List<AuditLog> auditLogList;

	private boolean administered;
	private Date administration_date;
	private String body_site_administered;
    private boolean side_administered_left;
    private Integer dose_number;
	private String lot_number;
	private String manufacturer;
	private Date manufacture_date;
	private Date expiry_date;
	private boolean adverse_reaction_observed;
    private boolean overdue;

    private Location clinic_location;

	private User creator;
	private Date dateCreated;
	private User changedBy;
	private Date dateChanged;
	private boolean retired;
	private Date dateRetired;
	private User retiredBy;
	private String retireReason;

	private int patient_id;

    public Vaccination() {
    }

    //Converts new SimpleVaccination into a new Vaccination
    //UUID is automatically generated
    public Vaccination(SimpleVaccination simpleVaccination){
        if (simpleVaccination != null) {

            Location userLocus = Context.getLocationService().getLocation(Integer.parseInt(Context.getAuthenticatedUser().getUserProperty(Constants.LOCATIONPROPERTY)));

            this.id = simpleVaccination.getId();
            this.calculatedDateRange = simpleVaccination.getCalculatedDateRange();
            this.scheduled_date = simpleVaccination.getScheduled_date();
            this.name = simpleVaccination.getName();
            this.indication_name = simpleVaccination.getIndication_name();
            this.dose = simpleVaccination.getDose();
            this.dosing_unit = simpleVaccination.getDosing_unit();
            this.route = simpleVaccination.getRoute();
            this.scheduled = simpleVaccination.getScheduled();
            this.vaccine = new Vaccine(simpleVaccination.getSimpleVaccine());
            if (simpleVaccination.getAdverse_reaction_observed())
                this.adverse_reaction = new AdverseReaction(simpleVaccination.getSimpleAdverse_reaction());
            this.administered = simpleVaccination.getAdministered();
            this.administration_date = simpleVaccination.getAdministration_date();
            this.body_site_administered = simpleVaccination.getBody_site_administered();
            this.side_administered_left = simpleVaccination.getSide_administered_left();
            this.dose_number = simpleVaccination.getDose_number();
            this.lot_number = simpleVaccination.getLot_number();
            this.manufacturer = simpleVaccination.getManufacturer();
            this.manufacture_date = simpleVaccination.getManufacture_date();
            this.expiry_date = simpleVaccination.getExpiry_date();
            this.adverse_reaction_observed = simpleVaccination.getAdverse_reaction_observed();
            this.overdue = simpleVaccination.getOverdue();

            this.patient_id = simpleVaccination.getPatient_id();

            if (simpleVaccination.getId() != null) {
                this.auditLogList = Context.getService(UtilsService.class).getAuditLogByVaccinationId(simpleVaccination.getId());
            }

            this.setUuid(simpleVaccination.getUuid());
            this.creator = Context.getAuthenticatedUser();
            this.clinic_location = userLocus;
            this.dateCreated = new Date();
        }
    }

    public void setVaccination(Vaccination vaccination){
        if (vaccination != null) {


            this.id = vaccination.getId();
            this.scheduled_date = vaccination.getScheduled_date();
            this.name = vaccination.getName();
            this.indication_name = vaccination.getIndication_name();
            this.dose = vaccination.getDose();
            this.dosing_unit = vaccination.getDosing_unit();
            this.route = vaccination.getRoute();
            this.scheduled = vaccination.getScheduled();
            this.scheduled_date = vaccination.getScheduled_date();
            this.vaccine = vaccination.getVaccine();
            if (vaccination.getAdverse_reaction_observed())
                this.adverse_reaction = vaccination.getAdverse_reaction();
            this.administered = vaccination.getAdministered();
            this.administration_date = vaccination.getAdministration_date();
            this.body_site_administered = vaccination.getBody_site_administered();
            this.side_administered_left = vaccination.getSide_administered_left();
            this.dose_number = vaccination.getDose_number();
            this.lot_number = vaccination.getLot_number();
            this.manufacturer = vaccination.getManufacturer();
            this.manufacture_date = vaccination.getManufacture_date();
            this.expiry_date = vaccination.getExpiry_date();
            this.adverse_reaction_observed = vaccination.getAdverse_reaction_observed();
            this.overdue = vaccination.getOverdue();

            this.patient_id = vaccination.getPatient_id();

            this.auditLogList = Context.getService(UtilsService.class).getAuditLogByVaccinationId(vaccination.getId());

            this.clinic_location = vaccination.getClinic_location();

            this.setUuid(vaccination.getUuid());
            this.creator = vaccination.getCreator();
            this.dateCreated = vaccination.getDateCreated();
        }
    }

    public void updateFromVaccine(Vaccine vaccine){
        if (vaccine.getName() != null)
            name = vaccine.getName();
        if (vaccine.getIndication_name() != null)
            indication_name = vaccine.getIndication_name();
        if (vaccine.getDose() != null)
            dose = vaccine.getDose();
        if (vaccine.getDose_number() != null)
            dose_number = vaccine.getDose_number();
        if (vaccine.getDosing_unit() != null)
            dosing_unit = vaccine.getDosing_unit();
        if (vaccine.getRoute() != null)
            route = vaccine.getRoute();
        scheduled = vaccine.getScheduled();
        if (vaccine.getBody_site_administered() != null)
            body_site_administered = vaccine.getBody_site_administered();
        if (vaccine.getSide_administered_left() != null)
            side_administered_left = vaccine.getSide_administered_left();
    }

    public void updateFromOwnVaccine(){
        updateFromVaccine(this.vaccine);
    }

    //Caching cannot be implemented
    public List<AuditLog> getAuditLogList() {
        if (id != null) {
            if (Context.getService(UtilsService.class).getAuditLogByVaccinationId(id) != null) {
                auditLogList = Context.getService(UtilsService.class).getAuditLogByVaccinationId(id);
                return auditLogList;
            } else {
                return new ArrayList<AuditLog>();
            }
        }else {
                return new ArrayList<AuditLog>();
        }
    }

    public boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public void setAuditLogList(List<AuditLog> auditLogList) {
        this.auditLogList = auditLogList;
    }

    public boolean getSide_administered_left() {
        return side_administered_left;
    }

    public void setSide_administered_left(boolean side_administered_left) {
        this.side_administered_left = side_administered_left;
    }

    public Location getClinic_location() {
        return clinic_location;
    }

    public void setClinic_location(Location clinic_location) {
        this.clinic_location = clinic_location;
    }

    public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

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

    public boolean getRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
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

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public AdverseReaction getAdverse_reaction() {
		return adverse_reaction;
	}

	public void setAdverse_reaction(AdverseReaction adverse_reaction) {
		this.adverse_reaction = adverse_reaction;
	}

	public Date getScheduled_date() {
		return scheduled_date;
	}

	public void setScheduled_date(Date scheduled_date) {
		this.scheduled_date = scheduled_date;
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

	public boolean getAdministered() {
		return administered;
	}

	public void setAdministered(boolean administered) {
		this.administered = administered;
	}

	public Date getAdministration_date() {
		return administration_date;
	}

	public void setAdministration_date(Date administration_date) {
		this.administration_date = administration_date;
	}

	public String getBody_site_administered() {
		return body_site_administered;
	}

	public void setBody_site_administered(String body_site_administered) {
		this.body_site_administered = body_site_administered;
	}

	public Integer getDose_number() {
		return dose_number;
	}

	public void setDose_number(Integer dose_number) {
		this.dose_number = dose_number;
	}

	public String getLot_number() {
		return lot_number;
	}

	public void setLot_number(String lot_number) {
		this.lot_number = lot_number;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getManufacture_date() {
		return manufacture_date;
	}

	public void setManufacture_date(Date manufacture_date) {
		this.manufacture_date = manufacture_date;
	}

	public Date getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}

	public boolean getAdverse_reaction_observed() {
		return adverse_reaction_observed;
	}

	public void setAdverse_reaction_observed(boolean adverse_reaction_observed) {
		this.adverse_reaction_observed = adverse_reaction_observed;
	}

	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}