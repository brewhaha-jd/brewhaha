const
	userRepo = require('../models/user/userRepository');
	Auth = require('../models/auth/authEntity');
	authService = require('../services/authService');
	errorHandler = require('../error_handlers/errorHandler');

module.exports = {
	getAll: function (callback) {
		userRepo.getAll(function (mongoErr, entities) {
			if (mongoErr) {
				errorHandler.throwError(mongoErr, function (err) {
					callback(err)
				})
			} else {
				callback(null, entities)
			}
		})
	},

	getById: function (id, callback) {
		userRepo.getById(id, function (mongoErr, entity) {
			if (mongoErr) {
				errorHandler.throwError(mongoErr, function (err) {
					callback(err)
				})
			} else if (entity === null) {
				errorHandler.throwMongoNotFound(function (err) {
					callback(err)
				})
			} else {
				callback(null, entity)
			}
		})
	},

	createUserAndAuth: function (resource, callback) {
		userRepo.create(resource, function (mongoUserErr, userEntity) {
			if(mongoUserErr) {
				errorHandler.throwError(mongoUserErr, function (err) {
					callback(err, null)
				})
			} else {
				let authResource = new Auth({
					username: userEntity.username,
					password: resource.password,
					user: userEntity._id
				});
				authService.create(authResource, function (mongoAuthErr) {
					if (mongoAuthErr) {
						errorHandler.throwError(mongoAuthErr, function (err) {
							callback(err, null)
						})
					} else {
						callback(null, userEntity)
					}
				});
			}
		})
	}
};