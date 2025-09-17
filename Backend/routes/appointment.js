const express = require('express');
const router = express.Router();
const {
  bookAppointment,
  getAppointments,
  updateStatus,
} = require('../controllers/appointmentController.js');
const authMiddleware = require('../middleware/authMiddleware.js');
const authorizeRoles = require('../middleware/roleMiddleware.js');

// Student books an appointment
router.post('/', authMiddleware.protect, authorizeRoles('student'), bookAppointment);

// User gets their own appointments
router.get('/', authMiddleware.protect, getAppointments);

// Doctor/Admin updates appointment status
router.patch('/:id/status', authMiddleware.protect, authorizeRoles('doctor', 'admin'), updateStatus);

module.exports = router;