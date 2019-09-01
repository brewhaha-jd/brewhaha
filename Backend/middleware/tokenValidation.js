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









// var jwt = require('jsonwebtoken');
//
// module.exports = function(req,res,next) {
//     var token = req.body.token || req.query.token || req.headers['x-access-token'];
//     // decode token
//     if (token) {
//         // verifies secret and checks exp
//         jwt.verify(token, global.config.secret, function(err, decoded) {
//             if (err) {
//                 return res.json({"error": true, "message": 'Failed to authenticate token.' });
//             }
//             req.decoded = decoded;
//             next();
//         });
//     } else {
//         // if there is no token
//         // return an error
//         return res.status(403).send({
//             "error": true,
//             "message": 'No token provided.'
//         });
//     }
// }