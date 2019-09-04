const
    express = require('express');
    authService = require('../services/authService');
    userService = require('../services/userService');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();

router.post('/createUser', function (req, res, next) {
    userService.createUserAndAuth(req.body, function (err, userEntity) {
        res.locals.err = err;
        res.locals.statusCode = 201;
	if (userEntity !== null) {
	    res.locals.response = "/api/user/" + userEntity._id;
	}	
        next()
    });
});

router.post('/login', function (req, res, next) {
    authService.authenticate(req.body.username, req.body.password, function (err, tokens) {
        res.locals.err = err;
        res.locals.statusCode = 200;
        res.locals.response = tokens;
        next()
    });
});

router.post('/token', function (req, res, next) {
    authService.useRefreshToken(req.body.refreshToken, req.body.username, function (err, newToken) {
        res.locals.err = err;
        res.locals.statusCode = 202;
        res.locals.response = newToken;
        next()
    });
});



router.use(function (req, res) {
    errorChecking.checkErrors(req, res)
});

module.exports = router;
