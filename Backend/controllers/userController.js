const
    express = require('express');
    userService = require('../services/userService');
    tokenValidation = require('../middleware/tokenValidation');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();


router.use(function (req, res, next) {
    tokenValidation.validateToken(req, res, next)
});

router.get('/', function (req, res, next) {
    userService.getAll(function (err, userEntities) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(userEntities)
        }
    });
});

router.get('/:id', function (req, res, next) {
    userService.getById(req.params.id, function (err, userEntity) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(userEntity)
        }
    });
});

router.use(function (err, req, res, next) {
    errorChecking.sendApiError(err, req, res)
});

module.exports = router;