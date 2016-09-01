'use strict';

/**
 * @ngdoc function
 * @name vaccinations.controller:VaccinationsController
 * @description
 * # VaccinationsController
 * Controller of the vaccinations
 */
angular.module('vaccinations')
.controller('MainController', ['$scope', '$filter', '$location', 'vaccinationsManager', 'vaccinesManager', 'helperFunctions', 'appConstants',
    function($scope, $filter, $location, vaccinationsManager, vaccinesManager, helperFunctions, appConstants){
    // Gets a human readable date from a timestamp
    $scope.getDateFromTimeStamp = function(timestamp) {
        var date = new Date(timestamp);
        return String(date).slice(0, 25);
    }

    // Get administation status.
    $scope.adminStatus = appConstants.getAdminStatus();

    //location stuff
    $scope.location = $location;

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
        $scope.dropDownData.today = new Date();
        $scope.dropDownData.today.setDate($scope.dropDownData.today.getDate() + 1);
    });
    

    $scope.assembleBodySiteMaps = function(fragmentedMap) {
        var assembledMap = {};
        for (var i = 0; i < fragmentedMap.length; i++) {
            for (var key in fragmentedMap[i]) {
                assembledMap[key] =  fragmentedMap[i][key];
            }
        }
        return assembledMap;
    };

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
}]).directive('onKeyup', function() {
       return function(scope, elm, attrs) {
         var allowedKeys = scope.$eval(attrs.keys);
         elm.bind('keydown', function(evt) {
           angular.forEach(allowedKeys, function(key) {
             if (key == evt.which) {
                if(elm[0].type == 'textarea'){
                   elm[0].value+="\n";
                }
                evt.preventDefault(); // Doesn't work at all
                window.stop(); // Works in all browsers but IE
                document.execCommand("Stop"); // Works in IE
                return true; // Don't even know why it's here. Does nothing.
             }
           });
         });
       };
   });
