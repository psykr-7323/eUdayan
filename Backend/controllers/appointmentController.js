const Appointment = require("../models/appointment");
const { successResponse, errorResponse } = require("../utils/response.js");

// Book appointment
exports.bookAppointment = async (req, res) => {
  try {
    const { doctorId, date, notes } = req.body;

    const appointment = await Appointment.create({
      studentId: req.user.id,
      doctorId,
      date,
      notes,
    });

    return successResponse(res, "Appointment booked successfully", appointment, 201);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error booking appointment");
  }
};

// Get user’s appointments
exports.getAppointments = async (req, res) => {
  try {
    const appointments = await Appointment.find({ studentId: req.user.id })
      .populate("doctorId", "specialization userId")
      .sort({ date: 1 });

    return successResponse(res, "Appointments fetched successfully", appointments);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error fetching appointments");
  }
};

// Update appointment status (doctor/admin)
exports.updateStatus = async (req, res) => {
  try {
    const { id } = req.params;
    const { status } = req.body;

    const appointment = await Appointment.findByIdAndUpdate(id, { status }, { new: true });

    if (!appointment) {
      return errorResponse(res, "Appointment not found", 404);
    }

    return successResponse(res, "Appointment status updated", appointment);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error updating status");
  }
};
