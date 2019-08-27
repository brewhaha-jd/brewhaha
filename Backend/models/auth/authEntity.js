/**
 * Created by nickhutchinson on 8/26/19.
 */
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const authSchema = mongoose.Schema({
	username: {
		type: String,
		required: true,
		unique: true
	},
	password: {
		type: String,
		required: true,
	},
	user: {
		type: Schema.Types.ObjectId,
		ref: "User"
	}
});

module.exports = mongoose.model('Auth', authSchema);