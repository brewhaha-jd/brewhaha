const
	breweryRepo = require('../models/brewery/breweryRepository');
	breweryMapper = require('../models/brewery/breweryMapper');
	reviewService = require('./reviewService');
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

	updateBrewery: function(id, resource, callback) {
		breweryRepo.update(id, resource, function (err, entity) {
			callback(err, entity)
		});
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
		let query = {};
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
    },

	updateBreweryRatings: function (id, callback) {
		let getReviewsPromise = new Promise(function (resolve, reject) {
			reviewService.getReviewsByBrewery(id, function (err, entities) {
				if (err) {
					reject(err)
				} else {
					resolve(entities)
				}
			})
		});
		getReviewsPromise.then(function (results) {
			let reviewAverages = calculateAverages(results);
			reviewAverages.aggregate = calculateAggregate(reviewAverages);
			breweryRepo.updateReviews(id, reviewAverages, function (err, entities) {
				callback(err, entities)
			})
		})
	}
};

function calculateAggregate(reviewAverages) {
	let aggregate = null;
	let sum = 0;

	let arr = [];
	if (reviewAverages.kidsFoodAvg != null) {
		arr.push(reviewAverages.kidsFoodAvg)
	}
	if (reviewAverages.kidsEntertainmentAvg != null) {
		arr.push(reviewAverages.kidsEntertainmentAvg)
	}
	if (reviewAverages.bathroomsAvg != null) {
		arr.push(reviewAverages.bathroomsAvg)
	}
	if (arr.length) {
		sum = arr.reduce(function (a, b) {return a + b;});
		aggregate = sum / arr.length;
	}
	return aggregate;
}

function calculateAverages(reviews) {
	let kidsFoodAvg, kidsEntertainmentAvg, bathroomsAvg, minAgeAvg = null;

	let kidsFoodArr = [];
	let kidsEntertainmentArr = [];
	let bathroomsArr = [];
	let minAgeArr = [];
	for (let i = 0; i < reviews.length; i++) {
		if (reviews[i].friendlinessRating.kidsFood != null) {
			kidsFoodArr.push(reviews[i].friendlinessRating.kidsFood)
		}
		if (reviews[i].friendlinessRating.kidsEntertainment != null) {
			kidsEntertainmentArr.push(reviews[i].friendlinessRating.kidsEntertainment)
		}
		if (reviews[i].friendlinessRating.bathrooms != null) {
			bathroomsArr.push(reviews[i].friendlinessRating.bathrooms)
		}
		if (reviews[i].friendlinessRating.minRecommendedAge != null) {
			minAgeArr.push(reviews[i].friendlinessRating.minRecommendedAge)
		}
	}
	if (kidsFoodArr.length) {
		let sum = kidsFoodArr.reduce(function (a, b) {return a + b;});
		kidsFoodAvg = sum / kidsFoodArr.length;
	}
	if (kidsEntertainmentArr.length) {
		let sum = kidsEntertainmentArr.reduce(function (a, b) {return a + b;});
		kidsEntertainmentAvg = sum / kidsEntertainmentArr.length;
	}
	if (bathroomsArr.length) {
		let sum = bathroomsArr.reduce(function (a, b) {return a + b;});
		bathroomsAvg = sum / bathroomsArr.length;
	}
	if (minAgeArr.length) {
		let sum = minAgeArr.reduce(function (a, b) {return a + b;});
		minAgeAvg = sum / minAgeArr.length;
	}

	return {
		"kidsFoodAvg" : kidsFoodAvg,
		"kidsEntertainmentAvg" : kidsEntertainmentAvg,
		"bathroomsAvg" : bathroomsAvg,
		"minAgeAvg" : minAgeAvg
	}

}