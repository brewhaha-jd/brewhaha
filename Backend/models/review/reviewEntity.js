/**
 * Created by nickhutchinson on 11/18/19.
 */
const
    mongoose = require('mongoose');
    Schema = mongoose.Schema;

const reviewSchema = mongoose.Schema({
    user: {
        type: Schema.Types.ObjectId,
        ref: "User",
        required: true
    },
    brewery: {
        type: Schema.Types.ObjectId,
        ref: "Brewery",
        required: true
    },
    datePosted: {
        type: Date,
        required: true
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
    },
    text: {
        type: String,
        required: false
    }
});

module.exports = mongoose.model('Review', reviewSchema);