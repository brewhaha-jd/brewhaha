const
    Review = require('./reviewEntity');
    reviewMapper = require('./reviewMapper');

module.exports = {

    create: function (resource, callback) {
        const entity = reviewMapper.mapResourceToEntity(resource);
        entity.save(function (err, entity) {
            callback(err, entity);
        });
    },

    getAll: function (callback) {
        Review.find().exec(function (err, entities) {
            callback(err, entities)
        });
    },

    getById: function (id, callback) {
        Review.findById(id,function (err, entity) {
            callback(err, entity)
        })
    },

    getByQuery: function (query, callback) {
        Review.find(query, function (err, entities) {
            callback(err, entities)
        })
    }
};