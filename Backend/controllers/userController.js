const
    express = require('express'),
    userService = require('../services/userService');
    tokenValidation = require('../middleware/tokenValidation');

let router = express.Router();


router.use(function (req, res, next) {
    tokenValidation.validateToken(req, res, next)
});

router.get('/', function (req, res, next) {
    userService.getAll(function (err, userEntities) {
        res.locals.err = err;
        res.locals.statusCode = 200;
        res.locals.response = userEntities;
        next()
    });
});

router.get('/:id', function (req, res, next) {
    userService.getById(req.params.id, function (err, userEntity) {
        res.locals.err = err;
        res.locals.statusCode = 200;
        res.locals.response = userEntity;
        next()
    });
});


router.use(function (req, res) {
    if(res.locals.err){
        res.locals.statusCode = res.locals.err.statusCode;
        res.locals.response = res.locals.err.response;
    }
    res.status(res.locals.statusCode).send(res.locals.response)
});

module.exports = router;