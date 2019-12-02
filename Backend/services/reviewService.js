const
	reviewRepo = require('../models/review/reviewRepository');
	breweryService = require('./breweryService');
	errorHandler = require('../error_handlers/errorHandler');

module.exports = {
	createReview: function(resource, userId, callback) {
		resource.user = userId;
		resource.datePosted = new Date().toISOString();
		resource.friendlinessRating.aggregate = calculateAggregate(resource);
		reviewRepo.create(resource, function (err, entity) {
			if (err) return callback(err);
			breweryService.updateBreweryRatings(resource.brewery, function (err, entities) {
				callback(err, entity)
			})
		})
	},

	getById: function (id, callback) {
		reviewRepo.getById(id, function (err, entity) {
			if (entity == null && err != null) {
				err = errorHandler.throwMongoNotFoundError();
			}
			callback(err, entity);
		})
	},

	getReviewsByBrewery: function (breweryId, callback) {
		let query = {"brewery" : breweryId};
		reviewRepo.getByQuery(query, function (err, entities) {
			callback(err, entities)
		})
	}
};

function calculateAggregate(resource) {
	let aggregate = null;
	let sum = 0;

	let reviewArray = [];
	if (resource.friendlinessRating.kidsFood != null) {
		reviewArray.push(resource.friendlinessRating.kidsFood)
	}
	if (resource.friendlinessRating.kidsEntertainment != null) {
		reviewArray.push(resource.friendlinessRating.kidsEntertainment)
	}
	if (resource.friendlinessRating.bathrooms != null) {
		reviewArray.push(resource.friendlinessRating.bathrooms)
	}

	if (reviewArray.length) {
		sum = reviewArray.reduce(function (a, b) {return a + b;});
		aggregate = sum / reviewArray.length;
	}
	return aggregate;
}