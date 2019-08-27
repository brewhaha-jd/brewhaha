module.exports = {

    getAll: function (callback) {
        User.find().exec(function (err, entities) {
            if (err) {
                console.log(err)
            }
            callback([200, entities])
        });
    },

    getById: function (id, callback) {
        User.findById(id,function (err, entity) {
            if (err) {
                console.log(err)
            }
            callback([200, entity])
        })
    },

	create: function (resource, callback) {
		const entity = userMapper.mapResourceToEntity(resource);
		entity.save(function (err) {
			if (err) {
				callback(mongoErrorHandler.throwMongoError(err))
			} else {
			    let uri = "/api/user/" + entity._id;
				callback([201, uri])
			}
		});
	}
};




const User = require('./userEntity');
const userMapper = require('./userMapper');
const mongoErrorHandler = require('../../error_handlers/mongoErrorHandler');