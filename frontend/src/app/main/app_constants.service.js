'use strict';

/**
 * @ngdoc overview
 * @name vaccinations
 * @description
 * # vaccinations
 *
 * Main module of the application.
 */

// Constants for this instance of the app
angular.module('vaccinations')
.service('appConstants', function ($http, $location) {
    var exports = {
        // Set url for testing
        URL: '',
        PATH:'/openmrs/ws/rest/v2/vaccinationsmodule/vaccinations',

        // Retrive patient ID from window.location;
        getPatientId: function () {
            var patientId = $location.search().patientId;
            if (!patientId) {
                throw new Error('The patient ID could not be parsed from the URL');
            } else {
                return patientId;
            }
       }
    };

    return exports;
});