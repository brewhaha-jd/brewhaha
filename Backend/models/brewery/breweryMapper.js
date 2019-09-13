const Brewery = require('./breweryEntity');

module.exports = {
	mapResourceToEntity: function (resource) {
		if (resource.friendlinessRating === undefined) {
			resource.friendlinessRating = {
				aggregate: null,
				kidsFood: null,
				kidsEntertainment: null,
				bathrooms: null,
				minRecommendedAge: null
			};
		}
		let entity = new Brewery({
			name: resource.name,
			address: {
				number: resource.address.number,
				line1: resource.address.line1,
				line2: resource.address.line2,
				line3: resource.address.line3,
				city: resource.address.city,
				stateOrProvince: resource.address.stateOrProvince,
				county: resource.address.county,
				country: resource.address.country,
				postalCode: resource.address.postalCode,
				telephone: resource.address.telephone,
				location: {
					type: "Point",
					coordinates: [resource.address.longitude, resource.address.latitude]
				}
			},
			operatingHours: resource.operatingHours,
			website: resource.website,
			friendlinessRating : {
				aggregate: resource.friendlinessRating.aggregate,
				kidsFood: resource.friendlinessRating.kidsFood,
				kidsEntertainment: resource.friendlinessRating.kidsEntertainment,
				bathrooms: resource.friendlinessRating.bathrooms,
				minRecommendedAge: resource.friendlinessRating.minRecommendedAge
			}
		});
		if (resource._id != null) {
			entity._id = resource._id;
		}
		return entity;
	}
};