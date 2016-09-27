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

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class SimpleVaccination implements Serializable {

	//private static final long serialVersionUID = 1L;

    private String gName = "";
    private String lName = "";

	public Date[] getCalculatedDateRange() {
		return calculatedDateRange;
	}

	public void setCalculatedDateRange(Date[] calculatedDateRange) {
		this.calculatedDateRange = calculatedDateRange;
	}

	private Date [] calculatedDateRange;

	private Integer id;
    private Date scheduled_date;
	private String name;
	private String indication_name;
	private Double dose;
	private String dosing_unit;
	private String route;
    private boolean scheduled;

    private SimpleVaccine simpleVaccine;
    private SimpleAdverseReaction simpleAdverse_reaction;
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

    private String administered_by;
    private String clinic_location;

    private String excuse;
    private String reason;
    private boolean unadminister;

	private int patient_id;
	private String uuid;

    public SimpleVaccination() {
    }

    public SimpleVaccination(Vaccination vaccination){
        if (!(null == vaccination)) {
            this.id = vaccination.getId();
			this.calculatedDateRange = vaccination.getCalculatedDateRange();
            this.scheduled_date = vaccination.getScheduled_date();
            this.name = vaccination.getName();
            this.indication_name = vaccination.getIndication_name();
            this.dose = vaccination.getDose();
            this.dosing_unit = vaccination.getDosing_unit();
            this.route = vaccination.getRoute();
            this.scheduled = vaccination.getVaccine().getScheduled();
            this.simpleVaccine = new SimpleVaccine(vaccination.getVaccine());
            this.simpleAdverse_reaction = new SimpleAdverseReaction(vaccination.getAdverse_reaction());
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

            if (vaccination.getAuditLogList() != null) {
                this.auditLogList = vaccination.getAuditLogList();
            }else
                this.auditLogList = new ArrayList<AuditLog>();

            for (int i = 0; i <= auditLogList.size() - 1; i++){
                auditLogList.get(i).getAuditLogLineItemList();
            }

            if (vaccination.getCreator() != null) {
                if (vaccination.getCreator().getGivenName() != null)
                    gName = vaccination.getCreator().getGivenName();
                if (vaccination.getCreator().getFamilyName() != null)
                    lName = vaccination.getCreator().getFamilyName();
            }
            this.administered_by = gName + " " + lName;

            if (vaccination.getClinic_location() != null)
                this.clinic_location = vaccination.getClinic_location().getName();
            this.patient_id = vaccination.getPatient_id();
            this.uuid = vaccination.getUuid();
        }
    }

    public void updateFromSimpleVaccine(SimpleVaccine simpleVaccine){
        name = simpleVaccine.getName();
        indication_name = simpleVaccine.getIndication_name();
        dose = simpleVaccine.getDose();
        dose_number = simpleVaccine.getDose_number();
        dosing_unit = simpleVaccine.getDosing_unit();
        route = simpleVaccine.getRoute();
        scheduled = simpleVaccine.getScheduled();
        body_site_administered = simpleVaccine.getBody_site_administered();
        side_administered_left = simpleVaccine.getSide_administered_left();
    }

    public void updateFromOwnSimpleVaccine(){
        updateFromSimpleVaccine(this.simpleVaccine);
    }

    public SimpleVaccine getSimpleVaccine() {
        return simpleVaccine;
    }

    public void setSimpleVaccine(SimpleVaccine simpleVaccine) {
        this.simpleVaccine = simpleVaccine;
    }

    public boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public boolean getUnadminister() {
        return unadminister;
    }

    public void setUnadminister(boolean unadminister) {
        this.unadminister = unadminister;
    }

    public String getExcuse() {
        return excuse;
    }

    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<AuditLog> getAuditLogList() {
        return auditLogList;
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

    public String getAdministered_by() {
        return administered_by;
    }

    public void setAdministered_by(String administered_by) {
        this.administered_by = administered_by;
    }

    public String getClinic_location() {
        return clinic_location;
    }

    public void setClinic_location(String clinic_location) {
        this.clinic_location = clinic_location;
    }

    public SimpleAdverseReaction getSimpleAdverse_reaction() {
		return simpleAdverse_reaction;
	}

	public void setSimpleAdverse_reaction(SimpleAdverseReaction simpleAdverse_reaction) {
		this.simpleAdverse_reaction = simpleAdverse_reaction;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public Date getScheduled_date() {
		return scheduled_date;
	}

	public void setScheduled_date(Date scheduled_date) {
		if (scheduled_date == null){
			this.scheduled_date = new Date();
		}
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
