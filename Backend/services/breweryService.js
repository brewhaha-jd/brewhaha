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
	},

	getBreweriesNearLocation: function (location, rangeInMiles, sorted=true, callback) {
		let query = null;
		if (sorted) {
			const METERS_PER_MILE = 1609.34;
			query = {
				"address.location": {
					$nearSphere: {
						$geometry: {
							type: "Point",
							coordinates: location
						},
						$maxDistance: rangeInMiles * METERS_PER_MILE
					}
				}
			}
		} else {
			query = {
				"address.location": {
					$geoWithin: {
						$centerSphere: [ location, rangeInMiles / 3963.2 ]
					}
				}
			}
		}
		breweryRepo.getByQuery(query, function (err, entities) {
			callback(err, entities)
		})
	},

	getBreweriesByLocationAndRatings: function(location, rangeInMiles, ratings, callback) {
		const METERS_PER_MILE = 1609.34;
		let query = {
			"address.location": {
				$nearSphere: {
					$geometry: {
						type: "Point",
						coordinates: location
					},
					$maxDistance: rangeInMiles * METERS_PER_MILE
				}
			}
		};
		for (let i = 0; i < ratings.length; i++) {
			if (ratings[i].name === "minRecommendedAge") {
				query["friendlinessRating." + ratings[i].name] = {$lte: ratings[i].rating};
			}
			query["friendlinessRating." + ratings[i].name] = {$gte: ratings[i].rating};
		}
		breweryRepo.getByQuery(query, function (err, entities) {
			callback(err, entities)
		})


	},

	getBreweriesByRatings: function (ratings, sorted = true, callback) {
		for (let i = 0; i < ratings.length; i++) {
			if (ratings[i].name === "minRecommendedAge") {
				query["friendlinessRating." + ratings[i].name] = {$lte: ratings[i].rating};
			}
			query["friendlinessRating." + ratings[i].name] = {$gte: ratings[i].rating};
		}
		breweryRepo.getByQuery(query, function (err, entities) {
			callback(err, entities)
		})
	},

    getBreweriesBySearchingName: function (search, callback) {
        let query = [{
            $match: {
                name: {
                    $regex: search, '$options': 'i'
                }
            }
        }];
        breweryRepo.getByAggregate(query, function (err, entities) {
            callback(err, entities)
        })
    }
};