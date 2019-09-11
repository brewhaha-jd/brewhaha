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
        try {
            authResource.password = hasher.generate(authResource.password);
            authRepo.create(authResource, function (mongoErr) {
                callback(mongoErr, null)
            })
        } catch (e) {
            userRepo.delete(authResource.user, function (err) {
                if (err) console.log(err);
                callback(e, null)
            })
        }
    },

    logout: function (userId, callback) {
        refreshTokenRepo.deleteByUser(userId, function (mongoErr) {
            callback(mongoErr, null);
        })
    },

    authenticate: function (username, password, callback) {
        authRepo.getByUsernameAndPopulateUser(username, function (err, authEntity) {
            if (authEntity == null && err !== null) {
                err = errorHandler.throwMongoNotFoundError();
            } else if(hasher.verify(password, authEntity.password) === false) {
                err = errorHandler.throwInvalidAuthentication();
            }

            if (err) {
                callback(err, null)
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
                        callback(mongoRefreshTokenError, null)
                    } else {
                        let response = {
                            token: token,
                            refreshToken: refreshToken,
                            userId: authEntity.user._id
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
                callback(mongoErr, null);
            } else if (entity == null) {
                let err = errorHandler.throwMongoNotFoundError();
                callback(err, null);
            } else if (entity.user.username !== username) {
                let err = errorHandler.throwInvalidAuthentication();
                callback(err, null);
            } else {
                let token = jwt.sign(entity.user.toJSON(), global.config.tokenSecret, {
                    expiresIn: global.config.tokenExpiresIn
                });
                refreshTokenRepo.updateCreatedAt(refreshToken, new Date().toISOString(), function (createErr) {
                    callback(createErr, token);
                })
            }
        })
    }
};