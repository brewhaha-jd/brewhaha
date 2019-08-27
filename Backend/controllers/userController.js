const
    express = require('express'),
    userService = require('../services/userService');
    mongoErrorHandler = require('../error_handlers/mongoErrorHandler');

let router = express.Router();

// router.use(function (req, res, next) {
// 	if (req.session.loginToken) {
// 		next()
// 	} else {
// 		res.status(403).send('Forbidden')
// 	}
// });

router.get('/', function (req, res, next) {
    userService.getAll(function (err, userEntities) {
        if(err) {
            res.locals.err = err;
        } else {
            res.locals.statusCode = 200;
            res.locals.response = userEntities;
        }
        next()
    });
});

router.get('/:id', function (req, res, next) {
    userService.getById(req.params.id, function (err, userEntity) {
        if(err) {
            res.locals.err = err;
        } else if(userEntity === null) {
            let errorResponse = mongoErrorHandler.throwMongoNotFound();
            res.locals.statusCode = errorResponse[0];
            res.locals.response = errorResponse[1];
        } else {
            res.locals.statusCode = 200;
            res.locals.response = userEntity;
        }
        next()
    });
});

router.post('/', function (req, res, next) {
    userService.createUserAndAuth(req.body, function (err, userEntity) {
        if(err) {
            res.locals.err = err;
        } else {
            res.locals.statusCode = 201;
            res.locals.response = "/api/user/" + userEntity._id;
        }
        next()
    });
});

router.use(function (req, res) {
    if(res.locals.err){
        let errorResponse = mongoErrorHandler.throwMongoError(res.locals.err);
        res.locals.statusCode = errorResponse[0];
        res.locals.response = errorResponse[1];
    }
    res.status(res.locals.statusCode).send(res.locals.response)
});

module.exports = router;