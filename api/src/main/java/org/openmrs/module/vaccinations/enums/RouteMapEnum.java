package org.openmrs.module.vaccinations.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openmrs.module.vaccinations.util.Serializers.RouteMapEnumSerializer;


/**
 * Created by Serghei Luchianov on 2015-06-25.
 */
@JsonSerialize(using = RouteMapEnumSerializer.class)
public enum RouteMapEnum {
    Intramuscular (Routes.Intramuscular.getConceptId(), new String[] {BodySites.AnteriorForearm.getName(), BodySites.PosteriorForearm.getName(), BodySites.UpperArmTricep.getName(), BodySites.UpperArmDeltoid.getName(), BodySites.Thigh.getName(), BodySites.LowerLeg.getName(), BodySites.Buttock.getName()}),
    Intradermal (Routes.Intradermal.getConceptId(), new String[] {BodySites.AnteriorForearm.getName(), BodySites.PosteriorForearm.getName(), BodySites.UpperArmTricep.getName(), BodySites.UpperArmDeltoid.getName(), BodySites.Thigh.getName(), BodySites.LowerLeg.getName(), BodySites.Buttock.getName()}),
    Subcutaneous (Routes.Subcutaneous.getConceptId(), new String[] {BodySites.AnteriorForearm.getName(), BodySites.PosteriorForearm.getName(), BodySites.UpperArmTricep.getName(), BodySites.UpperArmDeltoid.getName(), BodySites.Thigh.getName(), BodySites.LowerLeg.getName(), BodySites.Buttock.getName(), BodySites.Abdomen.getName()}),
    Transdermal (Routes.Transdermal.getConceptId(), new String[] {BodySites.AnteriorForearm.getName(), BodySites.PosteriorForearm.getName(), BodySites.UpperArmTricep.getName(), BodySites.UpperArmDeltoid.getName(), BodySites.Thigh.getName(), BodySites.LowerLeg.getName(), BodySites.Buttock.getName(), BodySites.Abdomen.getName(), BodySites.Chest.getName(), BodySites.UpperBack.getName(), BodySites.LowerBack.getName()}),
    Oral (Routes.Oral.getConceptId(), new String[] {BodySites.Mouth.getName()}),
    Intranasal (Routes.Intranasal.getConceptId(), new String[] {BodySites.Nostril.getName()});

    private final String conceptId;
    private final String[] sites;

    private RouteMapEnum(String conceptId, String[] sites){
        this.conceptId = conceptId;
        this.sites = sites;
    }

    public String getConceptId() {
        return conceptId;
    }

    public String[] getSites() {
        return sites;
    }
}
