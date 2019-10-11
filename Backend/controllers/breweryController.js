const
    express = require('express');
    breweryService = require('../services/breweryService');
    tokenValidation = require('../middleware/tokenValidation');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();


router.use(function (req, res, next) {
    tokenValidation.validateToken(req, res, next)
});

router.get('/', function (req, res, next) {
    if(req.query.location && req.query.range && !req.query.ratings) {
        locationQuery(req, res, next)
    } else if (req.query.location && req.query.range && req.query.ratings) {
        locationAndRatingQuery(req, res, next)
    } else if (req.query.ratings) {
        ratingQuery(req, res, next)
    } else if (req.query.name) {
        nameQuery(req, res, next)
    } else {
        breweryService.getAll(function (err, breweryEntities) {
            if (err) {
                next(err);
            } else {
                res.status(200).json(breweryEntities)
            }
        });
    }
});

router.get('/:id', function (req, res, next) {
    breweryService.getById(req.params.id, function (err, breweryEntity) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(breweryEntity)
        }
    });
});

router.put('/', function (req, res, next) {
    let breweryResource = req.body;
    breweryService.createBreweryAndMapCoordinates(breweryResource, function (err, breweryEntity) {
        if (err) {
            next(err);
        } else {
            let breweryLink = "/api/brewery/" + breweryEntity._id;
            res.status(201).json({userLink: breweryLink})
        }
    })
});

router.post('/:id', function (req, res, next) {
    breweryService.updateBrewery(req.params.id, req.body, function (err, entity) {
        if (err) {
            next(err)
        } else {
            res.status(200).json(entity)
        }
    })
})

router.use(function (err, req, res, next) {
    errorChecking.sendApiError(err, req, res)
});

module.exports = router;

function locationQuery(req, res, next) {
    breweryService.getBreweriesNearLocation(req.query.location.split(","), req.query.range, true,
        function (err, entities) {
        if (err) {
            next(err)
        } else {
            res.status(200).json(entities)
        }
    })
}

function locationAndRatingQuery(req, res, next) {
    let ratings = processRatingQueries(req);
    breweryService.getBreweriesByLocationAndRatings(req.query.location.split(","), req.query.range, ratings, function (err, entities) {
        if (err) {
            next(err)
        } else {
            res.status(200).json(entities)
        }
    })
}

function ratingQuery(req, res, next) {
    let ratings = processRatingQueries(req);
    breweryService.getBreweriesByRatings(ratings, true,function (err, entities) {
        if (err) {
            next(err)
        } else {
            res.status(200).json(entities)
        }
    })
}

function nameQuery(req, res, next) {
    breweryService.getBreweriesBySearchingName(req.query.name, function (err, entities) {
        if (err) {
            next(err)
        } else {
            res.status(200).json(entities)
        }
    })
}

function processRatingQueries(req) {
    let queries = [];
    if (req.query.aggregate) {
        queries.push({
            name: "aggregate",
            rating: req.query.aggregate
        });
    }
    if (req.query.kidsFood) {
        queries.push({
            name: "kidsFood",
            rating: req.query.kidsFood
        });
    }
    if (req.query.kidsEntertainment) {
        queries.push({
            name: "kidsEntertainment",
            rating: req.query.kidsEntertainment
        });
    }
    if (req.query.bathrooms) {
        queries.push({
            name: "bathrooms",
            rating: req.query.bathrooms
        });
    }
    if (req.query.minRecommendedAge) {
        queries.push({
            name: "minRecommendedAge",
            rating: req.query.minRecommendedAge
        });
    }
    return queries;
}