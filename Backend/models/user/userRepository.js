module.exports = {

	create: function (resource, callback) {
		resource.save(function (err) {
			if (err) {
				console.log(err);
				callback(null)
			} else {
				callback(resource)
			}
		});
	}
};




const User = require('./userEntity');