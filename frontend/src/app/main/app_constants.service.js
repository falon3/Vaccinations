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
    var tempURL;
    if (document.getElementsByTagName("title")[0].innerHTML !== "KMRI") {
        tempURL = '';
    } else {
        tempURL = '';
    }

    var exports = {
        // Set url for testing front end locally
        //URL: 'http://208.77.196.178:65010',
        URL: tempURL,
        PATH:'/openmrs/ws/rest/v2/vaccinationsmodule/vaccinations',

        // Retrive patient ID from window.location;
        getPatientId: function () {
            var patientId = $location.search().patientId;
            if (!patientId) {
                throw new Error('The patient ID could not be parsed from the URL');
            } else {
                return patientId;
            }
       },

       getAdminStatus: function () {
            return ('True' === $location.search().retroactive || 'true' === $location.search().retroactive);
       }
    };

    return exports;
});