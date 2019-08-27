module.exports = {

	create: function (resource, callback) {
		const entity = userMapper.mapResourceToEntity(resource);
		entity.save(function (err) {
			if (err) {
				callback(mongoErrorHandler.throwMongoError(err))
			} else {
				callback([201, entity])
			}
		});
	}
};




const User = require('./userEntity');
const userMapper = require('./userMapper');
const mongoErrorHandler = require('../../error_handlers/mongo');