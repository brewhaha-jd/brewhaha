const
	userRepo = require('../models/user/userRepository');
	Auth = require('../models/auth/authEntity');
	authService = require('../services/authService');
	errorHandler = require('../error_handlers/errorHandler');

module.exports = {
	getAll: function (callback) {
		userRepo.getAll(function (mongoErr, entities) {
			callback(mongoErr, entities);
		})
	},

	getById: function (id, callback) {
		userRepo.getById(id, function (err, entity) {
			if (entity == null && err != null) {
				err = errorHandler.throwMongoNotFoundError();
			}
			callback(err, entity);
		})
	},

	createUserAndAuth: function (resource, callback) {
		userRepo.create(resource, function (mongoUserErr, userEntity) {
			if (mongoUserErr) {
				callback(mongoUserErr, null)
			} else {
				let authResource = new Auth({
					username: userEntity.username,
					password: resource.password,
					user: userEntity._id
				});
				authService.create(authResource, function (mongoAuthErr) {
					if (mongoAuthErr) {
						callback(mongoAuthErr, null)
					} else {
						callback(null, userEntity)
					}
				});
			}
		})
	}
};