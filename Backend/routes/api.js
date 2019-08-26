const express = require('express');
let router = express.Router();

router.use('/user', require("../controllers/users"));

module.exports = router;