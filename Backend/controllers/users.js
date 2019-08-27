const
    express = require('express'),
    userService = require('../services/user');
const mongoErrorHandler = require('../error_handlers/mongo');

let router = express.Router();

// router.use(function (req, res, next) {
// 	if (req.session.loginToken) {
// 		next()
// 	} else {
// 		res.status(403).send('Forbidden')
// 	}
// });

router.get('/', function (req, res, next) {
    userService.getAll(function (response) {
        res.locals.response = response;
        next()
    });
});

router.get('/:id', function (req, res, next) {
    userService.getById(req.params.id, function (response) {
        res.locals.response = response;
        next()
    });
});

router.post('/', function (req, res, next) {
    userService.create(req.body, function (response) {
        res.locals.response = response;
        next()
    });
});

router.use(function (req, res) {
    let statusCode = res.locals.response[0];
    let response = res.locals.response[1];
    res.status(statusCode).send(response)
});

module.exports = router;