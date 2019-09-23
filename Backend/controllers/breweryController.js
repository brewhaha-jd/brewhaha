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
    if(req.query.location && req.query.range) {
        locationQuery(req, res, next)
    } else if (req.query.ratingType && req.query.rating) {
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

function ratingQuery(req, res, next) {
    breweryService.getBreweriesByRatings(req.query.ratingType, req.query.rating, true,
        function (err, entities) {
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