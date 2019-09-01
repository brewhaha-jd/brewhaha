const
    express = require('express'),
    authService = require('../services/authService');
    userService = require('../services/userService');

let router = express.Router();

router.get('/login', function (req, res, next) {
    authService.authenticate(req.body.username, req.body.password, function (err, token) {
        if(err) {
            res.locals.err = err;
        } else {
            res.locals.statusCode = 200;
            res.locals.response = token;
        }
        next()
    });
});

router.post('/createUser', function (req, res, next) {
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
        res.locals.statusCode = res.locals.err.statusCode;
        res.locals.response = res.locals.err.response;
    }
    res.status(res.locals.statusCode).send(res.locals.response)
});

module.exports = router;