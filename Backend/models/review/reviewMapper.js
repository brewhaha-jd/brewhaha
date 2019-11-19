const Review = require('./reviewEntity');

module.exports = {
	mapResourceToEntity: function (resource) {
		let entity = new Review({
			user: resource.user,
			brewery: resource.brewery,
			datePosted: resource.datePosted,
			friendlinessRating: {
				aggregate: resource.friendlinessRating.aggregate,
				kidsFood: resource.friendlinessRating.kidsFood,
				kidsEntertainment: resource.friendlinessRating.kidsEntertainment,
				bathrooms: resource.friendlinessRating.bathrooms,
				minRecommendedAge: resource.friendlinessRating.minRecommendedAge
			},
			text: resource.text
		});
		if (resource._id != null) {
			entity._id = resource._id;
		}
		return entity;
	}
};