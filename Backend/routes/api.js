const express = require('express');
let router = express.Router();

router.use('/user', require("../controllers/userController"));

module.exports = router;