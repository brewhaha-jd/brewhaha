module.exports = {

	create: function (authEntity, callback) {
		authEntity.save(function (err) {
			    callback(err)
		});
	},

	getByUsernameAndPopulateUser: function (username, callback) {
		Auth.findOne({username: username}).populate('user').exec(function (err, entity) {
			callback(err, entity)
		})
	}
};




const Auth = require('./authEntity');
