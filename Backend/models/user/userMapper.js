const User = require('./userEntity');

module.exports = {
	mapResourceToEntity: function (resource) {
		let entity = new User({
			username: resource.username,
			name: {
				firstName: resource.name.firstName,
				lastName: resource.name.lastName
			},
			email: resource.email,
			breweryManager: {
				isManager: resource.breweryManager.isManager,
				brewery: resource.breweryManager.brewery
			},
			location: resource.location
		});
		if (resource._id != null) {
			entity._id = resource._id;
		}
		return entity;
	}
};