const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
  senderId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  receiverId: { type: mongoose.Schema.Types.ObjectId, ref: "User" }, // null if bot
  message: { type: String, required: true, trim: true, maxlength: 2000 },
  type: { type: String, enum: ["student", "doctor", "bot"], default: "student" },
  isRead: { type: Boolean, default: false },
}, { timestamps: true });

module.exports = mongoose.model("Chat", chatSchema);
