const
    express = require('express'),
    userService = require('../services/user');

let router = express.Router();

// router.use(function (req, res, next) {
// 	if (req.session.loginToken) {
// 		next()
// 	} else {
// 		res.status(403).send('Forbidden')
// 	}
// });

router.post('/', function (req, res) {
    userService.create(req.body, function (response) {
        res.status(response[0]).send(response[1])
    });
});

module.exports = router;