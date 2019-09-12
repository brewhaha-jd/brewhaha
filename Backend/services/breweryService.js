const
	breweryRepo = require('../models/brewery/breweryRepository');
	breweryMapper = require('../models/brewery/breweryMapper');
	errorHandler = require('../error_handlers/errorHandler');
	NodeGeocoder = require('node-geocoder');

module.exports = {
	getAll: function (callback) {
		breweryRepo.getAll(function (mongoErr, entities) {
			callback(mongoErr, entities);
		})
	},

	getById: function (id, callback) {
		breweryRepo.getById(id, function (err, entity) {
			if (entity == null && err != null) {
				err = errorHandler.throwMongoNotFoundError();
			}
			callback(err, entity);
		})
	},

	createBreweryAndMapCoordinates: function(resource, callback) {
		let geocoder = NodeGeocoder(global.config.geoCoderConfig);
		let addressToCode = resource.address.number + " " + resource.address.line1 + " " + resource.address.city;
		geocoder.geocode(addressToCode, function(err, results) {
			if (err) {
				callback(err, null)
			} else {
				let bestResult = results[0];
				resource.address.latitude = bestResult.latitude;
				resource.address.longitude = bestResult.longitude;
				resource.address.country = bestResult.country;
				resource.address.city = bestResult.city;
				resource.address.stateOrProvince = bestResult.state;
				resource.address.postalCode = bestResult.zipcode;
				resource.address.line1 = bestResult.streetName;
				resource.address.number = bestResult.streetNumber || resource.address.number;
				resource.address.county = bestResult.county;

				let entity = breweryMapper.mapResourceToEntity(resource);
				breweryRepo.create(entity, function (err, entity) {
					callback(err, entity)
				})
			}
		});
	}
};