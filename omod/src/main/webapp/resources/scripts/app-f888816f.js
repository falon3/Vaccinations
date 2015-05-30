'use strict';

/**
 * @ngdoc overview
 * @name vaccinationsApp
 * @description
 * # vaccinationsApp
 *
 * Main module of the application.
 */
 angular
 .module('vaccinations', [
    'ngAnimate',
    'ngCookies',
    'ngSanitize',
    'ngResource',
    'angular.filter'
]);


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
         vaccination.administration_date = new Date();
         vaccination.manufacture_date = new Date();
         vaccination.expiry_date = new Date();
         vaccination.scheduled_date = new Date();
         $scope.enteredAdminFormData = vaccination;
    };

    $scope.saveVaccination = function (enteredAdminFormData) {
        // When saving an administered vaccination ensure no scheduled
        // date is saved.
        var enteredAdminFormDataCopy = angular.copy(enteredAdminFormData);
        delete enteredAdminFormDataCopy.scheduled_date;
        enteredAdminFormDataCopy.name = $filter('uppercase')(enteredAdminFormDataCopy.name);
        vaccinationsManager.submitVaccination(enteredAdminFormDataCopy);
    };

    $scope.scheduleVaccination = function (enteredAdminFormData) {
        // When scheduling remove date properties that are setup for
        // administration.
        var enteredAdminFormDataCopy = angular.copy(enteredAdminFormData);
        delete enteredAdminFormDataCopy.administration_date;
        delete enteredAdminFormDataCopy.manufacture_date;
        delete enteredAdminFormDataCopy.expiry_date;
        enteredAdminFormDataCopy.name = $filter('uppercase')(enteredAdminFormDataCopy.name);
        vaccinationsManager.submitVaccination(enteredAdminFormDataCopy);
    };

    $scope.resetFormDataToDefaults();
}]);

'use strict';

// Manages a vaccination instance on each vaccination
// directive.
angular.module('vaccinations')
.controller('UnAdminVaccinationController', ['$scope', 'vaccinationsManager',
    function($scope, vaccinationsManager){

    // Form states and methods.
    $scope.state = {};
    $scope.state.administerFormOpen = false;

    $scope.resetFormDataToDefaults = function(){
        var vaccination = angular.copy($scope.getVaccination());
        vaccination.administration_date = new Date();
        vaccination.manufacture_date = new Date();
        vaccination.expiry_date = new Date();
        vaccination.scheduled_date = new Date(vaccination.scheduled_date);
        $scope.enteredAdminFormData = vaccination;
        if ($scope.enteredAdminFormData.scheduled_date <= (new Date())) {
            $scope.due = true;
        }
    };

    $scope.toggleAdministerForm = function(){
        $scope.state.administerFormOpen = !$scope.state.administerFormOpen;
    };

    // Called when vaccination data from form has been validated
    // and ready to create a new vaccination event.
    $scope.submitVaccination = function(vaccination) {
        var vaccsOrigCopy = angular.copy($scope.getVaccination());
        vaccinationsManager.submitVaccination(vaccination, vaccsOrigCopy);
    };

    // Only available if the vaccination is of type unscheduled.
    $scope.deleteVaccination = function(vaccination) {
        vaccinationsManager.deleteVaccination(vaccination);
    };

    // Form data inits.
    $scope.resetFormDataToDefaults();
}]);

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

    $scope.toggleReactionForm = function(){
        $scope.state.editFormOpen = false;
        $scope.state.adverseFormOpen = !$scope.state.adverseFormOpen;
    };

    $scope.toggleEditForm = function(){
        $scope.state.adverseFormOpen = false;
        $scope.state.editFormOpen = !$scope.state.editFormOpen;
    };

    $scope.resetFormDataToDefaults = function () {
        var vaccination = angular.copy($scope.getVaccination());
        vaccination = angular.copy($scope.getVaccination());
        vaccination.administration_date = new Date(vaccination.administration_date);
        vaccination.manufacture_date = new Date(vaccination.manufacture_date);
        vaccination.expiry_date = new Date(vaccination.expiry_date);
        $scope.enteredEditFormData = vaccination;

        if (vaccination.adverse_reaction_observed) {
            $scope.enteredAdverseFormData = vaccination.reaction_details;
            $scope.enteredAdverseFormData.date = new Date(vaccination.reaction_details.date);
        } else {
            $scope.enteredAdverseFormData.date = new Date();
        }
    };

    $scope.addReaction = function (reaction, enteredAdminFormData) {
        vaccinationsManager.submitReaction(reaction, enteredAdminFormData);
    };

    $scope.removeReaction = function (reaction) {
        vaccinationsManager.removeReaction(reaction);
    };

    // Available for all administered vaccinations.
    // Deletes a vaccination of type unscheduled, vaccinations of type
    // scheduled become unadministered instead of being removed.
    $scope.deleteVaccination = function(vaccination) {
        vaccinationsManager.deleteVaccination(vaccination);
    };

    $scope.updateVaccination = function (vaccination) {
        vaccinationsManager.submitVaccination(vaccination);
    };

    $scope.unadministerVaccination = function () {
        // Remove all information pertaining to administration.
        var vaccination = angular.copy($scope.getVaccination());
        vaccination.administered = false;
        delete vaccination.provider_id;
        delete vaccination.scheduler_id;
        delete vaccination.adverse_reaction_observed;
        delete vaccination.reaction_details;
        delete vaccination.administration_date;
        delete vaccination.lot_number;
        delete vaccination.manufacture_date;
        delete vaccination.expiry_date;
        delete vaccination.manufacturer;
        vaccinationsManager.submitVaccination(vaccination);
    };

    $scope.resetFormDataToDefaults();

}]);

