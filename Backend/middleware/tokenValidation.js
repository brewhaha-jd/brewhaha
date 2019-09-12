const
    jwt = require('jsonwebtoken');
    errorHandler = require('../error_handlers/errorHandler');

module.exports = {
    validateToken: function (req, res, next) {
        let token = req.headers['x-access-token'];
        if(token) {
            jwt.verify(token, global.config.tokenSecret, function (err, decoded) {
                if (err) {
                    err = errorHandler.throwInvalidAuthentication();
                    next(err)
                } else {
                    req.decoded = decoded;
                    next();
                }
            })
        } else {
            next(errorHandler.throwNoTokenProvidedError())
        }
    }
};