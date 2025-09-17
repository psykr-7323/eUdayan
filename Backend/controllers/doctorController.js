const Doctor = require("../models/doctor");
const User = require("../models/user");

// Register doctor (admin only)
exports.addDoctor = async (req, res) => {
  try {
    const { userId, specialization, availableSlots, experience, bio } = req.body;

    const user = await User.findById(userId);
    if (!user || user.role !== "doctor") {
      return res.status(400).json({ error: "User is not a doctor" });
    }

    const doctor = await Doctor.create({ userId, specialization, availableSlots, experience, bio });
    res.status(201).json(doctor);
  } catch (err) {
    res.status(500).json({ error: "Error adding doctor" });
  }
};

// Get all doctors
exports.getDoctors = async (req, res) => {
  try {
    const doctors = await Doctor.find().populate("userId", "name email profilePic");
    res.json(doctors);
  } catch (err) {
    res.status(500).json({ error: "Error fetching doctors" });
  }
};
