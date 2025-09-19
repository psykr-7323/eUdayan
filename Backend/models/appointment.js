const mongoose = require("mongoose");

const appointmentSchema = new mongoose.Schema({
  studentId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  doctorId: { type: mongoose.Schema.Types.ObjectId, ref: "Doctor", required: true },
  date: { type: Date, required: true },
  status: { 
    type: String, 
    enum: ["pending", "confirmed", "cancelled", "completed"], 
    default: "pending" 
  },
  notes: { type: String, maxlength: 1000 },
}, { timestamps: true });

module.exports = mongoose.model("Appointment", appointmentSchema);
