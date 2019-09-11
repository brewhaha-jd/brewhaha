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