'use strict';

angular.module('vaccinations')
.controller('StagedVaccinationController', ['$scope', '$filter', 'vaccinationsManager',
    function ($scope, $filter, vaccinationsManager) {
    $scope.state = {};
    $scope.state.administerFormOpen = true;

    $scope.removeStagedVaccination = function () {
        vaccinationsManager.removeStagedVaccination();
    };

    $scope.resetFormDataToDefaults = function () {
        var vaccination = angular.copy($scope.getVaccination());
        if (vaccination.custom) {
            vaccination.name = '';
        }
        vaccination.administration_date = new Date();
        vaccination.scheduled_date = new Date();
        $scope.enteredAdminFormData = vaccination;
    };

    // Transform a vaccine object into a vaccination object that
    // can be submitted to the vaccinations endpoint.
    $scope.transformVaccineToVaccination = function (vaccine) {
        var vaccination = angular.copy(vaccine);
        // Create nested objects
        debugger;
        vaccination.adverse_reaction_observed = false;
        vaccination.simpleVaccine = {};
        vaccination.simpleVaccine.uuid = vaccine.uuid;
        vaccination.simpleAdverse_reaction = null;
        delete vaccination.uuid;
        return vaccination;
    };

    $scope.saveVaccination = function (enteredAdminFormData) {
        // When saving an administered vaccination ensure no scheduled
        // date is saved.
        var enteredAdminFormDataCopy = angular.copy(enteredAdminFormData);
        var vaccination = $scope.transformVaccineToVaccination(enteredAdminFormData);
        vaccination.scheduled_date = new Date();
        // This field is the vaccine_id and needs to be removed on posts to create new vaccinations.
        vaccination.id = null;
        vaccinationsManager.submitVaccination(vaccination);
    };

    $scope.scheduleVaccination = function (enteredAdminFormData) {
        // When scheduling remove date properties that are setup for
        // administration.
        var enteredAdminFormDataCopy = angular.copy(enteredAdminFormData);
        var vaccination = $scope.transformVaccineToVaccination(enteredAdminFormData);
        vaccination.administration_date = null;
        vaccination.manufacture_date = null;
        vaccination.expiry_date = null;
        vaccination.id = null;
        vaccinationsManager.submitVaccination(vaccination);
    };

    $scope.resetFormDataToDefaults();
}]);
