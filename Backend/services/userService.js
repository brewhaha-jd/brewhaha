module.exports = {
	getAll: function (callback) {
		userRepo.getAll(function (err, entities) {
			callback(err, entities)
		})
	},

	getById: function (id, callback) {
		userRepo.getById(id, function (err, entity) {
			callback(err, entity)
		})
	},
	createUserAndAuth: function (resource, callback) {
		userRepo.create(resource, function (userErr, userEntity) {
			if(userErr) {
				callback(userErr, null)
			} else {
				let authResource = new Auth({
					username: userEntity.username,
					password: resource.password,
					user: userEntity._id
				});
				authService.create(authResource, function (authErr) {
					if (authErr) {
						callback(authErr, null)
					} else {
						callback(userErr, userEntity)
					}
				});
			}
		})
	}
};

const userRepo = require('../models/user/userRepository');
const Auth = require('../models/auth/authEntity');
const authService = require('../services/authService');