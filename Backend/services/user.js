module.exports = {

	create: function (resource, callback) {
		userRepo.create(resource, function (entity) {
			callback(entity)
		})
	}
};

const userRepo = require('../models/user/userRepository');