'use strict';

angular.module('vaccinations')
.service('vaccinesManager', ['$http', function($http) {
    var self = this;
    var promise = $http.get('/openmrs/ws/rest/v2/vaccinationsmodule/vaccines').success( function(data) {
        self.vaccines = data;
    }).error( function(data, status, headers, config) {
        debugger;
    });

    var exports = {
        getVaccines: function() {
            return promise;
        }
    };

    return exports;

}]);
'use strict';

// Manages the retrival of vaccinations from the server and the
// removal and entry of vaccinations for a patient.
angular.module('vaccinations')
.service('vaccinationsManager', ['$http', '$filter', '$rootScope', 'appConstants', 'helperFunctions',
    function($http, $filter, $rootScope, appConstants, helperFunctions){
    var self = this;
    self.stagedVaccinations = [];

    var setVaccinations = function (vaccinations) {
        if (self.vaccinations){
            throw new Error('Vaccinations have already been set.');
        } else {
            self.vaccinations = vaccinations;
        }
    };

    var promise = $http.get('/openmrs/ws/rest/v2/vaccinationsmodule/vaccinations/patients/1')
        .success(function(data, status, headers, config){
            setVaccinations(data.vaccinations);
        })
        .error(function(data, status, headers, config){
            alert('Error when retrieving patient vaccinations from server.');
        });

    var exports = {

        addVaccination: function(vaccination) {
            var index = helperFunctions.findObjectIndexByAttribute('_id', vaccination._id, self.vaccinations);
            if (index === undefined){
                self.vaccinations.push(vaccination);
            } else {
                throw new Error('Could not add vaccination to array, a vaccination with the _id attribute already exists.');
            }
        },

        // vaccinationCopy provides a way to remove template vaccinations
        // from the vaccinations list. It is only required when calling
        // the function with a new vaccination, not when the vaccination
        // exists on the server and needs to be modified.
        submitVaccination: function(vaccination, vaccsOrigCopy) {
            var that = this;
            $rootScope.$broadcast('waiting');
            // Prevent unintentional sending of reaction details
            // modifications.
            var vaccination = angular.copy(vaccination);
            try {
                delete vaccination.reaction_details;
            } catch(err) {
                console.log(err);
            }
            // Check whether we are updating an existing vaccination
            // or adding new vaccination.
            if (vaccination.hasOwnProperty('_id')) {
                // Vaccination exists, modify on server.
                $http.put(
                    '/vaccinations/' + vaccination._id +
                    '/patients/' + appConstants.getPatientId(window.location.href),
                    {vaccination: vaccination})
                .success( function (data) {
                    // Remove the old version and add the new version
                    $rootScope.$broadcast('success');
                    that.removeVaccination(vaccination._id);
                    that.addVaccination(data.vaccination); })
                .error( function (data) {
                    $rootScope.$broadcast('failure');
                    alert("The vaccination was not saved. Please try again.");
                });
            } else {
                // Vaccination does not exist on server. Post to server.
                $http.post(
                    '/vaccinations/patients/' + appConstants.getPatientId(window.location.href),
                    {vaccination: vaccination} )
                .success( function (data) {
                    // This catches new, unscheduled vaccinations
                    // that have not been saved to the patients records.
                    $rootScope.$broadcast('success');
                    if (vaccination._staged) {
                        that.removeStagedVaccination();
                    // This catches scheduled vaccinations that
                    // have yet to be saved to the patients records.
                    // Since scheduled unadministered vaccs have no id
                    // remove old version using object equality.
                    } else {
                        var idx = helperFunctions.findObjectIndexByEquality(vaccsOrigCopy, self.vaccinations);
                        self.vaccinations.splice(idx, 1);
                    }
                    that.addVaccination(data.vaccination); })
                .error( function (data) {
                    $rootScope.$broadcast('failure');
                    alert("The vaccination was not saved. Please try again.");
                });
            }
        },

        deleteVaccination: function(vaccination) {
            $rootScope.$broadcast('waiting');
            var that = this;
            $http.delete(
                '/vaccinations/' + vaccination._id +
                '/patients/' + appConstants.getPatientId(window.location.href))
            .success( function () {
                that.removeVaccination(vaccination._id);
                $rootScope.$broadcast('success');
            })
            .error( function () {
                $rootScope.$broadcast('error');
                alert("The vaccination could not be deleted. Please try again.");
            });
        },

        submitReaction: function (reaction, vaccination) {
            // Get the vaccination from the array.
            // Adding reaction details to the vaccination passed
            // into the function will only change the copy.
            $rootScope.$broadcast('waiting');
            var that = this;
            if (vaccination.adverse_reaction) {
                $http.put(
                    '/vaccinations/' + vaccination._id +
                    '/patients/' + appConstants.getPatientId(window.location.href) +
                    '/adverse_reactions/' + reaction._id,
                    {reaction: reaction} )
                .success( function (data) {
                    $rootScope.$broadcast('success');
                    that.removeVaccination(vaccination._id);
                    that.addVaccination(data.vaccination);
                })
                .error( function (data) {
                    $rootScope.$broadcast('failure');
                    alert("An error occured while sending information to server. Try again.");
                });
            } else {
                $http.post(
                    '/vaccinations/' + vaccination._id +
                    '/patients/' + appConstants.getPatientId(window.location.href) +
                    '/adverse_reactions',
                    {reaction: reaction} )
                .success( function (data) {
                    $rootScope.$broadcast('success');
                    that.removeVaccination(vaccination._id);
                    that.addVaccination(data.vaccination);
                })
                .error( function (data) {
                    $rootScope.$broadcast('failure');
                    alert("An error occured while sending information to server. Try again.");
                });
            }
        },

        removeReaction: function (reaction) {
            var that = this;
            $rootScope.$broadcast('waiting');
            $http.delete(
                '/vaccinations/' + reaction._vaccination_id +
                '/patients/' + appConstants.getPatientId(window.location.href) +
                '/adverse_reactions/' + reaction._id,
                {reaction: reaction})
            .success( function (data) {
                $rootScope.$broadcast('success');
                that.removeVaccination(data.vaccination._id);
                that.addVaccination(data.vaccination);
            })
            .error( function (data) {
                $rootScope.$broadcast('failure');
                alert("An error occured while sending information to the server. Try again.");
            });
        },

        getVaccinations: function(){
            return promise;
        },

        getVaccinationById: function(id){
            var vaccination = $filter('filter')(self.vaccinations, function(vaccination, index){
                return vaccination._id === id;
            });
            return vaccination[0];
        },

        removeVaccination: function(id){
            var index = helperFunctions.findObjectIndexByAttribute('_id', id, self.vaccinations);
            if (index !== undefined){
                self.vaccinations.splice(index, 1);
            } else {
                console.log("The index of the vaccination to be removed could not be found.");
            }
        },

        addStagedVaccination: function(vaccine) {
            // We only want 1 staged vaccination at a time to keep
            // things organized. So clear the array first.
            this.removeStagedVaccination();
            self.stagedVaccinations.push(vaccine);
        },

        getStagedVaccinations: function () {
            return self.stagedVaccinations;
        },

        removeStagedVaccination: function () {
            self.stagedVaccinations.length = 0;
        }
    };

    return exports;
 }]);

