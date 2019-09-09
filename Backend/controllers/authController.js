const
    express = require('express');
    authService = require('../services/authService');
    userService = require('../services/userService');
    errorChecking = require('../middleware/errorChecking');

let router = express.Router();

router.post('/createUser', function (req, res, next) {
    userService.createUserAndAuth(req.body, function (err, userEntity) {
        if (err) {
            next(err);
        } else {
            let userLink = "/api/user/" + userEntity._id;
            res.status(201).json({userLink: userLink})
        }
    });
});

router.post('/login', function (req, res, next) {
    authService.authenticate(req.body.username, req.body.password, function (err, response) {
        if (err) {
            next(err);
        } else {
            res.status(200).json(response)
        }
    });
});

router.post('/token', function (req, res, next) {
    authService.useRefreshToken(req.body.refreshToken, req.body.username, function (err, newToken) {
        if (err) {
            next(err);
        } else {
            res.status(202).json({token: newToken})
        }
    });
});

router.post('/logout', function (req, res, next) {
    authService.logout(req.body.userId, function (err) {
        if (err) {
            next(err);
        } else {
            res.status(204).send()
        }
    })
});

router.use(function (err, req, res, next) {
    errorChecking.sendApiError(err, req, res)
});

module.exports = router;
