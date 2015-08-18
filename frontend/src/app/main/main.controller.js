'use strict';

/**
 * @ngdoc function
 * @name vaccinations.controller:VaccinationsController
 * @description
 * # VaccinationsController
 * Controller of the vaccinations
 */
angular.module('vaccinations')
.controller('MainController', ['$scope', '$filter', 'vaccinationsManager', 'vaccinesManager', 'helperFunctions',
    function($scope, $filter, vaccinationsManager, vaccinesManager, helperFunctions){

    // Get list of patient vaccinations.
    vaccinationsManager.getVaccinations().success(function(data) {
        $scope.vaccinations = data[0];
        $scope.dropDownData = {};
        $scope.dropDownData.routes = data[1];
        $scope.dropDownData.dosingUnits = data[2];
        $scope.dropDownData.bodySites = data[3];
        $scope.dropDownData.manufacturers = data[4];
        $scope.dropDownData.changeReasons = data[5];
        $scope.dropDownData.routeMaps = $scope.assembleBodySiteMaps(data[6]);
    });

    $scope.assembleBodySiteMaps = function(fragmentedMap) {
        debugger;
        var assembledMap = {};
        for (var i = 0; i < fragmentedMap.length; i++) {
            for (var key in fragmentedMap[i]) {
                assembledMap[key] =  fragmentedMap[i][key];
            }
        }
        return assembledMap;
    };

    //Returns a list of body sites based on route
    // $scope.getBodySiteMapping = function(route) {
    //     debugger;
    //     for (var i = 0; i < $scope.dropDownData.routeMaps.length; i++) {
    //         if ($scope.dropDownData.routeMaps[i].hasOwnProperty(route)) {
    //             return $scope.dropDownData.routeMaps[i][route];
    //         }
    //     }
    // };

    // Get list of staged vaccinations.
    $scope.stagedVaccinations = vaccinationsManager.getStagedVaccinations();

    // Get list of vaccines.
    vaccinesManager.getVaccines().success( function(data) {
        $scope.vaccines = data;
    });

    $scope.stageVaccination = function (vaccine, scheduled) {
        var stagedVaccination = angular.copy(vaccine);
        stagedVaccination._staged = true;
        if (scheduled) {
            stagedVaccination._scheduling = true;
        } else {
            stagedVaccination._administering = true;
        }
        vaccinationsManager.addStagedVaccination(stagedVaccination);
        $scope.newVaccine = '';
    };

    $scope.formatVaccine = function (vaccine) {
        return helperFunctions.formatVaccineName(vaccine);
    };
}]);
