const RefreshToken = require('./refreshTokenEntity');

module.exports = {

	create: function (refreshTokenEntity, callback) {
		refreshTokenEntity.save(function (err) {
			    callback(err)
		});
	},

	getByRefreshToken: function (refreshToken, callback) {
		RefreshToken.findOne({refreshToken: refreshToken}).populate('user').exec(function (err, entity) {
			callback(err, entity)
		})
	},

	updateCreatedAt: function (refreshToken, newCreatedAt, callback) {
		RefreshToken.updateOne({refreshToken: refreshToken}, {createdAt: newCreatedAt}, function (err) {
			callback(err)
		})
	},

	deleteByUser: function (userId, callback) {
		RefreshToken.deleteOne({user: userId}, function (err) {
			callback(err)
		})
	}
};
