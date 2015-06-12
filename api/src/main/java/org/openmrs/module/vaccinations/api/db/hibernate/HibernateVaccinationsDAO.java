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
package org.openmrs.module.vaccinations.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.vaccinations.Vaccination;
import org.openmrs.module.vaccinations.api.db.VaccinationsDAO;

import java.util.List;

/**
 * It is a default implementation of  {@link VaccinationsDAO}.
 */
public class HibernateVaccinationsDAO implements VaccinationsDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;

	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

	@Override
	public List<Vaccination> listVaccinationsByPatientId(int patientId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Vaccination.class);
		crit.add(Restrictions.eq("retired", false));
		crit.add(Restrictions.eq("patient_id", patientId));

		List<Vaccination> vaccinationList = (List<Vaccination>)crit.list();
		if (vaccinationList.isEmpty())
			return null;

		return vaccinationList;
	}

	@Override
	public Vaccination saveOrUpdateVaccination(Vaccination vaccination) throws DAOException {
        Vaccination vaccination1 = (Vaccination)sessionFactory.getCurrentSession().merge(vaccination);
        sessionFactory.getCurrentSession().saveOrUpdate(vaccination1);
        sessionFactory.getCurrentSession().flush();
        //log.warn("MERGED VACCINATION RETURNING: " + vaccination1.toString());
		return vaccination1;
	}

	@Override
	public Vaccination getVaccinationByVaccinationId(int vaccinationId) {
		return (Vaccination)sessionFactory.getCurrentSession().get(Vaccination.class, vaccinationId);
	}

	@Override
	public Vaccination getVaccinationByUuId(String uuid) {
		if (uuid == null)
			return null;

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Vaccination.class);
		crit.add(Restrictions.eq("uuid", uuid));

		List<Vaccination> vaccinationList = (List<Vaccination>)crit.list();
		if (vaccinationList.isEmpty())
			return null;

		return vaccinationList.get(0);
	}
}
