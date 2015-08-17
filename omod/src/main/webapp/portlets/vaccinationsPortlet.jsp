<%@ include file="/WEB-INF/template/include.jsp"%>
<!doctype html>
<html class="no-js">
  <head>
    <meta charset="utf-8">
    <title>vaccinations</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/vaccinations/styles/vendor-66424c82.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/vaccinations/styles/app-2f8e93b1.css">
  </head>
  <body>

<div id="appcontainer" class="appcontainer" ng-app="vaccinations">
<!-- <div id="appcontainer"> -->

    <div class="container" ng-controller="MainController">

        <loader></loader>
        <!-- ADD SECTION -->
        <div class="add-vaccination-wrapper">
            <!--LEGEND-->
            <div class="legend-wrapper">
                <div><i class="demo-icon icon-circle-empty unadmin-x"></i><span>Unadministered Vaccination</span></div>
                <div><i class="demo-icon icon-ok-circled2 admin-check"></i><span>Administered Vaccination</span></div>
                <div><i class="demo-icon icon-cancel-circled2 admin-check-reaction"></i><span>Adverse Reaction</span></div>
            </div>
            <!--LEGEND-->

            <h5 class="add-vaccination-header">Add New Vaccination</h5>

            <div class="add-vaccination-select-wrapper">
                <select class="form-control add-vaccination" ng-model="newVaccine" ng-options="vaccines as formatVaccine(vaccines) for vaccines in vaccines"></select>
            </div>

            <div class="form-button-wrapper new-vaccine-wrapper">
                <button ng-if="newVaccine"  class="btn btn-info" ng-click="stageVaccination(newVaccine, false)"><strong>Administer a new vacciantion</strong></button>

                <button ng-if="newVaccine" class="btn btn-primary" ng-click="stageVaccination(newVaccine, true)"><strong>Book a new vaccination</strong></button>
            </div>



            <div class="staged-wrapper" ng-if="stagedVaccinations.length > 0">

                <!-- STAGED VACCINATIONS -->
                <vaccination ng-if="stagedVaccinations" get-vaccination="stagedVaccinations[0]" get-routes="dropDownData.routes" get-dosing-units="dropDownData.dosingUnits" get-body-sites="dropDownData.bodySites" get-manufacturers="dropDownData.manufacturers"></vaccination>
                <!-- /STAGED VACCINATION -->

            </div>

        </div>
        <!-- /SEARCH ADD SECTION -->

        <!-- VACCINATIONS -->
        <div class="vaccinations-wrapper">

            <!-- SCHEDULED VACCINATIONS -->
            <div ng-repeat="(name, vaccinationsGroup) in vaccinations | filter: {scheduled: 'true'} | groupBy: 'name'">
                <div ng-if="$index === 0" class="scheduled-header"><span class="label label-default section-label">Scheduled</span></div>
                <div class="vaccination-group-header"><span class="label label-default header-label" ng-bind="::name">Loading...</span></div>
                <vaccination get-vaccination="vaccination" get-routes="dropDownData.routes" get-dosing-units="dropDownData.dosingUnits" get-body-sites="dropDownData.bodySites" get-manufacturers="dropDownData.manufacturers" get-change-reasons="dropDownData.changeReasons" ng-repeat="vaccination in vaccinationsGroup | orderBy: ['dose_number']"></vaccination>
            </div>

            <!-- UNSCHEDULED VACCINATIONS -->
            <div ng-repeat="(name, vaccinationsGroup) in vaccinations | filter: {scheduled: 'false'} | groupBy: 'name'">
                <!-- Show the unscheduled header only if unscheduled vaccs present -->
                <div ng-if="$index === 0" class="not-scheduled-header"><span class="label label-default section-label">Unscheduled</span></div>
                <div class="vaccination-group-header" ><span class="label label-default header-label" ng-bind="::name">Loading...</span></div>
                <vaccination get-vaccination="vaccination" get-routes="dropDownData.routes" get-dosing-units="dropDownData.dosingUnits" get-body-sites="dropDownData.bodySites" get-manufacturers="dropDownData.manufacturers" get-change-reasons="dropDownData.changeReasons" ng-repeat="vaccination in vaccinationsGroup | orderBy: ['scheduled_date']"></vaccination>
            </div>

        </div>
        <!-- /VACCINATIONS -->

    </div>
     <!-- TEMPLATES -->

    <!-- LOADER -->
    <script type="text/ng-template" id='/app/loader/loader.template.html'>
        <div ng-if="state.loading || state.success" class="loader">
            <div ng-if="state.loading" class="spinner">
                <div class="spinner-container container1">
                    <div class="circle1"></div>
                    <div class="circle2"></div>
                    <div class="circle3"></div>
                    <div class="circle4"></div>
                </div>
                <div class="spinner-container container2">
                    <div class="circle1"></div>
                    <div class="circle2"></div>
                    <div class="circle3"></div>
                    <div class="circle4"></div>
                </div>
                <div class="spinner-container container3">
                    <div class="circle1"></div>
                    <div class="circle2"></div>
                    <div class="circle3"></div>
                    <div class="circle4"></div>
                </div>
            </div>
            <div ng-if="state.success" class="success-check">
                <i class="demo-icon icon-ok-circled2 big-check"></i>
            </div>
        </div>
    </script>
    <!-- /LOADER -->

    <!-- FEEDBACK -->
    <script type="text/ng-template" id="/app/feedback/feedback.template.html">
        <div ng-show="warn()" class="form-warning">
            <div class="alert alert-warning"><strong>{{ warning }}</strong></div>
        </div>
    </script>
    <!-- /FEEDBACK -->

    <!-- ADMINISTERED VACCINATION -->
    <script type="text/ng-template" id="/app/vaccination/administered/vaccination_administered.template.html">
        <!-- ADMINISTERED TEMPLATE -->
        <div ng-controller="AdminVaccinationController" class="vaccination administered">

            <!-- ADMINISTERED HEADER -->
            <div class="header administered-header" ng-class="{'adverse-header': enteredEditFormData.adverse_reaction_observed}">

                <i ng-if="!enteredEditFormData.adverse_reaction_observed" class="demo-icon icon-ok-circled2 admin-check"></i>

                <i ng-if="enteredEditFormData.adverse_reaction_observed" class="demo-icon icon-cancel-circled2 admin-check-reaction"></i>

                <span ng-if="enteredEditFormData.adverse_reaction_observed" class="label label-danger adverse-label">Adverse Reaction</span>

                <span ng-if="enteredEditFormData.dose_number">
                    <span class="dose-number-label">Course Number: </span>
                    <span class="dose-number">{{ ::enteredEditFormData.dose_number }}</span>
                </span>

                <span ng-if="enteredEditFormData.indication_name" class="header-info">
                    <span class="indication-label">Indication: </span>
                    <span class="indication-date">{{ ::enteredEditFormData.indication_name | date: 'mediumDate' }}</span>
                </span>

                <span class="administered-label">Administered: </span><span class="administered-date">{{ ::enteredEditFormData.administration_date | date: 'mediumDate' }}</span>

                <div class="btn-group pull-right">
                    <button type="button" class="btn btn-primary" ng-class="{'active': state.editFormOpen}" ng-click="toggleEditForm()">Details</button>

                    <button type="button" class="btn btn-danger" ng-class="{'active': state.adverseFormOpen}" ng-click="toggleReactionForm()" ng-if="!enteredEditFormData.adverse_reaction_observed">Add Reaction</button>

                    <button class="btn btn-danger" ng-class="{ 'active': state.adverseFormOpen }" ng-click="toggleReactionForm()" ng-if="enteredEditFormData.adverse_reaction_observed">Reaction Details</button>

                    <button type="button" class="btn btn-history" ng-class="{'active': state.auditLogOpen}" ng-click="toggleAuditLog()">History</button>
                </div>
            </div>
            <!-- /HEADER -->

            <!-- AUDIT LOG -->
            <div ng-if="state.auditLogOpen" class="form-wrapper">
                <div ng-repeat="change in enteredEditFormData.audit_log">
                    <span>Change by</span><span>change.user</span>
                    <span>Date</span><span>change.date</span>
                    <span>Field</span><span>change.field</span>
                    <span>From</span><span>change.old_value</span>
                    <span>To</span><span>change.new_value</span>
                </div>
            </div>
            <!-- /AUDIT LOG -->

            <!-- EDIT FORM -->
            <div ng-if="state.editFormOpen" class="form-wrapper">
            <form  name="form" novalidate>

                    <div class="form-group">
                        <label>Administered By</label>
                        <input disabled="disabled" class="form-control" type="text" ng-model="enteredEditFormData.administered_by" placeholder="Administered By">
                    </div>

                    <div class="form-group">
                        <label>Clinic Location</label>
                        <input disabled="disabled" class="form-control" type="text" ng-model="enteredEditFormData.clinic_location" placeholder="Clinic Location">
                    </div>

                    <div class="form-group">
                        <label>Adminstration Date</label>
                        <input ng-disabled="!isUnadministerable()" administration_date" class="form-control" type="date" ng-model="enteredEditFormData.administration_date" placeholder="Date" required>
                    </div>
                    <feedback warn="form.administration_date.$error.date" warning="Enter a valid administration date."></feedback>

                    <div class="form-group" ng-if="enteredEditFormData.dose_number">
                        <label>Course Number</label>
                        <input disabled="disabled" class="form-control" type="text" ng-model="enteredEditFormData.dose_number" placeholder="Dose In Course">
                    </div>

                    <div class="form-group">
                        <label>Dose</label>
                        <input ng-disabled="!isUnadministerable()" name="dose" class="form-control" type="number" ng-model="enteredEditFormData.dose" placeholder="Dose"required>
                    </div>
                    <feedback warn="form.dose.$error.number || form.dose.$error.required" warning="Enter a valid dose size. For ex .5, 2..."></feedback>

                  <div class="form-group">
                    <label>Units</label>
                    <select ng-disabled="!isUnadministerable()" name="dosing_unit" class="form-control" ng-model="enteredEditFormData.dosing_unit" ng-options="u.conceptId as u.name for u in getDosingUnits()" required>
                      <option value=""></option>
                    </select>
                  </div>
                  <feedback warn="form.dosing_unit.$error.required" warning="Enter a valid dosing unit. For example ml, drops..."></feedback>

                  <div class="form-group">
                    <label>Route</label>
                    <select ng-disabled="!isUnadministerable()" name="route" class="form-control" ng-model="enteredEditFormData.route" ng-options="r.conceptId as r.name for r in getRoutes()" required>
                      <option value=""></option>
                    </select>
                  </div>
                  <feedback warn="form.route.$error.required" warning="Enter a valid administration route.."></feedback>

                  <div class="form-group">
                    <label>Body Site Administered</label>
                    <select ng-disabled="!isUnadministerable()" name="body_site_administered" class="form-control" ng-model="enteredEditFormData.body_site_administered" ng-options="bs.name as bs.name for bs in getBodySites()" required>
                    </select>

                  </div>
                  <feedback warn="form.body_site_administered.$error.required" warning="Enter a valid body site."></feedback>

                  <div class="form-group">
                    <label>Side Administered</label>
                    <select ng-disabled="!isUnadministerable()" name="side_administered_left" class="form-control" ng-model="enteredEditFormData.side_administered_left" required ng-options="b.value as b.name for b in [{name:'left', value: true}, {name:'right', value: false}]">
                      <option value=""></option>
                      <option value="true" >Left</option>
                      <option value="false" >Right</option>
                    </select>
                  </div>
                  <feedback warn="form.side_administered_left.$error.required" warning="Select a side."></feedback>

                    <div class="form-group">
                        <label>Manufacturer</label>
                        <select ng-disabled="!isUnadministerable()" name="manufacturer" class="form-control" type="text" ng-model="enteredEditFormData.manufacturer" placeholder="Manufacturer" required ng-options="c.name as c.name for c in getManufacturers()">
                        <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="form.manufacturer.$error.required" warning="Enter a valid manufacturer. For example Pfizer, GlaxoSmithKline..."></feedback>

                    <div class="form-group">
                        <label>Lot Number</label>
                        <input ng-disabled="!isUnadministerable()" name="lot_number" class="form-control" type="text" ng-model="enteredEditFormData.lot_number" placeholder="Lot Number" required>
                    </div>
                   <feedback warn="form.lot_number.$error.required" warning="Enter a valid vaccine lot number."></feedback>

                    <div class="form-group">
                        <label>Manufacture Date</label>
                        <input  ng-disabled="!isUnadministerable()" name="manufacture_date" class="form-control" type="date" ng-model="enteredEditFormData.manufacture_date" placeholder="Manufacture Date">
                    </div>
                   <feedback warn="form.manufactur_date.$error.required || form.manufacture_date.$error.date" warning="Enter a valid manufacture date."></feedback>

                    <div class="form-group">
                        <label>Expiry Date</label>
                        <input ng-disabled="!isUnadministerable()" name="expiry_date" class="form-control" type="date" ng-model="enteredEditFormData.expiry_date" placeholder="Expiry Date">
                    </div>
                    <feedback warn="form.expiry_date.$error.required || form.expiry_date.$error.date" warning="Enter a valid expiry date."></feedback>




                    <div class="form-group" ng-if="isUnadministerable()">
                        <label>Reason For Change</label>
                        <select  name="excuse" class="form-control" ng-model="enteredEditFormData.excuse"
                        ng-options="r.name as r.name for r in getChangeReasons()" required>
                            <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="form.excuse.$error.required" warning="Select a valid reason for change"></feedback>


                    <div class="form-group" ng-if="enteredEditFormData.excuse == 'Other'">
                        <label>Description</label>
                        <input  type="text" name="reason" class="form-control" ng-model="enteredEditFormData.reason" required/>
                    </div>
                    <feedback ng-if="enteredEditFormData.reason == 1" warn="form.reason.$error.required" warning="Select a valid reason for change"></feedback>


                    <div class="clearfix form-button-wrapper">
                    <!-- cannot delete scheduled vaccines. -->

                        <button ng-if="isUnadministerable()" type="button" class="btn btn-default" ng-click="resetFormDataToDefaults()">Reset</button>

                        <button type="button" class="btn btn-danger" ng-if="!enteredEditFormData.scheduled && isUnadministerable()" ng-click="deleteVaccination(enteredEditFormData)">Delete</button>

                         <button ng-if="form.$valid && isUnadministerable()" type="submit" class="btn btn-warning" ng-click="unadministerVaccination(enteredEditFormData)">Unadminister</button>

                        <button ng-if="form.$valid && isUnadministerable()" type="submit" class="btn btn-primary" ng-click="updateVaccination(enteredEditFormData)">Update</button>
                    </div>
            </form>
            </div>
            <!-- /EDIT FORM -->

            <!-- ADVERSE REACTION FORM -->
            <div class="form-wrapper" ng-if="state.adverseFormOpen">
                <form name="form"  novalidate>

                    <div class="form-group">
                        <label>Date</label>
                        <input name="date" class="form-control" type="date" ng-model="enteredAdverseFormData.date" placeholder="Date" required>
                    </div>
                    <feedback warn="form.date.$error.date" warning="Enter a valid reaction date."></feedback>

                    <div class="form-group">
                        <label>Grade</label>
                        <select name="grade" class="form-control" ng-model="enteredAdverseFormData.grade" required>
                            <option selected="selected" value="160754">Adverse Reaction Grade 1</option>
                            <option value="160755">Adverse Reaction Grade 2</option>
                            <option value="160756">Adverse Reaction Grade 3</option>
                            <option value="160757">Adverse Reaction Grade 4</option>
                            <option value="160758">Adverse Reaction Grade 5</option>
                        </select>
                    </div>
                    <feedback warn="form.grade.$error.required" warning="Select a reaction grade."></feedback>

                    <div class="form-group">
                        <label>Adverse Event Description</label>
                        <textarea name="adverse_event" class="form-control" type="text" rows="4" ng-model="enteredAdverseFormData.adverse_event" placeholder="Description" required></textarea>
                    </div>
                    <feedback warn="form.adverse_event.$error.required" warning="Enter a valid adverse event description."></feedback>

                    <div class="form-button-wrapper">
                    <!-- cannot delete scheduled vaccines. -->
                        <button class="btn btn-danger" ng-if="enteredEditFormData.adverse_reaction_observed" ng-click="removeReaction(enteredAdverseFormData, enteredEditFormData)">Delete</button>

                        <button ng-if="!enteredEditFormData.adverse_reaction_observed && form.$valid" class="btn btn-warning" ng-click="addReaction(enteredAdverseFormData, enteredEditFormData)">Save Adverse Reaction</button>

                        <button ng-if="enteredEditFormData.adverse_reaction_observed" class="btn btn-warning" ng-click="addReaction(enteredAdverseFormData, enteredEditFormData)">Update</button>
                    </div>
                </form>
            </div>
            <!-- /ADVERSE REACTION FORM -->
        </div>
        <!-- ADMINISTERED TEMPLATE -->
    </script>
    <!-- /ADMINISTERED VACCINATION -->

    <!-- UNADMINISTERED VACCINATION -->
    <script type="text/ng-template" id="/app/vaccination/unadministered/vaccination_unadministered.template.html ">

        <!-- UNADIMISTERED TEMPLATE -->
        <div ng-controller="UnAdminVaccinationController" class="vaccination unadministered">

            <!-- UNADMINISTERED HEADER -->
            <div class="header unadministered-header" >

                <i class="demo-icon icon-circle-empty unadmin-x"></i>



                <span class="label label-danger due-label" ng-class="{'hidden': !due}">Due</span>

                <span ng-if="enteredAdminFormData.dose_number" class="header-info">
                    <span class="dose-number-label">Course Number: </span>
                    <span class="dose-number">{{ ::enteredAdminFormData.dose_number }}</span>
                </span>

                <span ng-if="enteredAdminFormData.indication_name" class="header-info">
                    <span class="indication-label">Indication: </span>
                    <span class="indication-date">{{ ::enteredAdminFormData.indication_name | date: 'mediumDate' }}</span>
                </span>

                <span class="header-info">
                    <span class="scheduled-label">To be administered on: </span>
                    <span class="scheduled-date">{{ ::enteredAdminFormData.scheduled_date | date: 'mediumDate' }}</span>
                </span>
                <div class="btn-group pull-right" role="group" aria-label="...">
                    <button type="button" class="btn btn-info" ng-class="{ 'active': state.administerFormOpen }" ng-click="toggleAdministerForm()">Administer</button>
                </div>
            </div>
            <!-- /HEADER -->

            <!-- ADMINISTRATION FORM -->
            <div ng-if="state.administerFormOpen" class="form-wrapper css-form">

                <form name="form" novalidate>

                    <div class="form-group">
                        <label>Administration Date</label>
                        <input name="administration_date" class="form-control" type="date" ng-model="enteredAdminFormData.administration_date" placeholder="Administration Date"required>
                    </div>
                    <feedback warn="form.administration_date.$error.date" warning="Enter a valid administration date."></feedback>

                    <div class="form-group" ng-if="enteredAdminFormData.dose_number">
                        <label>Course Number</label>
                        <input disabled="disabled" class="form-control" type="text" ng-model="enteredAdminFormData.dose_number" placeholder="Dose in Course">
                    </div>

                    <div class="form-group">
                        <label>Dose</label>
                        <input name="dose" class="form-control" type="number" ng-model="enteredAdminFormData.dose" placeholder="Dose" required>
                    </div>
                    <feedback warn="form.dose.$error.required || form.dose.$error.number" warning="Enter a valid dose. Dose must be a number"></feedback>

                    <div class="form-group">
                        <label>Units</label>
                        <select name="dosing_unit" class="form-control" ng-model="enteredAdminFormData.dosing_unit" ng-options="u.conceptId as u.name for u in getDosingUnits()" required>
                          <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="form.dosing_unit.$error.required" warning="Enter a valid dosing unit. For example ml, drops..."></feedback>

                    <div class="form-group">
                        <label>Route</label>
                        <select name="route" class="form-control" ng-model="enteredAdminFormData.route" ng-options="r.conceptId as r.name for r in getRoutes()" required>
                          <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="form.route.$error.required" warning="Enter a valid administration route.."></feedback>

                    <div class="form-group">
                        <label>Body Site Administered</label>
                        <select name="body_site_administered" class="form-control" ng-model="enteredAdminFormData.body_site_administered" ng-options="bs.name as bs.name for bs in getBodySites()" required>
                        </select>

                    </div>
                    <feedback warn="form.body_site_administered.$error.required" warning="Enter a valid body site."></feedback>

                    <div class="form-group">
                    <label>Side Administered</label>
                    <select name="side_administered_left" class="form-control" ng-model="enteredAdminFormData.side_administered_left" required ng-options="b.value as b.name for b in [{name:'Left', value: true}, {name:'Right', value: false}]">
                       <option value=""></option>
                       <option value=true>Left</option>
                       <option value=false>Right</option>
                    </select>
                    </div>
                    <feedback warn="form.side_administered_left.$error.required" warning="Select a side."></feedback>

                    <div class="form-group">
                        <label>Manufacturer</label>
                        <select name="manufacturer" class="form-control" type="text" ng-model="enteredAdminFormData.manufacturer" placeholder="Manufacturer" required ng-options="c.name as c.name for c in getManufacturers()">
                        <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="form.manufacturer.$error.required" warning="Enter a valid manufacturer. For example Pfizer, GlaxoSmithKline..."></feedback>

                    <div class="form-group">
                        <label>Lot Number</label>
                        <input name="lot_number" class="form-control" type="text" ng-model="enteredAdminFormData.lot_number" placeholder="Lot Number" required>
                    </div>
                    <feedback warn="form.lot_number.$error.required" warning="Enter a valid vaccine lot number."></feedback>

                    <div class="form-group">
                        <label>Manufacture Date</label>
                        <input name="manufacture_date" class="form-control" type="date" ng-model="enteredAdminFormData.manufacture_date" placeholder="Manufacture Date" required>
                    </div>
                    <feedback warn="form.manufacture_date.$error.date" warning="Enter a valid manufacture date."></feedback>

                    <div class="form-group">
                        <label>Expiry Date</label>
                        <input name="expiry_date" class="form-control" type="date" ng-model="enteredAdminFormData.expiry_date" placeholder="Expiry Date" required>
                    </div>
                    <feedback warn="form.expiry_date.$error.date" warning="Enter a valid expiry date."></feedback>

                    <div class="form-button-wrapper">
                        <!-- cannot delete scheduled vaccines only modify. -->
                        <button type="button" class="btn btn-danger" ng-if="!enteredAdminFormData.scheduled" ng-click="deleteVaccination(enteredAdminFormData)">Delete</button>
                        <button type="button" class="btn btn-default" ng-click="resetFormDataToDefaults()">Reset</button>
                        <button ng-show="form.$valid" type="submit" class="btn btn-primary" ng-click="submitVaccination(enteredAdminFormData)">Administer</button>
                    </div>
                </form>
            </div>
            <!-- / ADMINISTRATION FORM -->
        </div>
        <!-- /UNADMINISTERED TEMPLATE -->
    </script>
    <!-- /UNADMINISTERED VACCINATION -->

    <!-- STAGED VACCINATION HEADER -->
    <script type="text/ng-template" id="/app/vaccination/staged/staged_header.template.html">
        <!-- STAGED HEADER -->
        <div class="header unadministered-header staged" >
            <h4 class="drug"><span class="label label-default">{{ enteredAdminFormData.name }}</span></h4>
        </div>
        <!-- /STAGED HEADER -->
    </script>
    <!-- /STAGED VACCINATION HEADER -->

    <!-- STAGED VACCINATION -->
    <script type="text/ng-template" id="/app/vaccination/staged/vaccination_staged.template.html">
        <!-- STAGED TEMPLATE -->
        <div ng-controller="StagedVaccinationController" class="vaccination unadministered">

            <!-- STAGED HEADER -->
            <div class="header unadministered-header staged" >
                <h4 class="drug"><span class="label label-default">{{ enteredAdminFormData.name }}</span></h4>
            </div>
            <!-- /STAGED HEADER -->

            <!-- ADMINISTRATION FORM -->
            <form ng-if="state.administerFormOpen" class="form-wrapper show-hide" name="form" novalidate>
                <div>

                    <div ng-if="enteredAdminFormData._administering" class="form-group">
                        <label>Administration Date</label>
                        <input name="administration_date" class="form-control" type="date" ng-model="enteredAdminFormData.administration_date" placeholder="Date" required>
                    </div>
                    <feedback warn="form.administration_date.$error.date" warning="Enter a valid adminstration date."></feedback>

                    <div ng-if="enteredAdminFormData.custom" class="form-group">
                        <label>Vaccine Name</label>
                        <input name="name" class="form-control" type="text" ng-model="enteredAdminFormData.name" placeholder="Vaccine Name" ng-required="enteredAdminFormData.custom">
                    </div>
                    <feedback warn="form.name.$error.required" warning="Enter a vaccine name."></feedback>

                    <div ng-if="enteredAdminFormData._scheduling" class="form-group">
                        <label>Scheduled Date</label>
                        <input name="scheduled_date" class="form-control" type="date" ng-model="enteredAdminFormData.scheduled_date" placeholder="Scheduled Date" required>
                    </div>
                    <feedback warn="form.scheduled_date.$error.date" warning="Enter a valid scheduled date."></feedback>

                    <div class="form-group" ng-if="enteredAdminFormData.dose_number">
                        <label>Course Number</label>
                        <input disabled="disabled" class="form-control" type="text" ng-model="enteredAdminFormData.dose_number" placeholder="Dose In Course">
                    </div>

                    <div class="form-group">
                        <label>Dose</label>
                        <input name="dose" class="form-control" type="number" ng-model="enteredAdminFormData.dose" placeholder="Dose" ng-required="enteredAdminFormData._administering">
                    </div>
                    <feedback warn="(enteredAdminFormData._administering && form.dose.$error.required || form.dose.$error.number) || enteredAdminFormData._scheduling && form.dose.$error.number" warning="Enter a valid dose size. For ex .5, 2..."></feedback>

                  <div class="form-group">
                    <label>Units</label>
                    <select name="dosing_unit" class="form-control" ng-model="enteredAdminFormData.dosing_unit" ng-options="u.conceptId as u.name for u in getDosingUnits()" required>
                      <option value=""></option>
                    </select>
                  </div>
                  <feedback warn="form.dosing_unit.$error.required" warning="Enter a valid dosing unit. For example ml, drops..."></feedback>

                  <div class="form-group">
                    <label>Route</label>
                    <select name="route" class="form-control" ng-model="enteredAdminFormData.route" ng-options="r.conceptId as r.name for r in getRoutes()" required>
                      <option value=""></option>
                    </select>
                  </div>
                  <feedback warn="form.route.$error.required" warning="Enter a valid administration route.."></feedback>

                  <div class="form-group">
                    <label>Body Site Administered</label>
                    <select name="body_site_administered" class="form-control" ng-model="enteredAdminFormData.body_site_administered" ng-options="bs.name as bs.name for bs in getBodySites()" required>
                    </select>

                  </div>
                  <feedback warn="form.body_site_administered.$error.required" warning="Enter a valid body site."></feedback>

                  <div class="form-group">
                    <label>Side Administered</label>
                    <select name="side_administered_left" class="form-control" ng-model="enteredAdminFormData.side_administered_left" required ng-options="b.value as b.name for b in [{name:'Left', value: true}, {name:'Right', value: false}]">
                      <option value=""></option>
                      <option value=true >Left</option>
                      <option value=false >Right</option>
                    </select>
                  </div>
                  <feedback warn="form.side_administered_left.$error.required" warning="Select a side."></feedback>

                     <div ng-if="enteredAdminFormData._administering" class="form-group">
                        <label>Manufacturer</label>
                        <select name="manufacturer" class="form-control" type="text" ng-model="enteredAdminFormData.manufacturer" placeholder="Manufacturer" required="enteredAdminFormData._administering" ng-options="c.name as c.name for c in getManufacturers()">
                        <option value=""></option>
                        </select>
                    </div>
                    <feedback warn="enteredAdminFormData._administering && form.manufacturer.$error.required" warning="Enter a valid manufacturer. For example Pfizer, GlaxoSmithKline..."></feedback>

                    <div ng-if="enteredAdminFormData._administering" class="form-group">
                        <label>Lot Number</label>
                        <input name="lot_number" class="form-control" type="text" ng-model="enteredAdminFormData.lot_number" placeholder="Lot Number" required="enteredAdminFormData._administering">
                    </div>
                    <feedback warn="enteredAdminFormData._administering && form.lot_number.$error.required" warning="Enter a valid vaccine lot number."></feedback>

                    <div ng-if="enteredAdminFormData._administering" class="form-group">
                        <label>Manufacture Date</label>
                        <input name="manufacture_date" class="form-control" type="date" ng-model="enteredAdminFormData.manufacture_date" placeholder="Manufacture Date" required>
                    </div>
                    <feedback warn="enteredAdminFormData._administering && form.manufacture_date.$error.required || form.manufacture_date.$error.date" warning="Enter a valid manufacture date."></feedback>

                    <div ng-if="enteredAdminFormData._administering" class="form-group">
                        <label>Expiry Date</label>
                        <input name="expiry_date" class="form-control" type="date" ng-model="enteredAdminFormData.expiry_date" placeholder="Expiry Date" required>
                    </div>
                    <feedback warn="enteredAdminFormData._administering && form.expiry_date.$error.required || form.expiry_date.$error.date" warning="Enter a valid expiry date."></feedback>

                    <div class="form-button-wrapper">
                    <!-- cannot delete scheduled vaccines. -->
                        <button type="button" class="btn btn-danger" ng-click="removeStagedVaccination()">Cancel</button>

                        <button ng-if="enteredAdminFormData._administering" type="button" class="btn btn-primary" ng-click="resetFormDataToDefaults()">Reset</button>

                        <button ng-if="enteredAdminFormData._administering && form.$valid" type="submit" class="btn btn-warning" ng-click="saveVaccination(enteredAdminFormData)">Administer</button>

                        <button ng-if="enteredAdminFormData._scheduling && form.$valid" type="submit" class="btn btn-warning" ng-click="scheduleVaccination(enteredAdminFormData)">Book</button>
                    </div>
                </div>
            </form>
            <!-- / ADMINISTRATION FORM -->
        </div>
        <!-- /STAGED TEMPLATE -->
    </script>
    <!-- /STAGED VACCINATION -->

    <!-- /TEMPLATES -->
</div>
    <script src="${pageContext.request.contextPath}/moduleResources/vaccinations/scripts/vendor-1ddbb5d5.js"></script>

    <script src="${pageContext.request.contextPath}/moduleResources/vaccinations/scripts/app-7ca90298.js"></script>

  </body>
</html>
