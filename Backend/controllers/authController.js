const
    express = require('express'),
    authService = require('../services/authService');
    userService = require('../services/userService');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();

router.get('/login', function (req, res, next) {
    authService.authenticate(req.body.username, req.body.password, function (err, token) {
        res.locals.err = err;
        res.locals.statusCode = 200;
        res.locals.response = token;
        next()
    });
});

router.post('/createUser', function (req, res, next) {
    userService.createUserAndAuth(req.body, function (err, userEntity) {
        res.locals.err = err;
        res.locals.statusCode = 201;
        res.locals.response = "/api/user/" + userEntity._id;
        next()
    });
});

router.use(function (req, res) {
    errorChecking.checkErrors(req, res)
});

module.exports = router;