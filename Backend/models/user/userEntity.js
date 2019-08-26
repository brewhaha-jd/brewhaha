/**
 * Created by nickhutchinson on 8/26/19.
 */
const mongoose = require('mongoose');

const userSchema = mongoose.Schema({
	username: {
		type: String,
		required: true,
		unique: true
	},
	name: {
		firstName: String,
		lastName: String
	},
	email: {
		type: String,
		required: true,
		unique: true
	},
	breweryManager: {
		isManager: Boolean,
		brewery: String
	},
	location: String
});

module.exports = mongoose.model('User', userSchema);