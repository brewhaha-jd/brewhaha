module.exports = {

    getAll: function (callback) {
        User.find().exec(function (err, entities) {
            callback(err, entities)
        });
    },

    getById: function (id, callback) {
        User.findById(id,function (err, entity) {
            callback(err, entity)
        })
    },

	create: function (resource, callback) {
		const entity = userMapper.mapResourceToEntity(resource);
		entity.save(function (err, entity) {
            callback(err, entity);
		});
	}
};




const User = require('./userEntity');
const userMapper = require('./userMapper');