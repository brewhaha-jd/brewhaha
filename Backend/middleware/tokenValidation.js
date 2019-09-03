const
    jwt = require('jsonwebtoken');
    errorHandler = require('../error_handlers/errorHandler');

module.exports = {
    validateToken: function (req, res, next) {
        var token = req.headers['x-access-token'];
        if(token) {
            jwt.verify(token, global.config.tokenSecret, function (err, decoded) {
                if (err) {
                    errorHandler.throwInvalidAuthentication(function (err) {
                        res.status(err.statusCode).send(err.response)
                    })
                } else {
                    req.decoded = decoded;
                    next();
                }
            })
        } else {
            errorHandler.throwNoTokenProvidedError(function (err) {
                res.status(err.statusCode).send(err.response)
            })
        }
    }
};