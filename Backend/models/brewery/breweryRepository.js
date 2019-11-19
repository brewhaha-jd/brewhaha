const
    Brewery = require('./breweryEntity');
    errorHandler = require('../../error_handlers/errorHandler');

module.exports = {

    create: function (entity, callback) {
        entity.save(function (err, entity) {
            callback(err, entity);
        });
    },

    getAll: function (callback) {
        Brewery.find().exec(function (err, entities) {
            callback(err, entities)
        });
    },

    getById: function (id, callback) {
        Brewery.findById(id,function (err, entity) {
            callback(err, entity)
        })
    },

	getByQuery: function(query, callback) {
    	Brewery.find(query, function (err, entities) {
			callback(err, entities)
		})
	},

    getByAggregate: function(query, callback) {
        Brewery.aggregate(query, function (err, entities) {
            callback(err, entities)
        })
    },

    update: function(id, resource, callback) {
        Brewery.findById(id, function (err, entity) {
            if (err) return callback(err);
            if (entity === null) return callback(errorHandler.throwMongoNotFoundError());
            entity.address = resource.address;
            entity.name = resource.name;
            entity.website = resource.website;
            entity.__v += 1;
            entity.save();
            callback(err, entity)
        })
    },

    updateReviews: function (id, reviewAverages, callback) {
        Brewery.findById(id, function (err, entity) {
            if (err) return callback(err);
            if (entity === null) return callback(errorHandler.throwMongoNotFoundError());
            // noinspection DuplicatedCode
            entity.friendlinessRating.aggregate = reviewAverages.aggregate;
            entity.friendlinessRating.kidsFood = reviewAverages.kidsFoodAvg;
            entity.friendlinessRating.kidsEntertainment = reviewAverages.kidsEntertainmentAvg;
            entity.friendlinessRating.bathrooms = reviewAverages.bathroomsAvg;
            entity.friendlinessRating.minRecommendedAge = reviewAverages.minAgeAvg;
            entity.__v += 1;
            entity.save();
            callback(err, entity)
        })
    },

    delete: function (breweryId, callback) {
        Brewery.deleteOne({_id: breweryId}, function (err) {
            callback(err)
        })
    }
};