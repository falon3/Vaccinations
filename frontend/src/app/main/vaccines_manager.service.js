'use strict';

angular.module('vaccinations')
.service('vaccinesManager', ['$http', 'appConstants', function($http, appConstants) {
    var self = this;
    var promise = $http.get(
        appConstants.URL +
        '/ws/rest/v2/vaccinationsmodule' +
        '/vaccines/unscheduled')

    .success( function(data) {
        // Add flag to denote this is a custom vaccine.
        // It is required because an extra field is available as 'vaccine name'
        // when entering a custom vaccine.
        for (var i = 0; i < data.length; i++) {
            if (data[i].name === 'Custom') {
                data[i].name = 'Custom vaccine::';
                data[i].custom = true;
                console.log(data[i]);
                break;
            }
        }
        self.vaccines = data;
    });

    var exports = {
        getVaccines: function() {
            return promise;
        }
    };

    return exports;

}]);