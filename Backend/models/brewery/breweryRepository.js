const
    Brewery = require('./breweryEntity');

module.exports = {

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

	create: function (entity, callback) {
		entity.save(function (err, entity) {
            callback(err, entity);
		});
	},

    delete: function (breweryId, callback) {
        Brewery.deleteOne({_id: breweryId}, function (err) {
            callback(err)
        })
    }
};