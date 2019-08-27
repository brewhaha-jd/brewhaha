module.exports = {
	getAll: function (callback) {
		userRepo.getAll(function (entities) {
			callback(entities)
		})
	},

	getById: function (id, callback) {
		userRepo.getById(id, function (entity) {
			callback(entity)
		})
	},
	create: function (resource, callback) {
		userRepo.create(resource, function (entity) {
			callback(entity)
		})
	}
};

const userRepo = require('../models/user/userRepository');