'use strict';

angular.module('vaccinations')
.directive('vaccination', [ '$timeout', 'helperFunctions', function($timeout, helperFunctions) {
    return {
        restrict: 'E',
        // The link function provides access to the scope of the vaccination
        // element. Use the vaccinations.administered property to decide on
        // which template to include administered/unadministered.
        // The controller is assigned in the html templates.
        compile: function compile (element, attrs) {
            return {
                post: function postLink(scope, element, attributes) {
                    // Add popover for staged vaccinations.
                    scope.vaccination = {};
                    if (scope.getVaccination()._staged) {
                        scope.vaccination._staged = true;
                    }
                    scope.vaccination.administered = scope.getVaccination().administered;

                    scope.getContentUrl = function(){
                        if (scope.vaccination.hasOwnProperty('_staged')) {
                            return '/app/vaccination/staged/vaccination_staged.template.html';
                        }
                        else if (scope.vaccination.administered){
                            return '/app/vaccination/administered/vaccination_administered.template.html';
                        }
                        else if (!scope.vaccination.administered){
                            return '/app/vaccination/unadministered/vaccination_unadministered.template.html';
                        }
                    };
                }
            };
        },

        template: '<div ng-include="getContentUrl()"></div>',
        scope: {
            getVaccination: '&',
        }
    };
 }]);
