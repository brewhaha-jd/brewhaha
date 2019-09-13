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
    breweryService.getAll(function (err, breweryEntities) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(breweryEntities)
        }
    });
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