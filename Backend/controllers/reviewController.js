const
    express = require('express');
    reviewService = require('../services/reviewService');
    tokenValidation = require('../middleware/tokenValidation');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();


router.use(function (req, res, next) {
    tokenValidation.validateToken(req, res, next)
});

router.get('/:id', function (req, res, next) {
    reviewService.getById(req.params.id, function (err, reviewEntity) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(reviewEntity)
        }
    });
});

router.put('/', function (req, res, next) {
    let reviewResource = req.body;
    let user = req.decoded;
    reviewService.createReview(reviewResource, user._id,function (err, reviewEntity) {
        if (err) {
            next(err);
        } else {
            let breweryLink = "/api/review/" + reviewEntity._id;
            res.status(201).json({reviewLink: breweryLink})
        }
    })
});

router.use(function (err, req, res, next) {
    errorChecking.sendApiError(err, req, res)
});

module.exports = router;
