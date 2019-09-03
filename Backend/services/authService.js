const
    hasher = require('password-hash');
    jwt = require('jsonwebtoken');
    randtoken = require('rand-token');

    authRepo = require('../models/auth/authRepository');
    userRepo = require('../models/user/userRepository');
    refreshTokenRepo = require('../models/refreshToken/refreshTokenRepository');
    RefreshToken = require('../models/refreshToken/refreshTokenEntity');

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
        authRepo.getByUsernameAndPopulateUser(username, function (mongoErr, authEntity) {
            if (mongoErr) {
                errorHandler.throwError(function (err) {
                    callback(err, null)
                });
                callback(mongoErr)
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
                let refreshToken = randtoken.uid(256);
                let refreshTokenEntity = new RefreshToken({
                    createdAt: new Date().toISOString(),
                    refreshToken: refreshToken,
                    user: authEntity.user._id
                });
                refreshTokenRepo.create(refreshTokenEntity, function (mongoRefreshTokenError) {
                    if(mongoRefreshTokenError) {
                        errorHandler.throwError(mongoRefreshTokenError, function (err) {
                            callback(err, null)
                        })
                    } else {
                        let response = {
                            token: token,
                            refreshToken: refreshToken
                        };
                        callback(null, response)
                    }
                });
            }
        })
    },
    
    useRefreshToken: function (refreshToken, username, callback) {
        refreshTokenRepo.getByRefreshToken(refreshToken, function (mongoErr, entity) {
            if (mongoErr) {
                errorHandler.throwError(function (err) {
                    callback(err, null)
                });
                callback(mongoErr)
            } else if (entity == null) {
                errorHandler.throwMongoNotFound(function (err) {
                    callback(err, null);
                })
            } else if (entity.user.username !== username) {
                errorHandler.throwInvalidAuthentication(function (err) {
                    callback(err, null)
                })
            } else {
                let token = jwt.sign(entity.user.toJSON(), global.config.tokenSecret, {
                    expiresIn: global.config.tokenExpiresIn
                });
                refreshTokenRepo.updateCreatedAt(refreshToken, new Date().toISOString(), function (createErr) {
                    if(createErr) {
                        errorHandler.throwError(createErr, function (err) {
                            callback(err, null);
                        })
                    } else {
                        callback(null, token)
                    }
                })
            }
        })
    }
};