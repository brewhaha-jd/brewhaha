const express = require('express');
let router = express.Router();

router.use('/user', require("../controllers/userController"));
router.use('/auth', require("../controllers/authController"));
router.use('/brewery', require("../controllers/breweryController"));

module.exports = router;