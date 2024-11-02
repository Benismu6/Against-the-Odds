const express = require('express');
const { getTeams, addTeam } = require('../controllers/teamController');

const router = express.Router();

router.get('/', getTeams);
router.post('/', addTeam);

module.exports = router;
