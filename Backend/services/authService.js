const
    hasher = require('password-hash');
    jwt = require('jsonwebtoken');

    authRepo = require('../models/auth/authRepository');
    userRepo = require('../models/user/userRepository');
    errorHandler = require('../error_handlers/errorHandler');

module.exports = {
    create: function (authResource, callback) {
        authResource.password = hasher.generate(authResource.password);
        authRepo.create(authResource, function (mongoErr) {
            if (mongoErr) {
                errorHandler.throwError(mongoErr, function (err) {
                    callback(err)
                })
            } else {
                callback(null)
            }
        })
    },

    authenticate: function (username, password, callback) {
        authRepo.getByUsernameAndPopulateUser(username, function (err, authEntity) {
            if (err) {
                errorHandler.throwError(function (err) {
                    callback(err, null)
                });
                callback(err)
            } else if (authEntity == null) {
                errorHandler.throwMongoNotFound(function (err) {
                    callback(err, null);
                })
            } else if(hasher.verify(password, authEntity.password) === false) {
                errorHandler.throwInvalidAuthentication(function (err) {
                    callback(err, null);
                });
            } else {
                let token = jwt.sign(authEntity.user.toJSON(), global.config.tokenSecret, {
                    expiresIn: global.config.tokenExpiresIn
                });
                callback(err, token)
            }
        })
    },
};