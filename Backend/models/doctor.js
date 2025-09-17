const mongoose = require("mongoose");

const doctorSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true, unique: true },
  specialization: { type: String, required: true, trim: true },
  availableSlots: [{ type: Date }], // Doctor’s available timings
  experience: { type: Number, min: 0 },
  bio: { type: String, maxlength: 500 },
}, { timestamps: true });

module.exports = mongoose.model("Doctor", doctorSchema);