'use strict';

/**
 * @ngdoc function
 * @name vaccinations.controller:VaccinationsController
 * @description
 * # VaccinationsController
 * Controller of the vaccinations
 */
angular.module('vaccinations')
.controller('MainController', ['$scope', 'vaccinationsManager', 'vaccinesManager', 'helperFunctions',
    function($scope, vaccinationsManager, vaccinesManager, helperFunctions){

    // Get list of patient vaccinations.
    vaccinationsManager.getVaccinations().success(function(data) {
        $scope.vaccinations = data.vaccinations;
    });


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

'use strict';

angular.module('vaccinations')
.service('helperFunctions', function(){
    return {
        // Format vaccine names for dropdown
        formatVaccineName: function (vaccine) {
            var formattedVaccineName = vaccine.name + ':: ';
            if (vaccine.custom) {
                return 'Custom Vaccine::';
            }
            if (typeof vaccine.dose !== 'undefined') {
                formattedVaccineName += 'Dose: ' + vaccine.dose + ' ';
            }
            if (typeof vaccine.dosing_unit !== 'undefined') {
                formattedVaccineName += 'Unit: ' + vaccine.dosing_unit + ' ';
            }
            if (typeof vaccine.route !== 'undefined') {
                formattedVaccineName += 'Route: ' + vaccine.route + ' ';
            }
            return formattedVaccineName;
        },

        // Generate a random color
        getColor: function (mix) {
            var red = Math.floor((Math.random() * 16)) + 245;
            var green = Math.floor((Math.random() * 21)) + 235;
            var blue = Math.floor((Math.random() * 21)) + 235;
            return 'rgb(' + red + ',' + green + ',' + blue + ')'
        },

        // Find the index of an object with a given id.
        findObjectIndexByAttribute: function(attribute, attributeValue, array){
            for (var i = 0; i < array.length; i++) {
                if (array[i][attribute] === attributeValue){
                    return i;
                }
            }
            return undefined;
        },

        // Find the index of an object in an array.
        findObjectIndexByEquality: function(obj, array) {
            for (var i = 0; i < array.length; i++) {
                if (angular.equals(obj, array[i])) {
                    return i;
                }
            }
            return undefined;
        }
    };
});
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
.service('appConstants', ["$http", function ($http) {
    var exports = {
        // Should only be used for testing purposes
        setPatiendId: function (id) {
            this.patientId = id;
        },

        // TODO: Get the session ID if its not auto appended to requests.
        getSessionId: function () {

        },

        getURL: function (url) {

        },

        // Retrive patient ID from window.location;
        getPatientId: function (url) {
            if (this.patientId) {
                return this.patientId;
            }
            var patientId = /[0-9]+$/.exec(url);
            if (patientId === undefined || patientId === null) {
                throw new Error('The patient ID cannot be parsed from the URL');
            }
            if (patientId.length > 1 || patientId.length === 0) {
                throw new Error('The patient ID URL matched regex more than once.');
            }
            if (patientId[0] === undefined) {
                throw new Error('The patient ID could not be parsed from URL.');
            }
            // TODO: remove patientId and return actual
            return patientId[0];
        }
    };

    return exports;
}]);
'use strict';

angular.module('vaccinations')
.directive('loader', function () {
    var ddo = {
        replace: true,
        templateUrl: '/app/loader/loader.template.html',
        controller: function ($scope, $timeout) {
            $scope.state = {};
            $scope.state.loading = false;
            $scope.state.success = false;

            $scope.$on('waiting', function () {
                $scope.state.loading = true;
            });

            $scope.$on('success', function () {
                $scope.state.loading = false;
                $scope.state.success = true;
                $timeout( function () { $scope.state.success = false; }, 1000);
            });

            $scope.$on('failure', function () {
                $scope.state.loading = false;
                $scope.state.success = false;
            });
        }
    };

    return ddo;
});

'use strict';

angular.module('vaccinations')
.directive('feedback', [ function () {
    var ddo = {
        replace: true,
        templateUrl: '/app/feedback/feedback.template.html',
        scope: {
            // Boolean indicating whether to show or hide.
            warn: '&',
            warning: '@'
        }
    };

    return ddo;
}]);
'use strict';

angular.module('mockData', [])
.value('mockObjects', {
    vaccinations:[
    {
      "_id": "54f88ccdfb16c75e35261900",
      "_vaccine_id": "550b46047fcba390ff335aaa",
      "scheduled": true,
      "name": "Bacillus Calmette-Guerin",
      "indication_name": "Birth to 1 yo",
      "dose": 0.05,
      "dosing_unit": "ml",
      "route": "intra-dermal",
      "administered": true,
      "adverse_reaction_observed": false,
      "administration_date": "2014-10-01",
      "scheduled_date": "2014-09-05",
      "body_site_administered": "left forearm",
      "dose_number": 1,
      "manufacturer": "P. Corp.",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    },
    {
      "_vaccine_id": "550b46047fcba390ff335bba",
      "scheduled": true,
      "name": "Bacillus Calmette-Guerin",
      "indication_name": "Older than 1 yo",
      "dose": 0.1,
      "dosing_unit": "ml",
      "route": "intra-dermal",
      "administered": false,
      "adverse_reaction_observed": false,
      "administration_date": "",
      "scheduled_date": "2015-09-05",
      "body_site_administered": "left forearm",
      "dose_number": 2,
    },
    {
      "_id": "54f88ccdfb16c75e35261802",
      "_vaccine_id": "550b46047fcba390ff335cca",
      "scheduled": true,
      "name": "Polio",
      "indication_name": "Birth to 2 weeks",
      "dose": 2,
      "dosing_unit": "drops",
      "route": "PO",
      "administered": true,
      "adverse_reaction_observed": false,
      "administration_date": "2014-09-01",
      "scheduled_date": "2014-09-01",
      "body_site_administered": "Site",
      "dose_number": 1,
      "manufacturer": "P. Corp.",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    },
    {
      "_id": "54f88ccd6bba27070e77d3dc",
      "_vaccine_id": "4aslkjlkjaslkfjlkah234ql",
      "scheduled": false,
      "provider_id": "AAAA",
      "name": "Rotavirus",
      "dose": 0.1,
      "dosing_unit": "ml",
      "route": "",
      "administered": true,
      "adverse_reaction_observed": false,
      "administration_date": "2014-10-01",
      "scheduled_date": "2014-10-01",
      "body_site_administered": "",
      "manufacturer": "P. Corp.",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    },
    {
      "_vaccine_id": "550b46047fcba390ff335fff",
      "scheduled": true,
      "name": "Measles",
      "indication_name": "9 mo",
      "dose": .5,
      "dosing_unit": "ml",
      "route": "SC",
      "administered": false,
      "adverse_reaction_observed": false,
      "administration_date": "",
      "scheduled_date": "2015-03-01",
      "body_site_administered": "right outer thigh",
      "dose_number": 1,
    },
    {
      "_id": "54f88ccddce0415f38397d33",
      "_vaccine_id": "999",
      "scheduled": false,
      "provider_id": "BBBB",
      "name": "Hepatitis B",
      "dose": 2,
      "dosing_unit": "ml",
      "route": "",
      "administered": true,
      "adverse_reaction_observed": false,
      "administration_date": "2014-10-01",
      "scheduled_date": "2014-10-01",
      "body_site_administered": "",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    },
    {
      "_id": "54f88ccddce0415f38397d34",
      "_vaccine_id": 60000,
      "scheduled": false,
      "name": "Hepatitis B",
      "dose": 2,
      "dosing_unit": "ml",
      "route": "intra dermal",
      "administered": false,
      "adverse_reaction_observed": false,
      "administration_date": "",
      "scheduled_date": "2014-11-01",
      "body_site_administered": "left deltoid",
    },
    {
      "_id": "54f88ccda57c4a6954ecfe19",
      "_vaccine_id": "550b46047fcba390ff33ddda",
      "scheduled": true,
      "provider_id": "BBBB",
      "name": "Polio",
      "indication_name": "6 weeks",
      "dose": 2,
      "dosing_unit": "drops",
      "route": "PO",
      "administered": true,
      "adverse_reaction_observed": false,
      "administration_date": "2014-10-01",
      "scheduled_date": "2014-10-01",
      "body_site_administered": "left thigh",
      "dose_number": 2,
      "manufacturer": "P. Corp.",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    },
    {
      "_id": "54f88ccd2b5b5bbe9ad07c59",
      "_vaccine_id": 80000,
      "scheduled": false,
      "name": "Rotavirus",
      "dose": 0.05,
      "dosing_unit": "ml",
      "route": "intra dermal",
      "administered": false,
      "adverse_reaction_observed": false,
      "administration_date": "",
      "scheduled_date": "2014-11-01",
    },
    {
      "_id": "54f88ccdefe294237482eb5f",
      "_vaccine_id": "550b4604d6f88cbkkk3587ba",
      "scheduled": false,
      "provider_id": "BBBB",
      "name": "DPT",
      "dose": 0.1,
      "dosing_unit": "ml",
      "route": "intra dermal",
      "administered": true,
      "adverse_reaction_observed": true,
      "reaction_details": {
          "_id": "458d82kd",
          "_vaccination_id":"54f88ccdefe294237482eb5f",
          "date": "2014-10-01",
          "adverse_event_description": "Something bad, but not too bad.",
          "grade": "160754",
      },
      "administration_date": "2014-10-01",
      "scheduled_date": "2014-10-01",
      "body_site_administered": "outer thigh",
      "manufacturer": "P. Corp.",
      "lot_number": "1000",
      "manufacture_date": "2014-01-01",
      "expiry_date": "2016-01-01"
    }
  ],

  non_scheduled_vaccines: [

      {
        "_vaccine_id": "550b4604d6f88cbkkk3587ba",
        "name": "Rabies",
        "scheduled": false
      },
      {
        "_vaccine_id": "550b4604d6f88cbkkk3587ba",
        "name": "DPT",
        "scheduled": false
      },
      {
        "_vaccine_id": "550b460499dc11cccf4420bb",
        "name": "Hepatitis A",
        "scheduled": false
      },
      {
        "_vaccine_id": "550b4604a8298qqqb151205d",
        "name": "Hepatitis B",
        "scheduled": false
      },
      {
        "_vaccine_id": "4aslkjlkjaslkfjlkah234ql",
        "name": "Rotavirus",
        "scheduled": false
      },
      {
        "_vaccine_id": "550b4604a829wwweb1512ccc",
        "name": "Vitamin A",
        "dose": 100000,
        "dosing_unit": "IU",
        "route": "PO",
        "indication_name": "6 mo",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b4604a829wwwxxx512ccc",
        "name": "Rabies",
        "scheduled": false
      },
      {
        "_vaccine_id": "5zzz4604a829wwwxxx512ccc",
        "name": "Cholera",
        "scheduled": false
      },
      {
        "_vaccine_id": "5zzz4gg4a829wwwxxx512ccc",
        "name": "Mumps",
        "scheduled": false
      },
      {
        "_vaccine_id": "xzzz4gg4a829wwwxxx512ccc",
        "name": "Rubella",
        "scheduled": false
      },
      {
        "_vaccine_id": "xzzz4gg4ls29wwwxxx512ccc",
        "name": "Tetanus toxoid",
        "scheduled": false
      },
      {
        "_vaccine_id": "CUSTOM",
        "custom": true,
        "name": "",
        "scheduled": false,
      }

    ],

    scheduled_vaccines: [
      {
        "_vaccine_id": "550b46047fcba390ff335aaa",
        "name": "Bacillus Calmette-Guerin",
        "dose": .05,
        "dosing_unit": "ml",
        "dose_number": 1,
        "route": "Intra-dermal left forearm",
        "indication_name": "Birth to 1 yo",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff335bba",
        "name": "Bacillus Calmette-Guerin",
        "dose": .1,
        "dosing_unit": "ml",
        "dose_number": 2,
        "route": "Intra-dermal left forearm",
        "indication_name": "Older than 1 yo",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff335cca",
        "name": "Polio",
        "dose": 2,
        "dosing_unit": "drops",
        "dose_number": 1,
        "route": "PO",
        "indication_name": "Birth to 2 weeks",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff33ddda",
        "name": "Polio",
        "dose": 2,
        "dosing_unit": "drops",
        "dose_number": 2,
        "route": "PO",
        "indication_name": "6 weeks",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff33xxxa",
        "name": "Polio",
        "dose": 2,
        "dosing_unit": "drops",
        "dose_number": 3,
        "route": "PO",
        "indication_name": "10 weeks",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff335ccc",
        "name": "Polio",
        "dose": 2,
        "dosing_unit": "drops",
        "dose_number": 4,
        "route": "PO",
        "indication_name": "14 weeks",
        "scheduled": true
      },
      {
        "_vaccine_id": "550b46047fcba390ff335fff",
        "name": "Measles",
        "dose": .5,
        "dosing_unit": "SC",
        "dose_number": 1,
        "route": "right upper arm",
        "indication_name": "9 mo",
        "scheduled": true
      }
    ]
})
'use strict';

// var mockBackend = angular.module('mockBackend', ['vaccinations', 'ngMockE2E', 'mockData']);
// mockBackend.run(["$httpBackend", "$timeout", "mockObjects", "helperFunctions", "appConstants", function($httpBackend, $timeout, mockObjects, helperFunctions, appConstants){
//     // Get the mock json data from the mockData module.
//     var vaccinations = mockObjects.vaccinations;
//     var loaderDelay = 10000;
//     appConstants.setPatiendId(1);

//     $httpBackend.whenGET(/^\/?vaccinations\/patients\/1/).respond(mockObjects);
//     $httpBackend.whenGET(/^\/?vaccines/).respond(mockObjects);
//     $httpBackend.whenGET(/^\/?vaccines\/non_scheduled$/).respond(mockObjects);

//     $httpBackend.whenPOST(/^\/vaccinations\/patients\/1/).respond(function(method, url, data){
//         var vaccination = angular.fromJson(data).vaccination;
//         // Add a vaccination id field and remove the
//         // staged marker.
//         var time = new Date().getTime() + loaderDelay;
//         while (new Date() < time) {}

//         vaccination._id = "NEWLYADDED" + Math.floor(Math.random() * 10000000);
//         delete vaccination._staged;
//         delete vaccination._administering;
//         delete vaccination._scheduling;
//         if (vaccination.administration_date) {
//             vaccination.administered = true;
//         } else {
//             vaccination.administered = false;
//         }
//         vaccinations.push(vaccination);
//         return [200, {vaccination:vaccination}, {}];

//     });

//     $httpBackend.whenPUT(/^\/vaccinations\/[a-zA-Z0-9_]+\/patients\/[a-zA-Z0-9]+$/)
//         .respond( function (method, url, data) {
//             var time = new Date().getTime() + loaderDelay;
//             while (new Date() < time) {}

//             var vaccination = angular.fromJson(data).vaccination;
//             var index = helperFunctions.findObjectIndexByAttribute('_id', vaccination._id, vaccinations);
//             if (vaccination.administration_date) {
//                 vaccination.administered = true;
//             }

//             // Get old reaction details since they aren't sent
//             var reaction_details = vaccinations[index].reaction_details;
//             vaccination.reaction_details = reaction_details;
//             vaccinations.splice(index, 1);
//             vaccinations.push(vaccination);
//             return [200, {vaccination: vaccination}, {}];
//     });

//     $httpBackend.whenDELETE(/^\/vaccinations\/[a-zA-Z0-9]+\/patients\/[a-zA-Z0-9]+$/)
//         .respond( function (method, url, data) {
//             var time = new Date().getTime() + loaderDelay;
//             while (new Date() < time) {}

//             var vaccinationId = /[a-zA-Z0-9]+(?=\/patients\/)/.exec(url)[0];
//             var index = helperFunctions.findObjectIndexByAttribute('_id', vaccinationId, vaccinations);
//             vaccinations.splice(index, 1);

//             return [200, {}, {}];
//     });

//     $httpBackend.whenPOST(/^\/vaccinations\/[a-zA-Z0-9]+\/patients\/[a-zA-Z0-9]+\/adverse_reactions$/)
//         .respond(function (method, url, data) {
//             var time = new Date().getTime() + loaderDelay;
//             while (new Date() < time) {}

//             var reaction = angular.fromJson(data).reaction;

//             var vaccinationId = /[a-zA-Z0-9]+(?=\/patients\/)/.exec(url)[0];
//             var index = helperFunctions.findObjectIndexByAttribute('_id', vaccinationId, vaccinations);

//             var vaccination = vaccinations[index];
//             vaccination.reaction_details = reaction;
//             vaccination.reaction_details._id = "NEWLYADDED" + Math.floor(Math.random() * 10000000);
//             vaccination.adverse_reaction_observed = true;
//             vaccination.reaction_details._vaccination_id = vaccination._id;

//             return [200, {vaccination: vaccination}, {}];
//     });

//     $httpBackend.whenPUT(/^\/vaccinations\/[a-zA-Z0-9]+\/patients\/[a-zA-Z0-9]+\/adverse_reactions\/[0-9a-zA-Z]+$/)
//         .respond(function (method, url, data) {
//             var time = new Date().getTime() + loaderDelay;
//             while (new Date() < time) {}

//             var reaction = angular.fromJson(data).reaction;

//             var vaccinationId = /[a-zA-Z0-9]+(?=\/patients\/)/.exec(url)[0];
//             var index = helperFunctions.findObjectIndexByAttribute('_id', vaccinationId, vaccinations);
//             var vaccination = vaccinations[index];
//             vaccination.reaction_details = reaction;
//             vaccination.adverse_reaction_observed = true;

//             return [200, {vaccination: vaccination}, {}];
//     });

//     $httpBackend.whenDELETE(/^\/vaccinations\/[a-zA-Z0-9]+\/patients\/[a-zA-Z0-9]+\/adverse_reactions\/[0-9a-zA-Z]+$/)
//         .respond( function (method, url, data) {

//             var time = new Date().getTime() + loaderDelay;
//             while (new Date() < time) {}

//             var vaccinationId = /[a-zA-Z0-9]+(?=\/patients\/)/.exec(url)[0];
//             var index = helperFunctions.findObjectIndexByAttribute('_id', vaccinationId, vaccinations);
//             var vaccination = vaccinations[index];
//             vaccination.adverse_reaction_observed = false;
//             delete vaccination.reaction_details;
//             return [201, {vaccination: vaccination}, {}];
//         })

//     // Do not serve anything from the mock server on these routes.
//     $httpBackend.whenGET(/\/?mock_data\/.+/).passThrough();
//     $httpBackend.whenGET(/\/?app\/.+/).passThrough();
// }]);
// // Manually bootstrap the backend.
// angular.element(document).ready(function () {
//     angular.bootstrap(document, ['mockBackend']);
// });
