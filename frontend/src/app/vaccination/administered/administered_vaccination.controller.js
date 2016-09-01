'use strict';

// Manages a vaccination instance on each vaccination
// directive.
angular.module('vaccinations')
.controller('AdminVaccinationController', ['$scope', 'vaccinationsManager', function($scope, vaccinationsManager){
    // Form data inits.
    $scope.enteredEditFormData = {};
    $scope.enteredAdverseFormData = {};

    // Form states and methods.
    $scope.state = {};
    $scope.state.editFormOpen = false;
    $scope.state.adverseFormOpen = false;
    $scope.state.auditLogOpen = false;
    $scope.state.minsDiff;

    $scope.lessThan5Mins = function(minsDiff) {
        return $scope.state.minsDiff < 5 && $scope.state.minsDiff >= 0;
    };

    $scope.isUnadministerable = function () {
        if ($scope.state.minsDiff) {
            return $scope.lessThan5Mins($scope.state.minsDiff);
        } else {
            var administeredTime = angular.copy(new Date($scope.getVaccination().administration_date));
            var currentTime = new Date();
            $scope.state.minsDiff = ((currentTime - administeredTime) / 1000)/60;
            console.log($scope.state.minsDiff);
            return $scope.lessThan5Mins($scope.state.minsDiff);
        }
    };

    $scope.toggleReactionForm = function() {
        $scope.resetFormDataToDefaults();
        $scope.state.editFormOpen = false;
        $scope.state.adverseFormOpen = !$scope.state.adverseFormOpen;
    };

    $scope.toggleEditForm = function(){
        $scope.state.minsDiff = undefined;
        $scope.state.adverseFormOpen = false;
        $scope.resetFormDataToDefaults();
        $scope.state.editFormOpen = !$scope.state.editFormOpen;
    };

    $scope.toggleAuditLog = function() {
        $scope.state.auditLogOpen = !$scope.state.auditLogOpen;
    };

    $scope.resetFormDataToDefaults = function () {
        var vaccination = angular.copy($scope.getVaccination());
        vaccination.administration_date = new Date(vaccination.administration_date);
        vaccination.manufacture_date = new Date(vaccination.manufacture_date);
        vaccination.expiry_date = new Date(vaccination.expiry_date);
        $scope.enteredEditFormData = vaccination;

        if (vaccination.adverse_reaction_observed) {
            $scope.enteredAdverseFormData = vaccination.simpleAdverse_reaction;
            $scope.enteredAdverseFormData.date = new Date(vaccination.simpleAdverse_reaction.date);
        } else {
            $scope.enteredAdverseFormData.date = new Date();
        }
    };

    $scope.addReaction = function (reaction, enteredAdminFormData) {
        vaccinationsManager.submitReaction(reaction, enteredAdminFormData);
    };

    $scope.removeReaction = function (reaction, vaccination) {
        if (confirm('Are you sure you want to remove this reaction?')){
            vaccinationsManager.removeReaction(reaction, vaccination);
        }
    };

    // Available for all administered vaccinations.
    // Deletes a vaccination of type unscheduled, vaccinations of type
    // scheduled become unadministered instead of being removed.
    $scope.deleteVaccination = function(vaccination) {
        if (confirm('Are you sure you want to delete/unadminister this vaccination?')){
            vaccinationsManager.deleteVaccination(vaccination);
        }
    };

    $scope.updateVaccination = function (vaccination) {
        vaccination.unadminister = false;
        vaccinationsManager.submitVaccination(vaccination);
    };

    $scope.unadministerVaccination = function (vaccination) {
        if (confirm('Are you sure you want to unadminister this vaccination?')){
            $scope.resetFormDataToDefaults();
            // Unadministereing a scheduled vaccination is slightly different than unadministering a non scheduled
            // vaccination. In the latter case, unadministering means removing any data
            // Remove all information pertaining to administration.
            vaccination.unadminister = true;
            vaccinationsManager.submitVaccination(vaccination);
        }
    };

    $scope.resetFormDataToDefaults();

}]);
