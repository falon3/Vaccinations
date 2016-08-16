# Vaccinations
An OpenMRS module that allows management of patient vaccinations.
This module supports scheduled and unscheduled vaccinations, processing new vaccinations as well as backdating vaccinations.

###Installation

Install node
1. Clone the project

2. ```$ cd frontend```

3. ```$ npm install``` If you have problems try  ```$ sudo npm install```

4. ```$ bower install```

5. To serve locally ```$ gulp serve```

6. If you have problems with dependeinces not being met when running gulp try ```$ sudo rm -rf node_modules``` and then ```$ npm install```.

7. To build the OMOD ```$ gulp build```

If problems occur with node and npm versioning try using node version 0.10.36 & npm version 3.3.8. These are the versions the module was built with.

###Important
When serving locally, find and uncomment line ```URL: 'http://208.77.196.178:65010/openmrs',``` (found in /frontend/src/app/main/app_constants.service.js)   
Then also comment out the line below it ```URL: tempURL,```    

When committing, the line will need to be commented out again

