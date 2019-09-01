/**
 * Created by nickhutchinson on 8/26/19.
 */
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const refreshTokenSchema = mongoose.Schema({
	createdAt: {
		type: Date,
		expires: global.config.refreshTokenExpiresIn,
		required: true
	},
	refreshToken: {
		type: String,
		required: true,
		unique: true
	},
	user: {
		type: Schema.Types.ObjectId,
		ref: "User"
	}
});

module.exports = mongoose.model('RefreshToken', refreshTokenSchema);