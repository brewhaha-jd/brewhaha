/**
 * Created by nickhutchinson on 9/11/19.
 */
const mongoose = require('mongoose');

const brewerySchema = mongoose.Schema({
	name: {
		type: String,
		required: true
	},
	address: {
		number: {
			type: Number,
			required: true
		},
		line1: {
			type: String,
			required: true
		},
		line2: {
			type: String,
			required: false
		},
		line3: {
			type: String,
			required: false
		},
		city: {
			type: String,
			required: true
		},
		stateOrProvince: {
			type: String,
			required: true
		},
		county: {
			type: String,
			required: false
		},
		country: {
			type: String,
			required: true
		},
		postalCode: {
			type: Number,
			required: true
		},
		telephone: {
			type: String,
			required: true
		},
		latitude: {
			type: Number,
			required: true
		},
		longitude: {
			type: Number,
			required: true
		},
	},
	operatingHours: {},
	website: {
		type: String,
		required: false
	},
	friendlinessRating : {
		aggregate: {
			type: Number,
			required: false
		},
		kidsFood: {
			type: Number,
			required: false
		},
		kidsEntertainment: {
			type: Number,
			required: false
		},
		bathrooms: {
			type: Number,
			required: false
		},
		minRecommendedAge: {
			type: Number,
			required: false
		}
	}
});

module.exports = mongoose.model('Brewery', brewerySchema);