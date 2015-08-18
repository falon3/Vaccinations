package org.openmrs.module.vaccinations.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.vaccinations.*;
import org.openmrs.module.vaccinations.api.db.UtilsDAO;
import org.openmrs.module.vaccinations.api.UtilsService;
import org.openmrs.module.vaccinations.util.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Serghei Luchianov on 14/Jul/2015.
 */
public class UtilsServiceImpl extends BaseOpenmrsService implements UtilsService {

    protected final Log log = LogFactory.getLog(this.getClass());
    private SessionFactory sessionFactory;
    private UtilsDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(UtilsDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public UtilsDAO getDao() {
        return dao;
    }

    @Override
    public List<Manufacturer> getAllManufacturers(Boolean includeRetired) throws APIException {
        return dao.getAllManufacturers(includeRetired);
    }

    @Override
    public List<AuditLog> getAuditLogByVaccinationId(int vaccinationId) throws APIException {
        return dao.getAuditLogByVaccinationId(vaccinationId);
    }

    @Override
    public List<AuditLogLineItem> getAuditLogLineItems(int auditLogId) throws APIException {
        return dao.getAuditLogLineItems(auditLogId);
    }

    @Override
    public AuditLog saveOrUpdateAuditLog(AuditLog auditLog) throws APIException {
        return dao.saveOrUpdateAuditLog(auditLog);
    }

    @Override
    public AuditLogLineItem saveOrUpdateAuditLogLineItem(AuditLogLineItem auditLogLineItem) throws APIException {
        return dao.saveOrUpdateAuditLogLineItem(auditLogLineItem);
    }

    /*
        This method should always be run after the changes have been saved. That way there will always be a proper id for the newly created Vaccination
     */
    @Override
    public void createAuditLogRecord(Vaccination oldVac, Vaccination newVac, String excuse, String reason) throws APIException {
        createAuditLogRecord(oldVac, newVac, excuse, reason, false);
    }

    /*
       This method should always be run after the changes have been saved. That way there will always be a proper id for the newly created Vaccination
    */
    @Override
    public void createAuditLogRecord(Vaccination oldVac, Vaccination newVac, String excuse, String reason, boolean unadminister) throws APIException {

        log.info("Inside createAuditLogRecord method");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        AuditLog auditLog1 = new AuditLog();
        Location userLocus = Context.getLocationService().getLocation(Integer.parseInt(Context.getAuthenticatedUser().getUserProperty(Constants.LOCATIONPROPERTY)));
        String userName = Context.getAuthenticatedUser().getGivenName() + " " + Context.getAuthenticatedUser().getFamilyName();

        auditLog1.setVaccination_id(newVac.getId());
        auditLog1.setLocation(userLocus.toString());
        auditLog1.setChanged_by(userName);
        auditLog1.setExcuse(excuse);
        auditLog1.setReason(reason);

        AuditLog auditLog = saveOrUpdateAuditLog(auditLog1);




        if (unadminister == true){
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Unadministered");
            auditLogLineItem.setOriginal_value(((Boolean)false).toString());
            auditLogLineItem.setNew_value(((Boolean)unadminister).toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        c1.setTime(oldVac.getScheduled_date());
        c2.setTime(newVac.getScheduled_date());
        if (!c1.equals(c2)) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Scheduled Date");
            auditLogLineItem.setOriginal_value(c1.getTime().toString());
            auditLogLineItem.setNew_value(c2.getTime().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getName().equals(newVac.getName())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Name");
            auditLogLineItem.setOriginal_value(oldVac.getName().toString());
            auditLogLineItem.setNew_value(newVac.getName().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (oldVac.getIndication_name() != null && newVac.getIndication_name() != null &&
                !oldVac.getIndication_name().equals(newVac.getIndication_name())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Indication Name");
            auditLogLineItem.setOriginal_value(oldVac.getIndication_name().toString());
            auditLogLineItem.setNew_value(newVac.getIndication_name().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getDose().equals(newVac.getDose())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Dose");
            auditLogLineItem.setOriginal_value(oldVac.getDose().toString());
            auditLogLineItem.setNew_value(newVac.getDose().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getDosing_unit().toString().equals(newVac.getDosing_unit())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Dosing Unit");
            auditLogLineItem.setOriginal_value(oldVac.getDosing_unit().toString());
            auditLogLineItem.setNew_value(newVac.getDosing_unit().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (oldVac.getRoute() != null && newVac.getRoute() != null &&
                !oldVac.getRoute().equals(newVac.getRoute())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Route");
            auditLogLineItem.setOriginal_value(oldVac.getRoute().toString());
            auditLogLineItem.setNew_value(newVac.getRoute().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getScheduled() == newVac.getScheduled()) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Scheduled");
            auditLogLineItem.setOriginal_value(((Boolean)oldVac.getScheduled()).toString());
            auditLogLineItem.setNew_value(((Boolean)newVac.getScheduled()).toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getAdministered() == newVac.getAdministered()){
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Administered");
            auditLogLineItem.setOriginal_value(((Boolean)oldVac.getAdministered()).toString());
            auditLogLineItem.setNew_value(((Boolean)oldVac.getAdministered()).toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }


        c1.setTime(oldVac.getAdministration_date());
        c2.setTime(newVac.getAdministration_date());
        if (!c1.equals(c2)) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Administration Date");
            auditLogLineItem.setOriginal_value(c1.getTime().toString());
            auditLogLineItem.setNew_value(c2.getTime().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getBody_site_administered().equals(newVac.getBody_site_administered())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Body Site Administered");
            auditLogLineItem.setOriginal_value(oldVac.getBody_site_administered().toString());
            auditLogLineItem.setNew_value(newVac.getBody_site_administered().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getSide_administered_left() == newVac.getSide_administered_left()){
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Left Side Administered");
            auditLogLineItem.setOriginal_value(((Boolean)oldVac.getSide_administered_left()).toString());
            auditLogLineItem.setNew_value(((Boolean)oldVac.getSide_administered_left()).toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getDose_number().equals(newVac.getDose_number())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Dose Number");
            auditLogLineItem.setOriginal_value(oldVac.getDose_number().toString());
            auditLogLineItem.setNew_value(newVac.getDose_number().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getLot_number().equals(newVac.getLot_number())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Lot Number");
            auditLogLineItem.setOriginal_value(oldVac.getLot_number().toString());
            auditLogLineItem.setNew_value(newVac.getLot_number().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getManufacturer().equals(newVac.getManufacturer())) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Manufacturer");
            auditLogLineItem.setOriginal_value(oldVac.getManufacturer().toString());
            auditLogLineItem.setNew_value(newVac.getManufacturer().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        c1.setTime(oldVac.getManufacture_date());
        c2.setTime(newVac.getManufacture_date());
        if (!c1.equals(c2)) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Manufacture Date");
            auditLogLineItem.setOriginal_value(c1.getTime().toString());
            auditLogLineItem.setNew_value(c2.getTime().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        c1.setTime(oldVac.getExpiry_date());
        c2.setTime(newVac.getExpiry_date());
        if (!c1.equals(c2)) {
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Expiry Date");
            auditLogLineItem.setOriginal_value(c1.getTime().toString());
            auditLogLineItem.setNew_value(c2.getTime().toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        if (!oldVac.getAdverse_reaction_observed() == newVac.getAdverse_reaction_observed()){
            AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
            auditLogLineItem.setField("Adverse Reaction Observed");
            auditLogLineItem.setOriginal_value(((Boolean)oldVac.getAdverse_reaction_observed()).toString());
            auditLogLineItem.setNew_value(((Boolean)oldVac.getAdverse_reaction_observed()).toString());

            auditLogLineItem.setAudit_log_id(auditLog.getId());
            saveOrUpdateAuditLogLineItem(auditLogLineItem);
        }

        log.info("Comparing Adverse Reactions");
        if (oldVac.getAdverse_reaction() != null && newVac.getAdverse_reaction() != null) {

            c1.setTime(oldVac.getAdverse_reaction().getDate());
            c2.setTime(newVac.getAdverse_reaction().getDate());
            if (!c1.equals(c2)) {
                AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
                auditLogLineItem.setField("Adverse Reaction Date");
                auditLogLineItem.setOriginal_value(c1.getTime().toString());
                auditLogLineItem.setNew_value(c2.getTime().toString());

                auditLogLineItem.setAudit_log_id(auditLog.getId());
                saveOrUpdateAuditLogLineItem(auditLogLineItem);
            }

            if (!oldVac.getAdverse_reaction().getAdverse_event().equals(newVac.getAdverse_reaction().getAdverse_event())) {
                AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
                auditLogLineItem.setField("Adverse Event");
                auditLogLineItem.setOriginal_value(oldVac.getAdverse_reaction().getAdverse_event().toString());
                auditLogLineItem.setNew_value(newVac.getAdverse_reaction().getAdverse_event().toString());

                auditLogLineItem.setAudit_log_id(auditLog.getId());
                saveOrUpdateAuditLogLineItem(auditLogLineItem);
            }

            if (!oldVac.getAdverse_reaction().getGrade().equals(newVac.getAdverse_reaction().getGrade())) {
                AuditLogLineItem auditLogLineItem = new AuditLogLineItem();
                auditLogLineItem.setField("Adverse Reaction Grade");
                auditLogLineItem.setOriginal_value(oldVac.getAdverse_reaction().getGrade().toString());
                auditLogLineItem.setNew_value(newVac.getAdverse_reaction().getGrade().toString());

                auditLogLineItem.setAudit_log_id(auditLog.getId());
                saveOrUpdateAuditLogLineItem(auditLogLineItem);
            }

        }
    }
}
