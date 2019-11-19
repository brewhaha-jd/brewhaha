const express = require('express');
let router = express.Router();

router.use('/user', require("../controllers/userController"));
router.use('/auth', require("../controllers/authController"));
router.use('/brewery', require("../controllers/breweryController"));
router.use('/review', require("../controllers/reviewController"));

module.exports = router;